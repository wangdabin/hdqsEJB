package com.ceb.hdqs.service.impl;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.ceb.hdqs.config.PropertiesLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.dao.JyrqDao;
import com.ceb.hdqs.entity.JyrqEO;
import com.ceb.hdqs.service.IJyrqService;
import com.ceb.hdqs.service.JyrqService;
import com.ceb.hdqs.utils.DateFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;

@Stateless(mappedName = "JyrqService")
public class JyrqServiceImpl implements IJyrqService, JyrqService {
	@EJB
	JyrqDao referDao;

	public String queryJyrq() {
		if (PropertiesLoader.isJioyrqUseCatch()) {
			return PropertiesLoader.getInstance().getProperty(RegisterTable.CATCHED_JIOYRQ);
		} else {
			return saveOrUpdate();
		}
	}

	public String saveOrUpdate() {
		List<JyrqEO> list = referDao.findAll();
		if (list == null || list.isEmpty()) {
			JyrqEO newObj = new JyrqEO();
			init(newObj);
			referDao.save(newObj);
			return newObj.getJioyrq();
		}
		return list.get(0).getJioyrq();
	}

	public String updateJyrq() {
		List<JyrqEO> list = referDao.findAll();

		if (list == null || list.isEmpty()) {
			JyrqEO newObj = new JyrqEO();
			init(newObj);
			referDao.save(newObj);
			return newObj.getJioyrq();
		} else {
			JyrqEO entity = list.get(0);
			init(entity);
			referDao.update(entity);
			return entity.getJioyrq();
		}
	}

	public String forceUpdate(String jioyrq) {
		List<JyrqEO> list = referDao.findAll();

		if (list == null || list.isEmpty()) {
			JyrqEO newObj = new JyrqEO();
			init(newObj);
			referDao.save(newObj);
			return newObj.getJioyrq();
		} else {
			JyrqEO entity = list.get(0);
			updateJyrqObj(entity, jioyrq);
			referDao.update(entity);
			return entity.getJioyrq();
		}
	}

	private void init(JyrqEO entity) {
		long currentT = System.currentTimeMillis();
		entity.setJioyrq(DateFormatUtils.formatDate(currentT));
	}

	private void updateJyrqObj(JyrqEO entity, String jioyrq) {
		Date currentT = null;
		try {
			if (HdqsUtils.isBlank(jioyrq)) {
				currentT = new Date();
			} else {
				currentT = DateFormatUtils.parseDate(jioyrq);
			}
		} catch (ParseException e) {
			currentT = new Date();
		}

		entity.setJioyrq(DateFormatUtils.formatDate(currentT));
	}

	public void refreshJyrqObj(JyrqEO entity, String jioyrq) {
		Calendar instanse = Calendar.getInstance();
		if (HdqsUtils.isNotBlank(jioyrq)) {
			Date currentT = null;
			try {
				currentT = DateFormatUtils.parseDate(jioyrq);
				instanse.setTime(currentT);
			} catch (ParseException e) {
			}
		}

		entity.setJioyrq(DateFormatUtils.formatDate(instanse.getTimeInMillis()));
		instanse.add(Calendar.DAY_OF_MONTH, -1);
		entity.setScjyrq(DateFormatUtils.formatDate(instanse.getTimeInMillis()));
		instanse.add(Calendar.DAY_OF_MONTH, -1);
		entity.setSsjyrq(DateFormatUtils.formatDate(instanse.getTimeInMillis()));
		instanse.add(Calendar.DAY_OF_MONTH, 3);
		entity.setXcjyrq(DateFormatUtils.formatDate(instanse.getTimeInMillis()));
		instanse.add(Calendar.DAY_OF_MONTH, -1);//
		instanse.set(Calendar.MONTH, 11);
		instanse.set(Calendar.DAY_OF_MONTH, 31);
		entity.setZzhirq(DateFormatUtils.formatDate(instanse.getTimeInMillis()));
		entity.setShjnch(System.currentTimeMillis());
	}
}