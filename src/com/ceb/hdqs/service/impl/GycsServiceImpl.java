package com.ceb.hdqs.service.impl;

import java.lang.reflect.InvocationTargetException;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.dao.GycsDao;
import com.ceb.hdqs.entity.GycsEO;
import com.ceb.hdqs.service.GycsService;
import com.ceb.hdqs.service.IGycsService;
import com.ceb.hdqs.vo.GycsVO;

@Stateless(mappedName = "GycsService")
public class GycsServiceImpl implements IGycsService, GycsService {
	private static final Logger log = Logger.getLogger(GycsServiceImpl.class);
	@EJB
	GycsDao referDao;

	public void saveOrUpdate(GycsVO entity) {
		GycsEO existObj = findByGuiydh(entity.getGuiydh());
		try {
			if (existObj == null) {
				GycsEO newObj = new GycsEO();
				BeanUtils.copyProperties(newObj, entity);
				save(newObj);
			} else {
				BeanUtils.copyProperties(existObj, entity);
				update(existObj);
			}
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void save(GycsEO entity) {
		referDao.save(entity);
	}

	public void update(GycsEO entity) {
		referDao.update(entity);
	}

	public GycsEO findById(Long id) {
		return referDao.findById(id);
	}

	public GycsEO findByGuiydh(String guiydh) {
		return referDao.findByGuiydh(guiydh);
	}

	public void removeAll() {
		referDao.deleteAll();
	}

}