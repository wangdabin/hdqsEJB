package com.ceb.hdqs.service.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.action.asyn.AsynQueryHandler;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.service.ClokService;
import com.ceb.hdqs.service.YbjyService;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.JNDIUtils;

public class AsynHandlerJob implements Job {
	private static final Logger log = Logger.getLogger(AsynHandlerJob.class);
	public static final String SCHEDULER_NAME = "AsynHandlerJob";
	public static final String TRIGGER_NAME = "AsynHandlerTrigger";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		YbjyService ybjyService;
		ClokService clokService;
		try {
			ybjyService = (YbjyService) JNDIUtils.lookup(YbjyService.class);
			clokService = (ClokService) JNDIUtils.lookup(ClokService.class);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			return;
		}
		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		AsynQueryHandler handler = AsyHandlerHolder.getInstance().getHandler();
		if (handler == null || !handler.isFinish()) {
			return;
		}
		// acquire lock
		boolean lock = clokService.acquireLock(RegisterTable.CRON_ASYNHANDLER);
		if (!lock) {
			return;
		}
		log.debug(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));
		try {
			ybjyService.resetRunStatus();
			Map<String, List<PybjyEO>> map = ybjyService.queryUnhandleRecordsBySlbhao();
			if (map != null && map.size() > 0) {
				handler.doAsynQuery(map);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			clokService.releaseLock(RegisterTable.CRON_ASYNHANDLER);
		}
		log.debug(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}