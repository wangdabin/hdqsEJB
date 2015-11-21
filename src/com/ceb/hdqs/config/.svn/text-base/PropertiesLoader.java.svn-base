package com.ceb.hdqs.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;

import org.apache.commons.configuration.DefaultFileSystem;
import org.apache.commons.configuration.FileSystem;
import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.JyrqService;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;

public final class PropertiesLoader extends AbstractLoader {
	
	private static Object lock = new Object();
	private volatile static PropertiesLoader instance = null;
	private static Properties properties = new Properties();
	private static boolean jioyrqUseCatch = false;

	private PropertiesLoader() {
		System.out.println("Load config at " + DateTimeFormatUtils.formatDate(new Date()));
		String hdqsHome = getEnvValue(HDQS_HOME);
		if (StringUtils.isBlank(hdqsHome)) {
			System.err.println("ERROR: please set " + HDQS_HOME + " in env.");
			System.exit(-1);
		}
		setLogEnv(RegisterTable.APP_STDOUT_LOG);
		// ClassLoader classLoader =
		// Thread.currentThread().getContextClassLoader();
		// if (classLoader == null) {
		// classLoader = PropertiesLoader.class.getClassLoader();
		// }
		// InputStream inputStream =
		// classLoader.getResourceAsStream("config.properties");
		InputStream inputStream = null;
		try {
			FileSystem fs = new DefaultFileSystem();
			inputStream = fs.getInputStream(HdqsUtils.formatFilePath(hdqsHome), "config.properties");
			properties.load(inputStream);
			String serverName = getEnvValue(RegisterTable.LOCAL_SERVER_NAME);
			properties.put(RegisterTable.LOCAL_CLOCK_NAME, HdqsUtils.getHostName() + serverName);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		String useCatch = properties.getProperty(RegisterTable.USE_CATCH_JIOYRQ, "false");
		if (useCatch.equalsIgnoreCase("true")) {
			jioyrqUseCatch = true;
			refreshJioyrq();
		} else {
			jioyrqUseCatch = false;
		}
		// try {
		// String resolveLocation =
		// SystemPropertyUtils.resolvePlaceholders("classpath:log4j.xml");
		// URL url = ResourceUtils.getURL(resolveLocation);
		// DOMConfigurator.configure(url);
		// } catch (Exception e) {
		// e.printStackTrace();
		// System.exit(-1);
		// }
	}

	public void refreshJioyrq() {
		try {
			JyrqService jyrqService = (JyrqService) JNDIUtils.lookup(JyrqService.class);
			String jioyrq = jyrqService.saveOrUpdate();
			properties.setProperty(RegisterTable.CATCHED_JIOYRQ, jioyrq);
		} catch (NamingException e) {
			System.err.println("ERROR: Refresh Jioyrq " + JyrqService.class.getSimpleName() + " failure!");
			System.exit(-1);
		}
	}

	public static PropertiesLoader getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new PropertiesLoader();
				}
			}
		}
		return instance;
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	public static String[] getClusterOrder() throws HdqsServiceException {
		String clusterNodes = PropertiesLoader.getInstance().getProperty(RegisterTable.DATABASE_CLUSTER_ORDER);
		if (StringUtils.isBlank(clusterNodes)) {
			throw new HdqsServiceException("ERROR: The properties " + RegisterTable.DATABASE_CLUSTER_ORDER + " cannot be null or empty.");
		}
		String[] nnArray = clusterNodes.split(",");
		return nnArray;
	}

	public static boolean overflowMinuteLimit(long shjnch) {
		String period = PropertiesLoader.getInstance().getProperty(RegisterTable.LOCK_EXPIRED_MINUTE_PERIOD);
		boolean result = false;
		if (StringUtils.isNotBlank(period) && HdqsUtils.isNumeric(period)) {
			long duration = Long.parseLong(period);
			result = System.currentTimeMillis() - shjnch > TimeUnit.MINUTES.toMillis(duration);
		}
		return result;
	}

	public static boolean isJioyrqUseCatch() {
		return jioyrqUseCatch;
	}
}