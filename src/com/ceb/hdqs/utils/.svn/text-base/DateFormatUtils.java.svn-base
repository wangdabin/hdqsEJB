package com.ceb.hdqs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateFormatUtils {
	private static final String DATE_FORMAT = "yyyyMMdd";

	private DateFormatUtils() {

	}

	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DATE_FORMAT);
			// 严格控制输入 比如2006-02-31，根本没有这一天，也会认为时间格式不对
			defaultDateFormat.setLenient(false);
			return defaultDateFormat;
		}
	};

	public static Date parseDate(String dateStr) throws ParseException {
		return threadLocal.get().parse(dateStr);
	}

	public static Date parseDate(String dateStr, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.parse(dateStr);
	}

	public static String formatDate(Date date) {
		return threadLocal.get().format(date);
	}

	public static String formatDate(Long date) {
		return threadLocal.get().format(date);
	}

	public static String formatDate(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.format(date);
	}

	public static String formatDate(Long date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setLenient(false);
		return sdf.format(date);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(DateFormatUtils.formatDate(System.currentTimeMillis()));
	}
}
