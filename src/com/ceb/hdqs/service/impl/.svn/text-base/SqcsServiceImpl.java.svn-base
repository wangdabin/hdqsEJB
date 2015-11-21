package com.ceb.hdqs.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.dao.SqcsDao;
import com.ceb.hdqs.entity.SqcsEO;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.IJyrqService;
import com.ceb.hdqs.service.ISqcsService;
import com.ceb.hdqs.service.SqcsService;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.form.Handle0780Form;

@Stateless(mappedName = "SqcsService")
public class SqcsServiceImpl implements SqcsService, ISqcsService {
	private static final Logger log = Logger.getLogger(SqcsServiceImpl.class);
	@EJB
	SqcsDao referDao;
	@EJB
	IJyrqService jyrqService;

	public void save(Handle0780Form form) throws HdqsServiceException {
		SqcsEO existObj = referDao.findByGuiydh(form.getGuiydh());
		if (existObj != null) {
			throw new HdqsServiceException("柜员代号存在其他重复对象,不允许增加");
		}
		SqcsEO entity = new SqcsEO();
		try {
			BeanUtils.copyProperties(entity, form);
			entity.setJiluzt(HdqsConstants.STATUS_NORMAL);
			referDao.save(entity);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void delete(Handle0780Form form) throws HdqsServiceException {
		SqcsEO entity = referDao.findById(Long.valueOf(form.getHandid()));
		if (entity == null) {
			throw new HdqsServiceException("待删除对象不存在或已经删除");
		}
		if (entity.getJiluzt().equals(HdqsConstants.STATUS_UNUSE)) {
			throw new HdqsServiceException("待删除对象已经处于删除状态,不允许重复删除");
		}
		entity.setWeihgy(form.getWeihgy());
		entity.setWeihrq(form.getWeihrq());
		entity.setWeihxm(form.getWeihxm());
		entity.setJiluzt(HdqsConstants.STATUS_UNUSE);
		referDao.update(entity);
	}

	public SqcsEO update(Handle0780Form form) throws HdqsServiceException {
		SqcsEO entity = referDao.findById(Long.valueOf(form.getHandid()));
		if (entity == null) {
			throw new HdqsServiceException("待修改对象不存在或已经删除");
		}
		if (!form.getGuiydh().equals(entity.getGuiydh())) {
			SqcsEO existObj = referDao.findByGuiydh(form.getGuiydh());
			if (existObj != null) {
				throw new HdqsServiceException("柜员代号存在其他重复对象,不允许修改");
			}
		}

		try {
			BeanUtils.copyProperties(entity, form);
			// entity.setJiluzt(HdqsConstants.STATUS_NORMAL);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		referDao.update(entity);
		return entity;
	}

	public List<SqcsEO> findByProperty(Handle0780Form form) {
		return this.findByProperty(form, 0, 0);
	}

	public List<SqcsEO> findByProperty(Handle0780Form form, int startIdx, int count) {
		StringBuffer buff = new StringBuffer("select model from SqcsEO model where 1=1");
		if (HdqsUtils.isNotBlank(form.getGuiydh()))
			buff.append(" and model.guiydh='" + form.getGuiydh() + "'");
		if (HdqsUtils.isNotBlank(form.getGuiyxm()))
			buff.append(" and model.guiyxm='" + form.getGuiyxm() + "'");
		if (HdqsUtils.isNotBlank(form.getGylxdm()))
			buff.append(" and model.gylxdm='" + form.getGylxdm() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihrq()))
			buff.append(" and model.weihrq='" + form.getWeihrq() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihgy()))
			buff.append(" and model.weihgy='" + form.getWeihgy() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihxm()))
			buff.append(" and model.weihxm='" + form.getWeihxm() + "'");
		if (HdqsUtils.isNotBlank(form.getJiluzt()))
			buff.append(" and model.jiluzt='" + form.getJiluzt() + "'");
		buff.append(" order by model.id asc");
		List<SqcsEO> list = referDao.findByProperty(buff.toString(), startIdx, count);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list;
	}

	public List<SqcsEO> findAll() {
		List<SqcsEO> list = referDao.findAll(0, 0);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list;
	}

	public boolean authorize(String jio1gy) {
		/**
		 * 满足下列条件可以做0779交易： Select * from 柜员权限表 where 交易柜员=柜员代号 and 交易日期>=权限起始日期
		 * and 交易日期<=权限终止日期 and记录状态 =0 如果查询出存在一条记录，就可以做0779交易。
		 */
		String jioyrq = jyrqService.queryJyrq();

		StringBuffer buff = new StringBuffer("select model from SqcsEO model where model.jiluzt=" + HdqsConstants.STATUS_NORMAL);
		buff.append(" and model.guiydh='" + jio1gy + "'");
		buff.append(" and model.qyngrq<='" + jioyrq + "'");
		buff.append(" and model.jshurq>='" + jioyrq + "'");

		List<SqcsEO> list = referDao.findByProperty(buff.toString(), 0, 0);
		if (list == null || list.isEmpty()) {
			return false;
		}
		return true;
	}

	public long getCountByProperty(Handle0780Form form) {
		StringBuffer buff = new StringBuffer("select count(model) from SqcsEO model where 1=1");
		if (HdqsUtils.isNotBlank(form.getGuiydh()))
			buff.append(" and model.guiydh='" + form.getGuiydh() + "'");
		if (HdqsUtils.isNotBlank(form.getGuiyxm()))
			buff.append(" and model.guiyxm='" + form.getGuiyxm() + "'");
		if (HdqsUtils.isNotBlank(form.getGylxdm()))
			buff.append(" and model.gylxdm='" + form.getGylxdm() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihrq()))
			buff.append(" and model.weihrq='" + form.getWeihrq() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihgy()))
			buff.append(" and model.weihgy='" + form.getWeihgy() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihxm()))
			buff.append(" and model.weihxm='" + form.getWeihxm() + "'");
		if (HdqsUtils.isNotBlank(form.getJiluzt()))
			buff.append(" and model.jiluzt='" + form.getJiluzt() + "'");

		long count = referDao.getCountByProperty(buff.toString());
		return count;
	}

}
