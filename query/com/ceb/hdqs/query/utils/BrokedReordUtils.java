package com.ceb.hdqs.query.utils;

import java.util.ArrayList;
import java.util.List;
import com.ceb.hdqs.query.entity.QueryDocumentContext;

/**
 * 半角、全角文字处理工具类
 * 
 * @author user
 * 
 */
public class BrokedReordUtils {
	/**
	 * 将不连续的账号缓存到 brokedCache
	 * @param documentContext上下文环境
	 * @param key
	 * @param errorMsg
	 */
	public static void cacheBrokedReord(QueryDocumentContext documentContext, String key, String errorMsg) {
		if (documentContext.getBrokedCache().get(key) == null) {
			List<String> list = new ArrayList<String>();
			list.add(errorMsg);
			documentContext.getBrokedCache().put(key, list);
		} else {
			documentContext.getBrokedCache().get(key).add(errorMsg);
		}
	}

}