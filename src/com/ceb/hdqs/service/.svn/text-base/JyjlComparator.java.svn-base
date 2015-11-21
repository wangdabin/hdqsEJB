package com.ceb.hdqs.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.ceb.hdqs.entity.PjyjlEO;
import com.google.common.primitives.Longs;

public class JyjlComparator implements Comparator<PjyjlEO> {
	/**
	 * 按照营业机构升序排序，按照时间从大到小排序
	 */
	public int compare(PjyjlEO left, PjyjlEO right) {
		int ret = left.getYngyjg().compareTo(right.getYngyjg());
		if (ret != 0) {
			return ret;
		}
		int b = left.getJioyrq().compareTo(right.getJioyrq());
		if (b != 0) {
			return -b;
		}
		return -Longs.compare(left.getStartTime(), right.getStartTime());
	}

	public static void main(String[] args) {
		PjyjlEO r1 = new PjyjlEO();
		r1.setYngyjg("1001");
		r1.setJioyrq("20131104");
		r1.setStartTime(3111111111111L);

		PjyjlEO r2 = new PjyjlEO();
		r2.setYngyjg("1001");
		r2.setJioyrq("20131104");
		r2.setStartTime(2222222222222L);
		List<PjyjlEO> list = new ArrayList<PjyjlEO>();
		list.add(r1);
		list.add(r2);
		Collections.sort(list, new JyjlComparator());
		for (PjyjlEO re : list) {
			System.out.println(re);
		}
	}
}