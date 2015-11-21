package com.ceb.hdqs.service;

import javax.ejb.Local;

@Local
public interface ITimerScheduleService {
	public void startTimer();
}