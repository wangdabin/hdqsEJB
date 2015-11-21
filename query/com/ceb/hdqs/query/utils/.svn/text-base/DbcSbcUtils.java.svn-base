package com.ceb.hdqs.query.utils;

import java.io.PrintStream;

/**
 * 半角、全角文字处理工具类
 * 
 * @author user
 * 
 */
public class DbcSbcUtils {
	/**
	 * 根据字符的Unicode码判断字符是半角字符还是全角字符
	 * 
	 * @param c
	 *            字符
	 * @return true:半角; false:全角
	 */
	public static boolean isDbcCase(char c) {
		// 基本拉丁字母(即键盘上可见的空格、数字、字母和符号)
		if (c >= 32 && c <= 127) {
			return true;
		} else if (c >= 65377 && c <= 65439) {
			// 日文半角
			return true;
		}
		return false;
	}

	/**
	 * 获取字符串长度(区分半角、全角)
	 * 
	 * @param str
	 * @return
	 */
	public static int getLength(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isDbcCase(c)) {
				len = len + 1;
			} else {
				len = len + 2;
			}
		}
		return len;
	}

	/**
	 * 获取字符串中全角字符长度
	 * 
	 * @param str
	 * @return
	 */
	public static int getSbcLength(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!isDbcCase(c)) {
				len = len + 1;
			}
		}
		return len;
	}

	/**
	 * 获取字符串中半角字符长度
	 * 
	 * @param str
	 * @return
	 */
	public static int getDbcLength(String str) {
		int len = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (isDbcCase(c)) {
				len = len + 1;
			}
		}
		return len;
	}

	/**
	 * 字符串截取(区分半角、全角)
	 * 
	 * @param str
	 * @param limit
	 * @return
	 */
	public static String left(String str, int limit) {
		if(str==null){
			return "";
		}
		if (getLength(str) <= limit) {
			return str;
		}
		char[] chars = str.toCharArray();
		int charLenSum = 0;
		String result = "";
		for (int i = 0; i < chars.length; i++) {
			int charLen = isDbcCase(chars[i]) ? 1 : 2;
			if (charLenSum + charLen > limit) {
				return result;
			}
			charLenSum += charLen;
			result += chars[i];
			if (charLenSum == limit) {
				return result;
			}
		}
		return "";
	}

	public static void fixLeftPrint(PrintStream printer, String str, int limit) {
		int totalLen = limit * 2;// 固定最大长度,全是全角时最大长度
		String lstr = DbcSbcUtils.left(str, totalLen);// 截取字符串
		int sbclen = DbcSbcUtils.getSbcLength(lstr);// 计算全角字符个数

		printer.printf("%1s%-" + (totalLen - sbclen) + "s", "", lstr);
	}

	public static void fixRightPrint(PrintStream printer, String str, int limit) {
		int totalLen = limit * 2;// 固定最大长度,全是全角时最大长度
		String lstr = DbcSbcUtils.left(str, totalLen);// 截取字符串
		int sbclen = DbcSbcUtils.getSbcLength(lstr);// 计算全角字符个数

		printer.printf("%1s%" + (totalLen - sbclen) + "s", "", lstr);
	}
	
}