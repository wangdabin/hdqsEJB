package com.ceb.hdqs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ceb.hdqs.entity.PybjyEO;
import com.google.common.primitives.Longs;

public class YbjyComprator implements Comparator<PybjyEO> {
	/**
	 * 按时间从小到大排序
	 */
	public int compare(PybjyEO left, PybjyEO right) {
		return Longs.compare(left.getStartTime(), right.getStartTime());
	}

	public static void main(String[] args) {
		PybjyEO record1 = new PybjyEO();
		record1.setStartTime(11111L);

		PybjyEO record2 = new PybjyEO();
		record2.setStartTime(22222L);

		List<PybjyEO> list = new ArrayList<PybjyEO>();
		list.add(record1);
		list.add(record2);
		Collections.sort(list, new YbjyComprator());
		for (PybjyEO r : list) {
			System.out.println(r);
		}
	}
}