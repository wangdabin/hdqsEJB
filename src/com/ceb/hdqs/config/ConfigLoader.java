package com.ceb.hdqs.config;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.ManagedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ceb.hdqs.entity.XtpzEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.service.XtpzService;
import com.ceb.hdqs.utils.DateFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;
import com.ceb.hdqs.wtc.HdqsWtcException;

public final class ConfigLoader extends AbstractLoader {

	private static Object lock = new Object();
	private volatile static ConfigLoader instance = null;
	private static PropertiesConfiguration config = new PropertiesConfiguration();
	private static ManagedReloadingStrategy strategy = new ManagedReloadingStrategy();
	private static boolean loadFromDB = false;

	private ConfigLoader() {
		String loadCfgFromDB = PropertiesLoader.getInstance().getProperty(RegisterTable.LOAD_CONFIG_FROM_DB, "false");
		if (loadCfgFromDB.equalsIgnoreCase("true")) {
			loadFromDB = true;
		} else {
			loadFromDB = false;
		}
		if (loadFromDB) {
			refreshDB();
		} else {
			String hdqsHome = getEnvValue(HDQS_HOME);
			if (StringUtils.isBlank(hdqsHome)) {
				System.err.println("ERROR: please set " + HDQS_HOME + " in env.");
				System.exit(-1);
			}

			try {
				config.load(new File(HdqsUtils.formatFilePath(hdqsHome), "hdqs.properties"));
				config.setReloadingStrategy(strategy);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		refreshConfiguration();
	}

	public static ConfigLoader getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new ConfigLoader();
				}
			}
		}
		return instance;
	}

	public void reloadConfig() {
		if (loadFromDB) {
			refreshDB();
		} else {
			strategy.refresh();
		}
		refreshConfiguration();
		refreshLog4j();
	}

	/**
	 * <pre>
	 * 
	 * public static final org.apache.log4j.Level FATAL;
	 * 
	 * public static final org.apache.log4j.Level ERROR;
	 * 
	 * public static final org.apache.log4j.Level WARN;
	 * 
	 * public static final org.apache.log4j.Level INFO;
	 * 
	 * public static final org.apache.log4j.Level DEBUG;
	 * 
	 * 
	 * </pre>
	 * 
	 * @return
	 */
	public String refreshLog4j() {
		Iterator<String> iter = config.getKeys(RegisterTable.LOG4J_CFG_PREFIX);
		while (iter.hasNext()) {
			String key = iter.next();
			String value = config.getString(key);
			String pack = key.substring(RegisterTable.LOG4J_CFG_PREFIX.length() + 1);
			Level level = Level.toLevel(value.toUpperCase());
			Logger logger = LogManager.getLogger(pack);
			logger.setLevel(level);
		}
		return "Log4j level set success.";
	}

	private void refreshConfiguration() {
		if (HdqsUtils.isWindows()) {
			return;
		}
		Map<String, Configuration> map = QueryConfUtils.getClusterConfig();
		if (map == null || map.isEmpty()) {
			System.err.println("ERROR: Cluster configuration is null ,weblogic will exit.");
			System.exit(-1);
		}
		for (Entry<String, Configuration> mapConfig : map.entrySet()) {
			Configuration configuration = null;
			try {
				configuration = mapConfig.getValue();

				Iterator<String> iter = config.getKeys(RegisterTable.HDQS_QUERY_CFG_PREFIX);
				while (iter.hasNext()) {
					String key = iter.next();
					String value = config.getString(key);
					configuration.set(key, value);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}

	private void refreshDB() {
		try {
			XtpzService xtpzService = (XtpzService) JNDIUtils.lookup(XtpzService.class);
			List<XtpzEO> list = xtpzService.findAll();
			for (XtpzEO obj : list) {
				config.setProperty(obj.getName(), obj.getValue());
			}
		} catch (NamingException e) {
			System.err.println("ERROR: Load config from database failure!");
			System.exit(-1);
		}
	}

	public String getProperty(String key) {
		return config.getString(key);
	}

	public String getProperty(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	public int getInt(String name, int defaultValue) {
		String valueString = getTrimmed(name);
		if (valueString == null)
			return defaultValue;

		return Integer.parseInt(valueString);
	}

	public long getLong(String name, long defaultValue) {
		String valueString = getTrimmed(name);
		if (valueString == null)
			return defaultValue;

		return Long.parseLong(valueString);
	}

	public boolean getBoolean(String name, boolean defaultValue) {
		String valueString = getTrimmed(name);
		if (null == valueString || "".equals(valueString)) {
			return defaultValue;
		}
		valueString = valueString.toLowerCase();
		if ("true".equals(valueString))
			return true;
		else if ("false".equals(valueString))
			return false;
		else
			return defaultValue;
	}

	public String getTrimmed(String name) {
		String value = getProperty(name);

		if (null == value) {
			return null;
		} else {
			return value.trim();
		}
	}

	/**
	 * 查询时间是否落在查询时间截止阀值(格式YYYYMMDD)之前
	 */
	public static void fitTimeLimit(String endDate, String startDate) throws HdqsWtcException {
		fitTimeLimit(endDate, startDate, "");
	}

	public static void fitTimeLimit(String endDate, String startDate, String message) throws HdqsWtcException {
		ConfigLoader instance = ConfigLoader.getInstance();
		String switchEndStr = instance.getProperty(RegisterTable.QUERY_END_TIME_LIMIT_SWITCH);
		String switchStartStr = instance.getProperty(RegisterTable.QUERY_START_TIME_LIMIT_SWITCH);
		if (HdqsUtils.isNotBlank(switchStartStr) && switchStartStr.equalsIgnoreCase("true")) {
			String timeLimit = instance.getProperty(RegisterTable.QUERY_START_TIME_LIMIT);
			if (startDate.compareTo(timeLimit) < 0) {
				throw new HdqsWtcException(message + "查询起始时间小于设定查询日期阀值" + timeLimit);
			}
		}
		if (HdqsUtils.isNotBlank(switchEndStr) && switchEndStr.equalsIgnoreCase("true")) {
			String timeLimit = instance.getProperty(RegisterTable.QUERY_END_TIME_LIMIT);
			if (endDate.compareTo(timeLimit) > 0) {
				throw new HdqsWtcException(message + "查询结束时间超过设定查询日期阀值" + timeLimit);
			}
		}
	}

	/**
	 * 时间跨度是否超过days限制
	 * 
	 * @param startStr
	 * @param endStr
	 * @return
	 */
	public static void overflowMonthLimit(String startStr, String endStr) throws HdqsWtcException {
		String spanStr = ConfigLoader.getInstance().getProperty(RegisterTable.QUERY_DAY_SPAN);
		boolean result = false;
		try {
			Date startDate = DateFormatUtils.parseDate(startStr);
			Date endDate = DateFormatUtils.parseDate(endStr);

			if (HdqsUtils.isNotBlank(spanStr) && HdqsUtils.isNumeric(spanStr)) {
				int span = Integer.parseInt(spanStr);
				if (endDate.getTime() - startDate.getTime() > TimeUnit.DAYS.toMillis(span)) {
					result = true;
				}
			}
		} catch (ParseException e) {
			result = true;
		}
		if (result) {
			throw new HdqsWtcException("起止日期时间间隔不能超过" + spanStr + "天");
		}
	}

	public static void main(String[] args) {
		ConfigLoader.getInstance().getProperty("APP.STDOUT.LOG");
	}
}