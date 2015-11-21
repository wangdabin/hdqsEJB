package com.ceb.hdqs.service.scheduler;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.utils.Key;

import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;

public final class CronSchedulerService {
	private static final Logger log = Logger.getLogger(CronSchedulerService.class);
	private static Object lock = new Object();
	private volatile static CronSchedulerService instance = null;
	private static Scheduler scheduler = null;

	private CronSchedulerService() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	public static CronSchedulerService getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new CronSchedulerService();
				}
			}
		}
		return instance;
	}

	public void startScheduler() {
		if (scheduler == null) {
			return;
		}
		try {
			if (!scheduler.isShutdown()) {
				scheduler.deleteJob(new JobKey(RemoveExpiredCacheJob.SCHEDULER_NAME, Key.DEFAULT_GROUP));
				scheduler.deleteJob(new JobKey(SynchronizedToHbaseJob.SCHEDULER_NAME, Key.DEFAULT_GROUP));
//				scheduler.deleteJob(new JobKey(RemoveExpiredLogJob.SCHEDULER_NAME, Key.DEFAULT_GROUP));
				scheduler.deleteJob(new JobKey(AsynHandlerJob.SCHEDULER_NAME, Key.DEFAULT_GROUP));
				scheduler.deleteJob(new JobKey(RefreshJioyrqJob.SCHEDULER_NAME, Key.DEFAULT_GROUP));
				scheduler.deleteJob(new JobKey(RefreshJgcsJob.SCHEDULER_NAME, Key.DEFAULT_GROUP));
				// scheduler.deleteJob(new JobKey(UpdateMacJob.SCHEDULER_NAME,
				// Key.DEFAULT_GROUP));
			}
			log.info("------- Scheduling Jobs ----------------");
			/**
			 * remove expired cache
			 */
			PropertiesLoader instance = PropertiesLoader.getInstance();
			JobDetail job = JobBuilder.newJob(RemoveExpiredCacheJob.class).withIdentity(RemoveExpiredCacheJob.SCHEDULER_NAME, Key.DEFAULT_GROUP).build();
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(RemoveExpiredCacheJob.TRIGGER_NAME, Key.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_REMOVEEXPIREDCACHE))).build();
			scheduler.scheduleJob(job, trigger);
			log.info(job.getKey() + " scheduled,expression: " + trigger.getCronExpression());

			/**
			 * remove expired record
			 */
			job = JobBuilder.newJob(SynchronizedToHbaseJob.class).withIdentity(SynchronizedToHbaseJob.SCHEDULER_NAME, Key.DEFAULT_GROUP).build();
			trigger = TriggerBuilder.newTrigger().withIdentity(SynchronizedToHbaseJob.TRIGGER_NAME, Key.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_SYNCHRONIZEDTOHBASE))).build();
			scheduler.scheduleJob(job, trigger);
			log.info(job.getKey() + " scheduled,expression: " + trigger.getCronExpression());

			/**
			 * remove expired log
			 */
			// job =
			// JobBuilder.newJob(RemoveExpiredLogJob.class).withIdentity(RemoveExpiredLogJob.SCHEDULER_NAME,
			// Key.DEFAULT_GROUP).build();
			// trigger =
			// TriggerBuilder.newTrigger().withIdentity(RemoveExpiredLogJob.TRIGGER_NAME,
			// Key.DEFAULT_GROUP)
			// .withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_REMOVEEXPIREDLOG))).build();
			// scheduler.scheduleJob(job, trigger);
			// log.info(job.getKey() + " scheduled,expression: " +
			// trigger.getCronExpression());

			/**
			 * asyn handle
			 */
			job = JobBuilder.newJob(AsynHandlerJob.class).withIdentity(AsynHandlerJob.SCHEDULER_NAME, Key.DEFAULT_GROUP).build();
			trigger = TriggerBuilder.newTrigger().withIdentity(AsynHandlerJob.TRIGGER_NAME, Key.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_ASYNHANDLER))).build();
			scheduler.scheduleJob(job, trigger);
			log.info(job.getKey() + " scheduled,expression: " + trigger.getCronExpression());

			/**
			 * update mac
			 */
			// job =
			// JobBuilder.newJob(UpdateMacJob.class).withIdentity(UpdateMacJob.SCHEDULER_NAME,
			// Key.DEFAULT_GROUP).build();
			// trigger =
			// TriggerBuilder.newTrigger().withIdentity(UpdateMacJob.TRIGGER_NAME,
			// Key.DEFAULT_GROUP)
			// .withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_UPDATEMAC))).build();
			// scheduler.scheduleJob(job, trigger);
			// log.info(job.getKey() + " scheduled,expression: " +
			// trigger.getCronExpression());

			/**
			 * refresh jioyrq
			 */
			job = JobBuilder.newJob(RefreshJioyrqJob.class).withIdentity(RefreshJioyrqJob.SCHEDULER_NAME, Key.DEFAULT_GROUP).build();
			trigger = TriggerBuilder.newTrigger().withIdentity(RefreshJioyrqJob.TRIGGER_NAME, Key.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_REFRESH_JIOYRQ))).build();
			scheduler.scheduleJob(job, trigger);
			log.info(job.getKey() + " scheduled,expression: " + trigger.getCronExpression());

			/**
			 * refresh JGCS
			 */
			job = JobBuilder.newJob(RefreshJgcsJob.class).withIdentity(RefreshJgcsJob.SCHEDULER_NAME, Key.DEFAULT_GROUP).build();
			trigger = TriggerBuilder.newTrigger().withIdentity(RefreshJgcsJob.TRIGGER_NAME, Key.DEFAULT_GROUP)
					.withSchedule(CronScheduleBuilder.cronSchedule(instance.getProperty(RegisterTable.CRON_REFRESHJGCS))).build();
			scheduler.scheduleJob(job, trigger);
			log.info(job.getKey() + " scheduled,expression: " + trigger.getCronExpression());

			scheduler.start();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			try {
				scheduler.shutdown(true);
			} catch (SchedulerException e1) {
			}
		}
	}

	public void stopScheduler() {
		if (scheduler == null) {
			return;
		}
		try {
			scheduler.shutdown(true);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}