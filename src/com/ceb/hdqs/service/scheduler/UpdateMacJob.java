package com.ceb.hdqs.service.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.wtc.MACUtils;

public class UpdateMacJob implements Job {
	private static final Logger log = Logger.getLogger(UpdateMacJob.class);
	public static final String SCHEDULER_NAME = "UpdateMacJob";
	public static final String TRIGGER_NAME = "UpdateMacTrigger";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		log.info(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));
		log.info(" Update mac: " + MACUtils.updateMAC());
		log.info(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}