package com.ceb.hdqs.utils;

import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public final class JNDIUtils {
	private static Map<String, Object> cache = new HashMap<String, Object>();
	private static Object lock = new Object();
	private static Context ctx;

	private JNDIUtils() {
	}

	public static Context getInitialContext() throws NamingException {
		if (ctx == null) {
			synchronized (lock) {
				if (ctx == null) {
					ctx = new InitialContext();
				}
			}
		}
		return ctx;
	}

	public synchronized static Object lookup(String ejbName) throws NamingException {
		Object obj = cache.get(ejbName);
		if (obj == null) {
			obj = getInitialContext().lookup(ejbName);
			cache.put(ejbName, obj);
		}
		return obj;
	}

	@SuppressWarnings("rawtypes")
	public static Object lookup(Class clazz) throws NamingException {
		return lookup(clazz, null);
	}

	@SuppressWarnings("rawtypes")
	public static Object lookup(Class clazz, String mappingName) throws NamingException {
		String ejbName = clazz.getSimpleName();
		if (mappingName != null && mappingName.trim().length() > 0) {
			ejbName = mappingName;
		}
		return lookup(ejbName + "#" + clazz.getName());
	}
}
