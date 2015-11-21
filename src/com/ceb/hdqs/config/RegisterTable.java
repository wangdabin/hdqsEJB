package com.ceb.hdqs.config;

/**
 * 全局常量定义，规则为使用类名_常量名
 * 
 * @author wangjie
 * 
 */
public final class RegisterTable {
	private RegisterTable() {

	}

	public static final String HDQS_RESOURCE_PATH = "hdqs.resource.path";
	public static final String APP_STDOUT_LOG = "HDQS_LOG";
	public static final String LOAD_CONFIG_FROM_DB = "load.config.from.db";
	public static final String USE_CATCH_JIOYRQ = "use.catch.jioyrq";
	public static final String CATCHED_JIOYRQ = "catched.jioyrq";

	public static final String LOCAL_CLOCK_NAME = "ClockName";// 本机名称
	public static final String LOCAL_SERVER_NAME = "weblogic.Name";// 本机名称
	public static final String LOCK_EXPIRED_MINUTE_PERIOD = "lock.expired.minute.period";

	public static final String DATABASE_CLUSTER_ORDER = "database.cluster.order";
	public static final String CLUSTER_MANAGER_NAMENODES = "cluster.namenodes";
	public static final String MASTER_ZOOKEEPER_QUORUM = "master.zookeeper.quorum";
	public static final String STANDBY_ZOOKEEPER_QUORUM = "standby.zookeeper.quorum";

	// PREFIX不能添加后面的DOT,否则Configuration加载不到该前缀的属性
	public static final String LOG4J_CFG_PREFIX = "log4j";
	// PREFIX不能添加后面的DOT,否则Configuration加载不到该前缀的属性
	public static final String HDQS_QUERY_CFG_PREFIX = "hdqs.query";

	public static final String CRON_REMOVEEXPIREDCACHE = "cron.removeExpiredCache";
	public static final String CRON_SYNCHRONIZEDTOHBASE = "cron.synchronizedToHbase";
	// public static final String CRON_REMOVEEXPIREDLOG =
	// "cron.removeExpiredLog";
	public static final String CRON_ASYNHANDLER = "cron.asynHandler";
	public static final String CRON_REFRESH_JIOYRQ = "cron.refreshJioyrq";
	public static final String CRON_REFRESHJGCS = "cron.refreshJgcs";
	// public static final String CRON_UPDATEMAC = "cron.updateMac";
	// public static final String CRON_UPDATEJYRQ = "cron.updateJyrq";
	// public static final String CRON_UPDATEJYRQFLAG = "cron.updateJyrqFlag";

	public static final String ASY_THREAD_POOL_SIZE = "asy.thread.pool.size";
	public static final String MAC_CONFIG_FILE_PATH = "mac.config.path";
	public static final String FTP_UPLOAD_PATH = "ftp.upload.path";// FTP上传目录
	public static final String FTP_BACKUP_PATH = "ftp.bak.path";// FTP备份目录
	public static final String FTP_DOWNLOAD_PATH = "ftp.download.path";// 交易日志查询完成,异步交易文件合成路径
	public static final String GEN_FILE_PATH = "hdqs.query.output.tftp.path";// 生成查询结果文件目录
	public static final String CACHE_EXPIRED_MINUTE_THRESHOLD = "cache.expired.minute.threshold";// 阀值
	public static final String SYSLOG_EXPIRED_DAY_THRESHOLD = "syslog.expired.day.threshold";// 阀值
	public static final String DB_SYNCHRONIZE_HOURS_BEFORE_THRESHOLD = "db.synchronize.hours.before.threshold";// 阀值
	public static final String DB_SYNCHRONIZE_TO_CLUSTER_TIME_SPAN = "db.synchronize.to.cluster.time.span";

	public static final String QUERY_FILE_KB_THRESHOLD = "query.file.kb.threshold";
	public static final String WORKING_TIME_SPAN = "working.time.span";
	public static final String FREE_TIME_SPAN = "free.time.span";

	public static final String QUERY_END_TIME_LIMIT_SWITCH = "query.end.time.limit.switch";
	public static final String QUERY_START_TIME_LIMIT_SWITCH = "query.start.time.limit.switch";
	public static final String QUERY_END_TIME_LIMIT = "query.end.time.limit";
	public static final String QUERY_START_TIME_LIMIT = "query.start.time.limit";
	public static final String QUERY_DAY_SPAN = "query.day.span";

	public static final String QUERY_0775_RECORD_COUNT_THRESHOLD = "query.0775.record.count.threshold";// 0775交易查询记录阀值
	public static final String ASY_HANDLE_BATCH_SIZE = "asy.handle.batch.size";// 异步处理批量大小
	public static final String SYN_HANDLE_BATCH_SIZE = "syn.handle.batch.size";// 异步处理批量大小
	public static final String ASY_HANDLE_FILE_CHARSET = "asy.handle.file.charset";// 异步处理文件编码

	/**
	 * 同步转异步阀值
	 */
	public static final String HDQS_QUERY_SYNCHRONIZE_SWITCH_THRESHOLD = "hdqs.query.synchronize.switch.threshold";
	// #0不限制， 1文件个数，2文件总大小
	public static final String HDQS_QUERY_HANDLE_FILE_THRESHOLD_CTRL = "hdqs.query.handle.file.threshold.ctrl";
	public static final String HDQS_QUERY_HANDLE_FILE_SIZE_THRESHOLD = "hdqs.query.handle.file.size.threshold";
	// #B KB MB
	public static final String HDQS_QUERY_HANDLE_FILE_SIZE_UNIT = "hdqs.query.handle.file.size.unit";
	public static final String HDQS_QUERY_HANDLE_FILE_COUNT_THRESHOLD = "hdqs.query.handle.file.count.threshold";

	/**
	 * 打印文本文件每页显示行数
	 */
	public static final String HDQS_QUERY_PRINT_TXT_LINE_PER_PAGE = "hdqs.query.print.txt.line.per.page";
	/**
	 * 导出及异步生成PDF文件每页显示行数
	 */
	public static final String HDQS_QUERY_HANDLE_LINE_PER_PAGE = "hdqs.query.handle.line.per.page";
	/**
	 * 生成文件包含页数
	 */
	public static final String HDQS_QUERY_HANDLE_PAGE_PER_FILE = "hdqs.query.handle.page.per.file";

	// #0无 1文字
	public static final String HDQS_QUERY_WATER_MARK_CTRL = "hdqs.query.water.mark.ctrl";
	public static final String HDQS_QUERY_WATER_MARK_CONTENT = "hdqs.query.water.mark.content";
}