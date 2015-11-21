package com.ceb.hdqs.service.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.utils.DateTimeFormatUtils;

public class RefreshJioyrqJob implements Job {
	private static final Logger log = Logger.getLogger(RefreshJioyrqJob.class);
	public static final String SCHEDULER_NAME = "RefreshJioyrqJob";
	public static final String TRIGGER_NAME = "RefreshJioyrqTrigger";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		log.debug(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));
		PropertiesLoader instance = PropertiesLoader.getInstance();
		if (PropertiesLoader.isJioyrqUseCatch()) {
			instance.refreshJioyrq();
			log.info(" Refresh Jioyrq: " + instance.getProperty(RegisterTable.CATCHED_JIOYRQ));
		}

		log.debug(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}
