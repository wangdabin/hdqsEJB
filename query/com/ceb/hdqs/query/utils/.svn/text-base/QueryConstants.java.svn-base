package com.ceb.hdqs.query.utils;

import org.apache.hadoop.hbase.util.Bytes;

public class QueryConstants {
	/*------------------------------ CONFIGURATION     CONSTANTS --------------------------------------------*/

	/**
	 * 字段分隔符 propertity
	 */
	public static final String FILED_SPLITTER = "com.sky.cebank.field.split";
	/**
	 * rowkey分隔符 propertity
	 */
	public static final String ROWKEY_SPLITTER = "com.sky.cebank.rowkey.split";
	/**
	 * 同步转异步阀值
	 */
	public static final String SYNCHRONIZE_SWITCH_THRESHOLD = "hdqs.query.synchronize.switch.threshold";
	/**
	 * 同一受理编码生成文件控制0不限制，1文件个数，2文件总大小
	 */
	public static final String HANDLE_FILE_THRESHOLD_CTRL = "hdqs.query.handle.file.threshold.ctrl";
	/**
	 * 文件总大小阀值
	 */
	public static final String HANDLE_FILE_SIZE_THRESHOLD = "hdqs.query.handle.file.size.threshold";
	/**
	 * 文件总大小单位B KB MB
	 */
	public static final String HANDLE_FILE_SIZE_UNIT = "hdqs.query.handle.file.size.unit";
	/**
	 * 文件个数阀值
	 */
	public static final String HANDLE_FILE_COUNT_THRESHOLD = "hdqs.query.handle.file.count.threshold";
	/**
	 * 生成文件每页包含行数
	 */
	public static final String HANDLE_LINE_PER_PAGE = "hdqs.query.handle.line.per.page";

	/**
	 * txt每页条数
	 */
	public static final String PRINT_TXT_LINE_PER_PAGE = "hdqs.query.print.txt.line.per.page";
	/**
	 * 生成文件包含页数
	 */
	public static final String HANDLE_PAGE_PER_FILE = "hdqs.query.handle.page.per.file";
	/**
	 * 水印控制,0无 1文字 2图片
	 */
	public static final String WATER_MARK_CTRL = "hdqs.query.water.mark.ctrl";
	/**
	 * 水印内容
	 */
	public static final String WATER_MARK_CONTENT = "hdqs.query.water.mark.content";

	/**
	 * HBase查询过程中的最大字符和最小字符
	 */
	/**
	 * ~
	 */
	public static final String MAX_CHAR = "~";
	/**
	 * #
	 */
	public static final String MIN_NUM = "#";

	/**
	 * 客户查询
	 */
	public static final String QUERY_TYPE_CUSTOMER = "0";

	/**
	 * 内部查询
	 */
	public static final String QUERY_TYPE_BANK = "1";

	/**
	 * 监管查询
	 */
	public static final String QUERY_TYPE_MONITOR = "2";

	/**
	 * 查询文档信息范围的标志 查询全部文档
	 */

	// public static final String DOCUMENT_QUERY_PDF_ALL = "all";
	/**
	 * KEMUCC S-对私活期 V-贷款 Q-欠息 I-内部帐 O-表外帐 T-拆借贴现投资帐 Y-存放同业主文件
	 * 
	 */
	public static final String KEMUCC_DSHQ = "S";
	/**
	 * F-对私定期
	 */
	public static final String KEMUCC_DSDQ = "F";
	/**
	 * C-对公活期
	 */
	public static final String KEMUCC_DGHQ = "C";
	/**
	 * E-对公定期 S-对私活期 C-对公活期 F-对私定期 E-对公定期
	 */
	public static final String KEMUCC_DGDQ = "E";

	/**
	 * 账号性质 0活期,2定期,3全部
	 */
	public static final String ZHAOHXZ_HUOQI = "0";
	/**
	 * 账号性质 0活期,2定期,3全部
	 */
	public static final String ZHAOHXZ_DINGQI = "2";
	/**
	 * 账号性质 0活期,2定期,3全部
	 */
	public static final String ZHAOHXZ_ALL = "3";

	/**
	 * B集群的配置文件名称201
	 */
	public static String CUSTOMER_CONFIGURATION_FILE = "cebank-history_";

	public static final String HBASE_TABLE_FAMILY = "F";
	public static final String HBASE_TABLE_FAMILY_I = "I";
	public static final byte[] HBASE_TABLE_FAMILY_BYTE = Bytes.toBytes("F");
	public static final byte[] HBASE_TABLE_FAMILY_COUNTER = Bytes.toBytes("I");

	/**
	 * 记录一个账号查询完成
	 */
	public static final int ZHANGH_QUERY_FINISHED = 1;
	/**
	 * 记录一个账号没有查询完成的情况
	 */
	public static final int ZHANGH_QUERY_NO_FINISHED = 0;

	// hbase 表
	// ==========start==============================================================================================================
	/**
	 * AGDFH 对公定期主文件表
	 */
	public static final String TABLE_NAME_AGDFH = "AGDFH";
	/**
	 * AGHFH 对公活期主文件表
	 */
	public static final String TABLE_NAME_AGHFH = "AGHFH";
	/**
	 * AGDMX 对公定期明细表
	 */
	public static final String TABLE_NAME_AGDMX = "AGDMX";
	/**
	 * AKHZH 客户帐号系统帐号对照表
	 */
	public static final String TABLE_NAME_AKHZH = "AKHZH";
	/**
	 * ASDMX 对私定期明细表
	 */
	public static final String TABLE_NAME_ASDMX = "ASDMX";
	/**
	 * AGHMX 对公活期明细表
	 */
	public static final String TABLE_NAME_AGHMX = "AGHMX";
	/**
	 * VYKTD 一卡通表
	 */
	public static final String TABLE_NAME_VYKTD = "VYKTD";
	/**
	 * 账号解析表
	 */
	public static final String TABLE_NAME_AZHJX = "AZHJX";

	/**
	 * 账号解析03
	 */
//	public static final String TABLE_NAME_AZHJX_03 = "AZHJX03";
	/**
	 * 0772查询输入为客户账号的类型是存单、国债、储蓄存款存折时用的解析表
	 */
	public static final String TABLE_NAME_AZHJX0772 = "AZHJX0772";

	/**
	 * 客户账号0773，账号为RowKey的第一个字段
	 */
	public static final String TABLE_NAME_AKHZH_0773 = "AKHZH0773";
	/**
	 * 对私交易明细表
	 */
	public static final String TABLE_NAME_ASHMX = "ASHMX";
	/**
	 * 换卡登记表
	 */
	public static final String TABLE_NAME_BHKDJ = "BHKDJ";
	/**
	 * 对私客户表
	 */
	public static String TABLE_NAME_BDSKH = "BDSKH";
	/**
	 * 对公客户表
	 */
	public static String TABLE_NAME_BDGKH = "BDGKH";

	/**
	 * 同业客户表
	 */
	public static String TABLE_NAME_BTYKH = "BTYKH";
	/**
	 * 活期透支
	 */
	public static final String TABLE_NAME_BHQTZ = "BHQTZ";

	/**
	 * 对私客户证件表
	 */
	public static final String TABLE_NAME_BKHZJ = "BKHZJ";
	/**
	 * 法透余额
	 */
	public static final String TABLE_NAME_BFTYE = "BFTYE";

	/**
	 * 法透账户信息
	 */
	public static final String TABLE_NAME_AGHMX_FT = "AGHMX_FT";

	public static final String HBASE_TABLE_NAME_ASYNQUERY = "asynrecord";

	/**
	 * 交易明细纪录表
	 */
	public static final String HBASE_TABLE_NAME_EXCHANGE_RECORD = "exchangerecord";
	/**
	 * 柜员索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_GY = "gyIndexRecord";
	/**
	 * 机构索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_JG = "jgIndexRecord";
	/**
	 * 交易码索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_CODE = "codeIndexRecord";
	/**
	 * 柜员机构索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_GYJG = "gyjgIndexRecord";
	/**
	 * 柜员交易码索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_GYCODE = "gyCodeIndexRecord";
	/**
	 * 机构交易码索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_JGCODE = "jgCodeIndexRecord";
	/**
	 * 受理编码索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_HANDLENO = "handleIndexRecord";
	/**
	 * 柜员机构交易码索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_GYJGCODE = "gyjgcodeIndexRecord";
	/**
	 * 日期索引表
	 */
	public static final String HBASE_TABLE_NAME_INDEX_DATE = "dateIndexRecord";

	// hbase 表
	// ==========end================================================================================================================

	/**
	 * 对公历史记录查询代号
	 */
	public static final String CORPORATE_QUERY_CODE = "0770";

	/**
	 * 对私活期查询代号
	 */
	public static final String PRIVATE_LIQUID_QUERY_CODE = "0771";
	/**
	 * 对私定期查询代号
	 */
	public static final String PRIVATE_FIX_QUERY_CODE = "0772";

	public static final String CARD_REPLACEMENT_CODE = "0773";
	/**
	 * 历史数据批量结果查询(pdf)0776
	 */
	public static final String ASYNCHRONIZE_QUERY_GEETER_CODE = "0776";
	/**
	 * 对公账户历史查询（批量）0777
	 */
	public static final String CORPORATE_BATCH_QUERY_CODE = "0777";
	/**
	 * 对私账户历史查询（批量）0778
	 */
	public static final String PRIVATE_BATCH_QUERY_CODE = "0778";
	/**
	 * 历史数据批量结果查询（总行,pdf,excel）0779
	 */
	public static final String ASYNCHRONIZE_QUERY_GEETER_CODE_EXCEL = "0779";
	/**
	 * 单位卡历史查询（批量）0781
	 */
	public static final String CORPORATE_CARD_QUERY_CODE = "0781";

	/**
	 * 国债
	 */
	public static final String _0772_KHZHLX_GUOZHAI = "6";
	/**
	 * 存单
	 */
	public static final String _0772_KHZHLX_CUNDAN = "5";
	/**
	 * 储蓄存款存折
	 */
	public static final String _0772_KHZHLX_CXCKCZ = "4";

	/**
	 * 错误日志存放记录
	 */

	public static final int PRIVATE_AUTHORITY_KHZHJB_14 = 14;
	public static final int PRIVATE_AUTHORITY_KHZHJB_15 = 15;
	public static final int PRIVATE_AUTHORITY_KHZHJB_16 = 16;

	/**
	 * 授权业务配置文件值
	 * 
	 * @author user
	 * 
	 */
	public static class AuthorizeCode {
		/**
		 * 异步查询，提交文件查询条件 0777,0778 如果是输入文件查询
		 */
		public static final String asyfilequeryauthorize_code = "EAU0000005";
		/**
		 * 跨机构账户明细查询 查询出的YNGYJG│营业机构号！=查的营业机构
		 */
		public static final String yngyjgauthorize_code = "EAU0000001";
		/**
		 * 非客户对公活期查询，账户的业务代码是2608 0770,0777 如果是非客户查询，查询aghfh时查询出YEWUDH│业务代号
		 * =2608
		 */
		public static final String yewdhauthorize_code = "EAU0000002";
		/**
		 * 非提交查询条件本人，获取查询结果 0776 0779 如果查询柜员不是输入查询条件的柜员
		 */
		public static final String getresultauthorize_code = "EAU0000004";
		/**
		 * 非客户查询，查询光大员工账户信息 0771,0772,0773,0778 如果是非客户查询，查BDSKH表 查出 SHFOBZ=1
		 */
		public static final String employeeauthorize_code = "EAU0000003";

		/**
		 * 客户评估级别
		 */
		public static final String khzhjbauthorize_code = "EAU0000006";
	}

	public static final String ERROR_MSG_SUFFIX = ",请联系总行查证！";
	// **HUOBDH 货币代号
	public static final String HUOBDH_ALL = "00";// 全部
	public static final String HUOBDH_RMB = "01";// RMB 01 人民币
	public static final String HUOBDH_GBP = "12";// GBP 12 英镑
	public static final String HUOBDH_HKD = "13";// HKD 13 港币
	public static final String HUOBDH_USD = "14";// USD 14 美元
	public static final String HUOBDH_CHF = "15";// CHF 15 瑞士法郎
	public static final String HUOBDH_SEK = "21";// SEK 21 瑞典克郎
	public static final String HUOBDH_DKK = "22";// DKK 22 丹麦克郎
	public static final String HUOBDH_NOK = "23";// NOK 23 挪威克郎
	public static final String HUOBDH_JPY = "27";// JPY 27 日元
	public static final String HUOBDH_CAD = "28";// CAD 28 加拿大元
	public static final String HUOBDH_AUD = "29";// AUD 29 澳大利亚元
	public static final String HUOBDH_SGD = "32";// SGD 32 新加坡元
	public static final String HUOBDH_EUR = "38";// EUR 38 欧元
	public static final String HUOBDH_MOP = "81";// MOP 81 澳门元
	public static final String HUOBDH_THB = "84";// THB 84 泰国铢

	// **CHUIBZ 钞汇标志
	public static final String CHUIBZ_CHAO = "0";// 0-钞户
	public static final String CHUIBZ_HUI = "1";// 1-汇户
	public static final String CHUIBZ_ALL = "2";

	/**
	 * <PRE>
	 * JILUZT        记录状态
	 * 0-正常
	 * 1-销户
	 * 2-只收不付冻结
	 * 3-封闭冻结
	 * 4-删除
	 * 5-未使用
	 * 6-结清
	 * 7-打印
	 * 8-碰库
	 * 9-不动户
	 * A-不动户转收益
	 * B-死亡户
	 * C-报案户
	 * D-请与开户行接洽
	 * E-不能在他行销记户
	 * F-准客户
	 * G-未复核
	 * R-被当日冲正
	 * S-被隔日冲正
	 * J-禁用
	 * Y-预销户
	 * Z-质押冻结
	 * T-凭证在途
	 * </PRE>
	 */
	public static final String JILUZT_NORMAL = "0";
}