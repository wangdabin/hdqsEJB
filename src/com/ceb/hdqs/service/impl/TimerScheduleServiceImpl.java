package com.ceb.hdqs.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.service.ITimerScheduleService;
import com.ceb.hdqs.service.cache.Handle0770Cache;
import com.ceb.hdqs.service.cache.Handle0771Cache;
import com.ceb.hdqs.service.cache.Handle0772Cache;
import com.ceb.hdqs.service.cache.Handle0773Cache;

@Stateless
public class TimerScheduleServiceImpl implements ITimerScheduleService {
	private static final Logger log = Logger.getLogger(TimerScheduleServiceImpl.class);
	private static final String TIMER_SCHEDULE = "schedule_service";
	@Resource
	private TimerService timerService;

	public TimerScheduleServiceImpl() {
	}

	@PostConstruct
	// @PostConstruct修饰的方法会在EJB创建之后被EJB容器调用
	public void postConstruct() {
		log.debug("执行了@PostConstruct修饰的方法！");
	}

	@PreDestroy
	// @PreDestroy修饰的方法会在EJB销毁之前被EJB容器调用
	public void preDestroy() {
		log.debug("执行了@PreDestroy修饰的方法！");
	}

	@Timeout
	public void ejbTimeout(Timer timerRef) {
		log.debug("执行了@Timeout修饰的方法！");
		Handle0770Cache.getInstance().process();
		Handle0771Cache.getInstance().process();
		Handle0772Cache.getInstance().process();
		Handle0773Cache.getInstance().process();
	}

	/**
	 * 启动定时任务
	 */
	public void startTimer() {
		this.stopAllTimer();

		long interval = 5 * 60 * 1000;
		log.info("Start timer " + TIMER_SCHEDULE);
		timerService.createTimer(1000, interval, TIMER_SCHEDULE);
	}

	public void stopAllTimer() {
		@SuppressWarnings("unchecked")
		Iterator<Timer> iter = timerService.getTimers().iterator();
		while (iter.hasNext()) {
			Timer timer = (Timer) iter.next();
			log.info("Cancel timer " + timer.getInfo());
			timer.cancel();
		}
	}

	public void stopTimer(String timerName) {
		@SuppressWarnings("unchecked")
		Iterator<Timer> iter = timerService.getTimers().iterator();
		while (iter.hasNext()) {
			Timer timer = (Timer) iter.next();
			String info = (String) timer.getInfo();
			if (StringUtils.isNotBlank(info) && info.equals(timerName)) {
				log.info("Cancel timer " + timerName);
				timer.cancel();
			}
		}
	}

	public List<String> getAllTimerInfo() {
		List<String> list = new ArrayList<String>();
		@SuppressWarnings("unchecked")
		Iterator<Timer> iter = timerService.getTimers().iterator();
		while (iter.hasNext()) {
			Timer timer = (Timer) iter.next();
			String info = (String) timer.getInfo();
			list.add(info);
		}

		return list;
	}
}