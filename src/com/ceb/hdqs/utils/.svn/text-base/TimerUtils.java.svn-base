package com.ceb.hdqs.utils;

public class TimerUtils {
	private long startTime;
	private long endTime;

	public void start() {
		startTime = System.currentTimeMillis();
	}

	public void stop() {
		endTime = System.currentTimeMillis();
	}

	public float getExecutionTime() {
		return endTime - startTime;
	}

	public float getExecutionTimeSec() {
		return (endTime - startTime) / 1000f;
	}

	public float getExecutionTimeMin() {
		return (endTime - startTime) / (60 * 1000f);
	}
}