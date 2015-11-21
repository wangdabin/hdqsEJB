package com.ceb.hdqs.service.scheduler;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;

public class RemoveExpiredLogJob implements Job {
	private static final Logger log = Logger.getLogger(RemoveExpiredLogJob.class);
	public static final String SCHEDULER_NAME = "RemoveExpiredLogJob";
	public static final String TRIGGER_NAME = "RemoveExpiredLogTrigger";

	private String dataDir = PropertiesLoader.getInstance().getProperty(RegisterTable.APP_STDOUT_LOG);
	private String filePrefix;
	private String fileRegex = ".*log.*";
	private long expiredInterval = 2;
	private String timeUnit = TimeUnit.DAYS.name();

	public void execute(JobExecutionContext context) throws JobExecutionException {
		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		log.debug(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));
		File[] fileArray;
		String sysLogExpiredInterval = ConfigLoader.getInstance().getProperty(RegisterTable.SYSLOG_EXPIRED_DAY_THRESHOLD);
		if (HdqsUtils.isNotBlank(sysLogExpiredInterval) && HdqsUtils.isNumeric(sysLogExpiredInterval)) {
			expiredInterval = Long.parseLong(sysLogExpiredInterval);
		}
		if (timeUnit.equals(TimeUnit.DAYS.name())) {
			fileArray = HdqsUtils.listBeforeFiles(dataDir, fileRegex, filePrefix, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(expiredInterval));
		} else if (timeUnit.equals(TimeUnit.HOURS.name())) {
			fileArray = HdqsUtils.listBeforeFiles(dataDir, fileRegex, filePrefix, System.currentTimeMillis() - TimeUnit.HOURS.toMillis(expiredInterval));
		} else if (timeUnit.equals(TimeUnit.MINUTES.name())) {
			fileArray = HdqsUtils.listBeforeFiles(dataDir, fileRegex, filePrefix, System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(expiredInterval));
		} else if (timeUnit.equals(TimeUnit.SECONDS.name())) {
			fileArray = HdqsUtils.listBeforeFiles(dataDir, fileRegex, filePrefix, System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(expiredInterval));
		} else if (timeUnit.equals(TimeUnit.MILLISECONDS.name())) {
			fileArray = HdqsUtils.listBeforeFiles(dataDir, fileRegex, filePrefix, System.currentTimeMillis() - expiredInterval);
		} else {
			return;
		}

		if (fileArray == null || fileArray.length == 0) {
			log.info(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
			return;
		}

		for (int i = 0, size = fileArray.length; i < size; i++) {
			File file = fileArray[i];
			if (file.length() == 0 && file.getName().endsWith(".log")) {// 有一些日志文件，大小为空的，不需要删除
				continue;
			}
			file.delete();
		}
		log.debug(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}