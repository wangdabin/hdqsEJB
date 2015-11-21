package com.ceb.hdqs.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;

public final class HdqsUtils {
	private static final Pattern INTEGER_PATTERN = Pattern.compile("[0-9]+");

	private HdqsUtils() {

	}

	public static boolean isWindows() {
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("win")) {
			return true;
		}
		return false;
	}

	public static boolean isWorkingTime(Calendar currentTime) {
		String str = ConfigLoader.getInstance().getProperty(RegisterTable.WORKING_TIME_SPAN);
		String[] spanArray = str.split("\\|");

		return isFitSpanArray(currentTime, spanArray);
	}

	public static boolean isSynchronizeToClusterTime(Calendar currentTime) {
		String str = ConfigLoader.getInstance().getProperty(RegisterTable.DB_SYNCHRONIZE_TO_CLUSTER_TIME_SPAN);
		String[] spanArray = str.split("\\|");

		return isFitSpanArray(currentTime, spanArray);
	}

	private static boolean isFitSpanArray(Calendar currentTime, String[] spanArray) {
		Calendar from = Calendar.getInstance();
		from.set(Calendar.SECOND, 0);// second 置0
		Calendar to = Calendar.getInstance();
		to.set(Calendar.SECOND, 0);// second 置0
		boolean result = false;
		// 9:30-11:30|14:00-17:00
		for (int i = 0, size = spanArray.length; i < size; i++) {
			String spanStr = spanArray[i];
			initSpanFromAndTo(from, to, spanStr);
			if ((currentTime.compareTo(from) == 0 || currentTime.after(from)) && (currentTime.compareTo(to) == 0 || currentTime.before(to))) {
				result = true;
				break;
			}
		}

		return result;
	}

	private static void initSpanFromAndTo(Calendar from, Calendar to, String spanStr) {
		if (spanStr.contains("-")) {
			String[] timeArray = spanStr.split("-");
			String startSpan = timeArray[0];
			if (startSpan.contains(":")) {
				String[] hourArray = startSpan.split(":");
				from.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourArray[0]));
				from.set(Calendar.MINUTE, Integer.parseInt(hourArray[1]));
			} else {
				from.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startSpan));
				from.set(Calendar.MINUTE, 0);
			}
			String endSpan = timeArray[1];
			if (endSpan.contains(":")) {
				String[] hourArray = endSpan.split(":");
				to.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourArray[0]));
				to.set(Calendar.MINUTE, Integer.parseInt(hourArray[1]));
			} else {
				to.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endSpan));
				to.set(Calendar.MINUTE, 0);
			}
		} else {
			String startSpan = spanStr;
			if (startSpan.contains(":")) {
				String[] hourArray = startSpan.split(":");
				from.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourArray[0]));
				from.set(Calendar.MINUTE, Integer.parseInt(hourArray[1]));
			} else {
				from.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startSpan));
				from.set(Calendar.MINUTE, 0);
			}
			String endSpan = spanStr;
			if (endSpan.contains(":")) {
				String[] hourArray = endSpan.split(":");
				to.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourArray[0]));
				to.set(Calendar.MINUTE, Integer.parseInt(hourArray[1]));
			} else {
				to.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endSpan));
				to.set(Calendar.MINUTE, 0);
			}
		}
	}

	public static boolean isBlank(String str) {
		return StringUtils.isBlank(str);
	}

	public static boolean isNotBlank(String str) {
		return StringUtils.isNotBlank(str);
	}

	public static boolean isNumeric(String str) {
		return INTEGER_PATTERN.matcher(str).matches();
	}

	public static final String FOLDER_SEPARATOR = "/";
	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	public static String formatFilePath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = org.springframework.util.StringUtils.replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);
		return pathToUse;
	}

	public static boolean validateDate(String startDate, String endDate) {
		boolean result = true;
		try {
			long startTime = DateFormatUtils.parseDate(startDate).getTime();
			long endTime = DateFormatUtils.parseDate(endDate).getTime();
			if (startTime > endTime) {
				result = false;
			}
		} catch (ParseException e) {
			result = false;
		}

		return result;
	}

	public static File[] listBeforeFiles(String fileDir, final String fileRegex, final String filePrefix, final long endT) {
		return listFiles(false, fileDir, fileRegex, filePrefix, 0, endT);
	}

	public static File[] listFiles(final boolean rmEmpty, String fileDir, final String fileRegex, final String filePrefix, final long startT, final long endT) {
		if (isBlank(fileDir)) {
			return new File[0];
		}

		File filePath = new File(fileDir);
		if (!filePath.exists()) {
			return new File[0];
		}
		if (!filePath.isDirectory()) {
			return new File[0];
		}
		File[] fileArray = filePath.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (rmEmpty && file.length() == 0) {
					file.delete();
					return false;
				}
				if ((isBlank(fileRegex) || file.getName().matches(fileRegex)) && (isBlank(filePrefix) || file.getName().startsWith(filePrefix))) {
					if (match(file, startT, endT)) {
						return true;
					}
				}
				return false;
			}
		});
		return fileArray;
	}

	private static boolean match(File file, long startTime, long endTime) {
		if (startTime == 0 && endTime == 0) {
			return true;
		}
		long time = file.lastModified();
		if (endTime == 0) {
			if (time >= startTime)
				return true;
		} else if (time >= startTime && time <= endTime)
			return true;

		return false;
	}

	public static String getFixLengthStr(String str, int length) {
		int len = str.length();
		if (len > length) {
			return str.substring(0, length);
		}
		return str;
	}

	public static String getHostName() throws UnknownHostException {
		InetAddress addr = InetAddress.getLocalHost();
		System.out.println("Local HostAddress: " + addr.getHostAddress());
		System.out.println("Local HostName: " + addr.getHostName());
		return addr.getHostName();
	}

	public static String getMacAddr() throws SocketException {
		StringBuilder str = new StringBuilder();
		NetworkInterface ni = NetworkInterface.getByName("eth0");
		byte[] buf = ni.getHardwareAddress();
		for (int i = 0; i < buf.length; i++) {
			str.append(byteToHex(buf[i]));
		}
		return str.toString().toUpperCase();
	}

	public static String getLocalIP() throws SocketException {
		String ip = "";
		Enumeration<?> e1 = (Enumeration<?>) NetworkInterface.getNetworkInterfaces();
		while (e1.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) e1.nextElement();
			if (!ni.getName().equals("eth0")) {
				continue;
			}
			Enumeration<?> e2 = ni.getInetAddresses();
			while (e2.hasMoreElements()) {
				InetAddress addr = (InetAddress) e2.nextElement();
				if (addr instanceof Inet6Address) {
					continue;
				}
				ip = addr.getHostAddress();
			}
			break;
		}
		return ip;
	}

	/* 一个将字节转化为十六进制ASSIC码的函数 */
	private static String byteToHex(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] cArray = new char[2];
		cArray[0] = Digit[(ib >>> 4) & 0X0F];
		cArray[1] = Digit[ib & 0X0F];
		return new String(cArray);
	}
}