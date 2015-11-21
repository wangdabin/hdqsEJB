package com.ceb.hdqs.query.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.hdqs.action.query.parser.BdgkhParser;
import com.ceb.hdqs.action.query.parser.BdskhParser;
import com.ceb.hdqs.action.query.parser.BtykhParser;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.entity.JgcsEO;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.query.entity.BalanceVerifyEntity;
import com.ceb.hdqs.query.entity.CustomerInfo;
import com.ceb.hdqs.query.entity.EIdentityType;
import com.ceb.hdqs.query.entity.EnumChuibz;
import com.ceb.hdqs.query.entity.EnumHuobdh;
import com.ceb.hdqs.query.entity.EnumJiulzt;
import com.ceb.hdqs.query.entity.EnumKhzhlx;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.service.AuthorizeService;
import com.ceb.hdqs.service.JgcsService;
import com.ceb.hdqs.utils.DateFormatUtils;
import com.ceb.hdqs.utils.JNDIUtils;

public class QueryMethodUtils {
	private static final Log log = LogFactory.getLog(QueryMethodUtils.class);
	private static AuthorizeService authorizeService;
	private static JgcsService jgcsService;
	private static final String CHUIBZ = "CHUIBZ_";
	private static final String HUOBIDH = "HUOBIDH_";
	private static final String JILUZT = "JILUZT_";

	/**
	 * Make a string representation of the exception.
	 * 
	 * @param e
	 *            The exception to stringify
	 * @return A string with exception name and call stack.
	 */
	public static String stringifyException(Exception e) {
		return org.apache.hadoop.util.StringUtils.stringifyException(e);
	}

	/**
	 * 记录状态格式
	 * 
	 * @param jlzt
	 * @return
	 */
	public static String jlztFormat(String jlzt) {
		if (jlzt == null || StringUtils.isBlank(jlzt)) {
			return "";
		}
		return EnumJiulzt.valueOf(JILUZT + jlzt).getDisplay();
	}

	/**
	 * 格式化钞汇标志
	 * 
	 * @param chuibz
	 * @return
	 */
	public static String chuibzFormat(String chuibz) {
		if (chuibz == null || StringUtils.isBlank(chuibz)) {
			return "";
		}
		return EnumChuibz.valueOf(CHUIBZ + chuibz).getDisplay();

	}

	/**
	 * 格式化货币代号
	 * 
	 * @param huobdh
	 * @return
	 */
	public static String huobdhFormat(String huobdh) {
		if (huobdh == null || StringUtils.isBlank(huobdh)) {
			return "";
		}

		return EnumHuobdh.valueOf(HUOBIDH + huobdh).getDisplay();
	}

	/**
	 * 格式户借贷标志 0-借/收 1-贷/付
	 * 
	 * @param jdbz
	 * @return
	 */
	public static String jiedbzFormat(String jdbz) {
		if (jdbz == null || StringUtils.isBlank(jdbz)) {
			return "";
		}
		if (jdbz.equals("0")) {
			return "0-借/收";
		} else if (jdbz.equals("1")) {
			return "1-贷/付";
		} else {
			return jdbz;
		}
	}

	/**
	 * 卡种类 0-借记卡 1-贷记卡 2-准贷记卡 3-借贷合一卡
	 * 
	 * @param jdjkbz
	 * @return
	 */
	public static String jdjkbzFormat(String jdjkbz) {
		if (jdjkbz == null) {
			return "";
		}
		if (jdjkbz.equals("0")) {
			return "0-借记卡";
		} else if (jdjkbz.equals("1")) {
			return "1-贷记卡";
		} else if (jdjkbz.equals("2")) {
			return "2-准贷记卡";
		} else if (jdjkbz.equals("3")) {
			return "3-借贷合一卡";
		} else {
			return jdjkbz;
		}
	}

	/**
	 * 卡种类 0-普通卡 1-联名卡 2-认同卡 3-储值卡
	 * 
	 * @return
	 */
	public static String kaazlFormat(String kaazl) {
		if (kaazl == null) {
			return "";
		}
		if (kaazl.equals("0")) {
			return "0-普通卡";
		} else if (kaazl.equals("1")) {
			return "1-联名卡";
		} else if (kaazl.equals("2")) {
			return " 2-认同卡";
		} else if (kaazl.equals("3")) {
			return "3-储值卡";
		} else {
			return kaazl;
		}
	}

	// 0-对公帐号
	// 1-卡
	// 2-活期一本通
	// 3-定期一本通
	// 4-定期存折
	// 5-存单
	// 6-国债
	// 7-外系统帐号
	// 8-活期存折
	// 9-内部帐/表外帐
	// S-对私内部帐号
	// Z-客户号
	// A-所有客户账号类型
	// B-对公一号通
	public static String khzhlxFormat(String khzhlx) {
		if (khzhlx == null) {
			return "";
		}
		if (khzhlx.equals("0")) {
			return "0-对公帐号";
		} else if (khzhlx.equals("1")) {
			return "1-卡";
		} else if (khzhlx.equals("2")) {
			return "2-活期一本通";
		} else if (khzhlx.equals("3")) {
			return "3-定期一本通";
		} else if (khzhlx.equals("4")) {
			return "4-定期存折";
		} else if (khzhlx.equals("5")) {
			return "5-存单";
		} else if (khzhlx.equals("6")) {
			return "6-国债";
		} else if (khzhlx.equals("7")) {
			return "7-外系统帐号";
		} else if (khzhlx.equals("8")) {
			return "8-活期存折";
		} else if (khzhlx.equals("9")) {
			return "9-内部帐/表外帐";
		} else if (khzhlx.equals("S")) {
			return "S-对私内部帐号";
		} else if (khzhlx.equals("Z")) {
			return "Z-客户号";
		} else if (khzhlx.equals("A")) {
			return "A-所有客户账号类型";
		} else if (khzhlx.equals("B")) {
			return "B-对公一号通";
		} else {
			return khzhlx;
		}
	}

	/**
	 * 格式化数值
	 * 
	 * @param str
	 *            输入的金额
	 * @param huobdh
	 *            货币代号
	 * @return
	 */
	public static String decimalFormat(String str, String huobdh) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		NumberFormat formater = null;
		double value = Double.parseDouble(str);
		if (StringUtils.isBlank(huobdh) || huobdh.equals(QueryConstants.HUOBDH_JPY)) {
			formater = new DecimalFormat("###,###");
		} else {
			formater = new DecimalFormat("###,###.00");
		}
		String result = formater.format(value);
		if (result.startsWith(".")) {
			result = "0" + result;
		} else if (result.startsWith("-.")) {
			result = "-0." + result.substring(2);
		}
		return result;
	}

	public static String decimalFormat(String str, int len) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		NumberFormat formater = null;
		double value = Double.parseDouble(str);
		if (len == 0) {
			formater = new DecimalFormat("###,###");
		} else {
			StringBuffer buff = new StringBuffer("###,###.");
			for (int i = 0; i < len; i++) {
				buff.append("0");
			}
			formater = new DecimalFormat(buff.toString());
		}
		String result = formater.format(value);
		if (result.startsWith(".")) {
			result = "0" + result;
		} else if (result.startsWith("-.")) {
			result = "-0." + result.substring(2);
		}
		return result;
	}

	public static String formatDate(long date) {
		String pattern = "yyyyMMdd";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * 时间格式化工具：HH-mm-ss
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTime(long date) {
		String pattern = "HH-mm-ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String formatJIO1SJ(String time) {
		StringBuilder timePrefix = new StringBuilder("0");
		if (time.length() == 5) {
			return timePrefix.append(time).toString();
		}
		return time;
	}

	/**
	 * 获取用户中文名
	 * 
	 * @param KEHHAO
	 *            根据客户号，去查询对应的客户信息表，获取中文名称。 客户号 10位 ABBBBBBBBC A客户种类的分类号 0 –金融公司
	 *            1 –对私客户 2- 对公客户 B顺序号 C 校验码。
	 * 
	 * @return
	 * @throws IOException
	 */
	public static CustomerInfo getCustomerChineseName(String kehhao) throws IOException {
		char khhType = kehhao.charAt(0);
		CustomerInfo info = new CustomerInfo();
		switch (khhType) {
		case '0':
			BtykhParser tyParser = new BtykhParser();
			info = tyParser.getCustomerInfo(kehhao);
			break;
		case '1':
			BdskhParser dsParser = new BdskhParser();
			info = dsParser.getCustomerInfo(kehhao);
			break;
		case '2':
			BdgkhParser dgParser = new BdgkhParser();
			info = dgParser.getCustomerInfo(kehhao);
			break;
		default:
			info.setKehhao(kehhao);
			info.setKehzwm("");
			break;
		}

		return info;
	}

	public static String formatString(int len, String str) {
		return String.format("%-" + len + "s", str);
	}

	/**
	 * 将时间20100101 转换成2010年01月01日
	 * 
	 * @param date
	 * @return
	 */
	public static String reFormatDate(String date) {
		try {
			Date parseDate = DateFormatUtils.parseDate(date);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.format(parseDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 日期时间格式化工具：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param currentTimeMillis
	 * @return
	 */
	public static String formatDateAndTimeWithSplitter(long currentTimeMillis) {
		String pattern = "yyyy-MM-dd  HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(currentTimeMillis);
	}

	/**
	 * 日期时间格式化工具：yyyyMMdd HH:mm:ss
	 * 
	 * @param currentTimeMillis
	 * @return
	 */
	public static String formatDateAndTime(long currentTimeMillis) {
		String interval = " ";
		return formatDate(currentTimeMillis) + interval + formateTime(currentTimeMillis);
	}

	public static String formateTime(long currentTimeMillis) {
		String pattern = "HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(currentTimeMillis);
	}

	/**
	 * 根据查询业务中，解析出的授权规则进行查询对应的授权级别和授权原因
	 * 
	 * @param authLevel
	 * @return
	 */
	public static SqbmEO getAuthorityInfo(String szkmbm, String qudaoo) {
		SqbmEO sqbmEO = null;

		if (authorizeService == null) {
			try {
				authorizeService = (AuthorizeService) JNDIUtils.lookup(AuthorizeService.class);
			} catch (NamingException e) {
				log.error(e.getMessage(), e);
				return null;
			}
		}

		sqbmEO = authorizeService.findByAuthorizedCode(szkmbm, qudaoo);
		if (sqbmEO != null) {
			log.info("权限验证，获取的sqbmEO是：柜员级别：" + sqbmEO.getGuiyjb() + " 备注信息：" + sqbmEO.getBeizxx());
		} else {
			log.info("权限验证，获取的sqbmEO是空");
		}

		return sqbmEO;
	}

	/**
	 * 获取距离今天mount天的日期
	 * 
	 * @return
	 */
	public static Date addDays(int mount) {
		Date date = DateUtils.addDays(new Date(), mount);
		return date;
	}

	/**
	 * 获取机构名称
	 * 
	 * @param jigocs
	 * @return
	 */
	public static String getJGMC(String jigocs) {
		log.debug("需要查询的jigocs是：" + jigocs);
		if (jgcsService == null) {
			try {
				jgcsService = (JgcsService) JNDIUtils.lookup(JgcsService.class);
			} catch (NamingException e) {
				log.error(e.getMessage(), e);
			}
		}

		JgcsEO eo = jgcsService.findByYngyjg(jigocs);
		if (eo != null) {
			return eo.getJigomc();
		}
		return "";
	}

	/**
	 * 根据客户中文名过滤查询条件
	 * 
	 * @param oldName
	 *            图形前段回显成功后，带入的中文名称
	 * @param newName
	 *            hdqs根据 输入条件解析出的中文名称
	 * @return
	 */
	public static boolean checkKehuzwm(String oldName, String newName) {
		if (newName == null) {
			return false;
		}
		String[] newkhzwmArray = newName.trim().split("");
		String[] oldkhzwmArray = oldName.trim().split("");
		boolean flag = true;
		if (newkhzwmArray.length == oldkhzwmArray.length)
			for (int i = 0; i < newkhzwmArray.length; i++) {
				if (newkhzwmArray[i].trim().equals(oldkhzwmArray[i].trim())) {
					continue;
				} else {
					flag = false;
					break;
				}
			}
		else
			flag = false;
		return flag;
	}

	public static int isNull(String value) {
		if (StringUtils.isBlank(value)) {
			return 1;
		}
		return 0;
	}

	/**
	 * 根据输入条件获取条件类型和条件的值
	 * 
	 * @param record
	 * @return
	 */
	public static BalanceVerifyEntity getRecordPair(PybjyEO record) {
		BalanceVerifyEntity entity = new BalanceVerifyEntity();
		String chaxlx = record.getChaxzl();
		try {
			EnumQueryKind queryKind = EnumQueryKind.valueOf(EnumQueryKind.QK + chaxlx);
			String kindStr = queryKind.getDisplay().getValue();
			entity.setQueryKind(kindStr);
			entity.setQueryKindKey(queryKind.getDisplay().getKey().intValue() + "");
			switch (queryKind) {
			case QK1:
				
				entity.setValue(record.getZhangh());
				break;
			case QK2:
				EnumKhzhlx khzhlx = EnumKhzhlx.valueOf(EnumKhzhlx.LX + record.getKhzhlx());
				entity.setValue(record.getKehuzh());
				entity.setQueryKind(khzhlx.getDisplay().getValue());
				break;
			case QK3:
				entity.setValue(record.getKehuhao());
				break;
			case QK4:
				entity.setValue(record.getZhjhao());
				String zjTpye = record.getZhjnzl();
				EIdentityType idType = EIdentityType.valueOf(EIdentityType.ID + zjTpye);
				kindStr = idType.getDisplay();
				entity.setQueryKind(kindStr);

				break;
			default:
				break;
			}
		} catch (java.lang.IllegalArgumentException e) {
			log.warn(e.getMessage(), e);
			entity.setValue("");
			entity.setQueryKindKey("");
			entity.setQueryKind("");
		}

		return entity;
	}

	public static String generateOutputDir(PybjyEO record) {
		StringBuilder outputDir = new StringBuilder();
		String outputTemp = null;
		if (System.getProperty("os.name").startsWith("Windows")) {
			outputTemp = QueryConfUtils.getActiveConfig().get(RegisterTable.GEN_FILE_PATH, "D:\\data\\ftp\\");
		} else {
			outputTemp = QueryConfUtils.getActiveConfig().get(RegisterTable.GEN_FILE_PATH, "/data/ftp/");
			if (!outputTemp.endsWith("/")) {
				outputTemp = outputTemp + "/";
			}
		}
		outputDir.append(outputTemp).append(record.getJio1gy()).append(File.separator).append(record.getJioyrq()).append(File.separator).append(record.getSlbhao());
		return outputDir.toString();
	}

	public static void main(String[] args) {
		// System.out.println(decimalFormat("0.039", 2));
		// System.err.println(formatDate(1383727306013L));

		EIdentityType.valueOf("11").getDisplay();

	}
}