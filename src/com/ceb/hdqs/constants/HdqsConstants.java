package com.ceb.hdqs.constants;

public final class HdqsConstants {
	private HdqsConstants() {

	}

	public static final String UTF8_ENCODING = "UTF-8";
	public static final String SPLIT_CHAR = "|";

	public static final String FILE_KIND_PDF = "1";// PDF
	public static final String FILE_KIND_XLS = "2";// excel
	public static final String FILE_KIND_BOTH = "3";// 两者都有

	public static final String SHFOBZ_NONE = "0";
	public static final String SHFOBZ_PRINT = "1";// 打印,生成txt文件
	public static final String SHFOBZ_DOWNLOAD = "2";// 下载,生成pdf文件

	public static final String CHAXZL_ZHANGH = "1";// 账号
	public static final String CHAXZL_KEHUZH = "2";// 客户账号
	public static final String CHAXZL_KEHUHAO = "3";// 客户号
	public static final String CHAXZL_ZHJNZL = "4";// 证件种类

	// 客户帐号类型1-卡 2-活期一本通3-定期一本通 4-定期存折5-存单6-国债
	public static final String KHZHLX_CARD = "1";
	public static final String KHZHLX_HUOQIYIBENTONG = "2";
	public static final String KHZHLX_DINGQIYIBENTONG = "3";
	public static final String KHZHLX_DINGQICUNZHE = "4";
	public static final String KHZHLX_CUNDAN = "5";
	public static final String KHZHLX_GUOZHAI = "6";
	public static final String KHZHLX_DUIGONGYIHAOTONG = "B";
	// 打印格式 0-客户对账单,1-非客户对账单
	public static final String PRINT_STYLE_KEHU = "0";
	public static final String PRINT_STYLE_UNKEHU = "1";

	public static final String SYN_STATUS_INIT = "0";
	public static final String SYN_STATUS_RUNNING = "1";
	public static final String SYN_STATUS_SUCCESS = "2";

	public static final String RUNNING_STATUS_FAILURE = "-1";// 查询交易失败
	public static final String RUNNING_STATUS_WAITING = "0";// 查询交易等待
	public static final String RUNNING_STATUS_RUNNING = "1";// 查询交易正在进行
	public static final String RUNNING_STATUS_SUCCESS = "2";// 查询交易成功
	public static final String RUNNING_STATUS_UNSEQUENCE = "3";// 查询交易成功,但数据不连续

	public static final String NOTIFY_STATUS_WAITING = "0";// 等待通知
	public static final String NOTIFY_STATUS_SENT = "1";// 通知成功

	public static final String STATUS_NORMAL = "0";
	public static final String STATUS_UNUSE = "4";
}