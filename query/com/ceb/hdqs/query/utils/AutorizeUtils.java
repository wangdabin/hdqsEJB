package com.ceb.hdqs.query.utils;

import com.ceb.hdqs.service.SqcsService;
import com.ceb.hdqs.utils.JNDIUtils;

/**
 * 验证柜员是否具有维护权限工具类
 * 
 * 
 * 
 */
public class AutorizeUtils {
	public static SqcsService sqcsService;
	static
	{
		try {
			sqcsService = (SqcsService) JNDIUtils.lookup(SqcsService.class);
		} catch (Exception e) {

		}
	}
	public static boolean authorize(String Jio1gy) {
		return sqcsService.authorize(Jio1gy);
	}

}