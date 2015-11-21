package com.ceb.hdqs.service.scheduler;

import java.util.Date;

import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.service.JgcsService;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.JNDIUtils;

public class RefreshJgcsJob implements Job {
	private static final Logger log = Logger.getLogger(RefreshJgcsJob.class);
	public static final String SCHEDULER_NAME = "RefreshJgcsJob";
	public static final String TRIGGER_NAME = "RefreshJgcsTrigger";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		JgcsService jgcsService;
		try {
			jgcsService = (JgcsService) JNDIUtils.lookup(JgcsService.class);
		} catch (NamingException e) {
			log.error(e.getMessage(), e);
			return;
		}
		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		log.info(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));
		try {
			jgcsService.refreshCache();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.info(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}
