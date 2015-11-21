package com.ceb.hdqs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateTimeFormatUtils {
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss ";

	private DateTimeFormatUtils() {

	}

	private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
		protected synchronized SimpleDateFormat initialValue() {
			SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DATETIME_FORMAT);
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
		System.out.println(DateTimeFormatUtils.formatDate(1380520403620L));
		System.out.println(DateTimeFormatUtils.formatDate(1380520410020L));
		System.out.println(StringUtils.reverse("78720181000076749"));
	}
}