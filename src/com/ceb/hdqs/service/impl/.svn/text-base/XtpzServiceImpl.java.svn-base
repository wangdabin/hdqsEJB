package com.ceb.hdqs.service.impl;

import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.dao.XtpzDao;
import com.ceb.hdqs.entity.XtpzEO;
import com.ceb.hdqs.service.IXtpzService;
import com.ceb.hdqs.service.XtpzService;
import com.ceb.hdqs.utils.DateTimeFormatUtils;

@Stateless(mappedName = "XtpzService")
public class XtpzServiceImpl implements XtpzService, IXtpzService {
	@EJB
	XtpzDao xtpzDao;

	public void updateDeadLineForQuery(String queryTime) {
		// public static final String QUERY_END_TIME_LIMIT_SWITCH =
		// "query.end.time.limit.switch";
		// public static final String QUERY_END_TIME_LIMIT =
		// "query.end.time.limit";
		XtpzEO entity = findRecordByName(RegisterTable.QUERY_END_TIME_LIMIT);
		entity.setValue(queryTime);
		entity.setWeihrq(DateTimeFormatUtils.formatDate(System.currentTimeMillis()));
		xtpzDao.update(entity);
	}

	public XtpzEO findRecordByName(String name) {
		return xtpzDao.findRecordByName(name);
	}

	public List<XtpzEO> findAll() {
		List<XtpzEO> list = xtpzDao.findAll();
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}
}