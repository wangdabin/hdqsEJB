package com.ceb.hdqs.config;

import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.utils.HdqsUtils;

public abstract class AbstractLoader {
	protected static final String HDQS_HOME = "HDQS_HOME";
	
	protected void setLogEnv(String logDir) {
		String value = getEnvValue(logDir);
		if (StringUtils.isBlank(value)) {
			System.err.println("ERROR: The properties " + logDir + " cannot be null or empty.");
			System.exit(-1);
		}
		value = HdqsUtils.formatFilePath(value);
		System.setProperty(logDir, value);
	}

	protected String getEnvValue(String key) {
		String value = System.getProperty(key);
		if (StringUtils.isBlank(value)) {
			value = System.getenv(key);
		}
		if (StringUtils.isNotBlank(value)) {
			System.out.println("In system env, " + key + "=" + value);
		}
		return value;
	}
}
