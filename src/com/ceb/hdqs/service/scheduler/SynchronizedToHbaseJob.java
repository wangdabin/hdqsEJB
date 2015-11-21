package com.ceb.hdqs.service.scheduler;

import java.util.Calendar;
import java.util.Date;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.service.ClokService;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.JyjlService;
import com.ceb.hdqs.service.YbjyService;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;

public class SynchronizedToHbaseJob implements Job {
	private static final Logger log = Logger.getLogger(SynchronizedToHbaseJob.class);
	public static final String SCHEDULER_NAME = "SynchronizedToHbaseJob";
	public static final String TRIGGER_NAME = "SynchronizedToHbaseTrigger";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 业务时间不进行同步处理
		if (!HdqsUtils.isSynchronizeToClusterTime(Calendar.getInstance())) {
			return;
		}
		YbjyService ybjyService;
		JyjlService jyjlService;
		ClokService clokService;
		try {
			ybjyService = (YbjyService) JNDIUtils.lookup(YbjyService.class);
			jyjlService = (JyjlService) JNDIUtils.lookup(JyjlService.class);
			clokService = (ClokService) JNDIUtils.lookup(ClokService.class);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			return;
		}
		// acquire lock
		boolean lock = clokService.acquireLock(RegisterTable.CRON_SYNCHRONIZEDTOHBASE);
		if (!lock) {
			return;
		}

		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		log.debug(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));
		try {
			if (ybjyService.isFinish()) {
				ybjyService.resetMasterSynStatus();
				ybjyService.resetStandbySynStatus();
				ybjyService.synchronizeRecords();
				// 同步成功后不进行物理删除，等待分区表过期删除
				// ybjyService.rmSynchronizedRecords();
			}

			if (jyjlService.isFinish()) {
				jyjlService.resetMasterSynStatus();
				jyjlService.resetStandbySynStatus();
				jyjlService.synchronizeRecords();
				// 同步成功后不进行物理删除，等待分区表过期删除
				// jyjlService.rmSynchronizedRecords();
			}
		} catch (HdqsServiceException e) {
			log.error(e.getMessage(), e);
		} finally {
			clokService.releaseLock(RegisterTable.CRON_SYNCHRONIZEDTOHBASE);
		}
		log.debug(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}