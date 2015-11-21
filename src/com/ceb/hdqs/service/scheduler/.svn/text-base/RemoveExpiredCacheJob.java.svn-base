package com.ceb.hdqs.service.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.ceb.hdqs.service.cache.Handle0770Cache;
import com.ceb.hdqs.service.cache.Handle0771Cache;
import com.ceb.hdqs.service.cache.Handle0772Cache;
import com.ceb.hdqs.service.cache.Handle0773Cache;
import com.ceb.hdqs.service.cache.Handle0775Cache;
import com.ceb.hdqs.service.cache.Handle0779Cache;
import com.ceb.hdqs.utils.DateTimeFormatUtils;

public class RemoveExpiredCacheJob implements Job {
	private static final Logger log = Logger.getLogger(RemoveExpiredCacheJob.class);
	public static final String SCHEDULER_NAME = "RemoveExpiredCacheJob";
	public static final String TRIGGER_NAME = "RemoveExpiredCacheTrigger";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		org.quartz.JobKey jobKey = context.getJobDetail().getKey();
		log.debug(jobKey + " executing at " + DateTimeFormatUtils.formatDate(new Date()));

		Handle0770Cache.getInstance().process();
		Handle0771Cache.getInstance().process();
		Handle0772Cache.getInstance().process();
		Handle0773Cache.getInstance().process();
		Handle0775Cache.getInstance().process();
		Handle0779Cache.getInstance().process();
		log.debug(jobKey + " completed at " + DateTimeFormatUtils.formatDate(new Date()));
	}
}
