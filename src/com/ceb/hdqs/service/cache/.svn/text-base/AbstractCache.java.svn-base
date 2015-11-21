package com.ceb.hdqs.service.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;

public abstract class AbstractCache {
	protected boolean isExpired(long currentT, long lastModifiedT) {
		String prop = ConfigLoader.getInstance().getProperty(RegisterTable.CACHE_EXPIRED_MINUTE_THRESHOLD);
		long threshold = 1 * 1000;
		if (StringUtils.isNotBlank(prop)) {
			threshold = TimeUnit.MINUTES.toMillis(Long.parseLong(prop));
		}
		if ((currentT - lastModifiedT) > threshold) {
			return true;
		}
		return false;
	}
}