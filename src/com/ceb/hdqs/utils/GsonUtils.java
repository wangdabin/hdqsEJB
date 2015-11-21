package com.ceb.hdqs.utils;

import com.google.gson.Gson;

public final class GsonUtils {
	private static final Gson gson = new Gson();

	private GsonUtils() {
	}

	public static Gson getInstance() {
		return gson;
	}

	public static String fromJson(Object o) {
		return gson.toJson(o);
	}
}
