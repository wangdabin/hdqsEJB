package com.ceb.hdqs.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.dao.SqbmDao;
import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.service.AuthorizeService;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.IAuthorizeService;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.form.Handle0774Form;

@Stateless(mappedName = "AuthorizeService")
public class AuthorizeServiceImpl implements IAuthorizeService, AuthorizeService {
	private static final Logger log = Logger.getLogger(AuthorizeServiceImpl.class);
	@EJB
	SqbmDao referDao;

	public void save(Handle0774Form form) throws HdqsServiceException {
		SqbmEO existObj = referDao.findBySzkmbmAndQudaoo(form.getSzkmbm(), form.getQudaoo());
		if (existObj != null) {
			throw new HdqsServiceException("授权编码和渠道存在,不允许重复增加");
		}
		SqbmEO entity = new SqbmEO();
		try {
			BeanUtils.copyProperties(entity, form);
			if (HdqsUtils.isNotBlank(form.getFormjb())) {
				entity.setGuiyjb(Integer.parseInt(form.getFormjb()));
			} else {
				entity.setGuiyjb(0);
			}
			referDao.save(entity);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void delete(Handle0774Form form) throws HdqsServiceException {
		SqbmEO entity = referDao.findById(Long.parseLong(form.getHandid()));
		if (entity == null) {
			throw new HdqsServiceException("待删除对象不存在或已经删除");
		}
		if (entity.getJiluzt().equals(HdqsConstants.STATUS_UNUSE)) {
			throw new HdqsServiceException("待删除对象已处于删除状态,不允许重复删除");
		}
		entity.setWeihrq(form.getWeihrq());
		entity.setWeihgy(form.getWeihgy());
		entity.setWeihxm(form.getWeihxm());
		entity.setJiluzt(HdqsConstants.STATUS_UNUSE);
		referDao.update(entity);
	}

	/**
	 * 授权编码和渠道是唯一索引不允许修改
	 */
	public SqbmEO update(Handle0774Form form) throws HdqsServiceException {
		SqbmEO entity = referDao.findById(Long.parseLong(form.getHandid()));
		if (entity == null) {
			throw new HdqsServiceException("待修改对象不存在或已经删除");
		}
		if (!form.getSzkmbm().equals(entity.getSzkmbm()) || !form.getQudaoo().equals(entity.getQudaoo())) {
			throw new HdqsServiceException("授权编码和渠道是唯一索引,不允许修改");
		}
		try {
			BeanUtils.copyProperties(entity, form);
			if (HdqsUtils.isNotBlank(form.getFormjb())) {
				entity.setGuiyjb(Integer.parseInt(form.getFormjb()));
			} else {
				entity.setGuiyjb(0);
			}
			// entity.setJiluzt(HdqsConstants.STATUS_NORMAL);
			referDao.update(entity);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}

		return entity;
	}

	public SqbmEO findByAuthorizedCode(String szkmbm, String qudaoo) {
		return referDao.findByAuthorizedCode(szkmbm, qudaoo, HdqsConstants.STATUS_NORMAL);
	}

	public long getCountByProperty(Handle0774Form form) {
		StringBuffer buff = new StringBuffer("select count(model)");
		buildQueryStr(form, buff);
		long count = referDao.getCountByProperty(buff.toString());
		return count;
	}

	public List<SqbmEO> findByProperty(Handle0774Form form) {
		return this.findByProperty(form, 0, 0);
	}

	public List<SqbmEO> findByProperty(Handle0774Form form, int startIdx, int count) {
		StringBuffer buff = new StringBuffer("select model");
		buildQueryStr(form, buff);
		buff.append(" order by model.id asc");
		List<SqbmEO> list = referDao.findByProperty(buff.toString(), startIdx, count);
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list;
	}

	private void buildQueryStr(Handle0774Form form, StringBuffer buff) {
		buff.append(" from SqbmEO model where 1=1");
		if (HdqsUtils.isNotBlank(form.getSzkmbm()))
			buff.append(" and model.szkmbm='" + form.getSzkmbm() + "'");
		if (HdqsUtils.isNotBlank(form.getFormjb()))
			buff.append(" and model.guiyjb='" + form.getFormjb() + "'");
		if (HdqsUtils.isNotBlank(form.getBeizxx()))
			buff.append(" and model.beizxx='" + form.getBeizxx() + "'");
		if (HdqsUtils.isNotBlank(form.getQudaoo()))
			buff.append(" and model.qudaoo='" + form.getQudaoo() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihrq()))
			buff.append(" and model.weihrq='" + form.getWeihrq() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihgy()))
			buff.append(" and model.weihgy='" + form.getWeihgy() + "'");
		if (HdqsUtils.isNotBlank(form.getBeiy04()))
			buff.append(" and model.beiy04='" + form.getBeiy04() + "'");
		if (HdqsUtils.isNotBlank(form.getWeihxm()))
			buff.append(" and model.weihxm='" + form.getWeihxm() + "'");
		if (HdqsUtils.isNotBlank(form.getJiluzt()))
			buff.append(" and model.jiluzt='" + form.getJiluzt() + "'");
	}

	public List<SqbmEO> findAll() {
		List<SqbmEO> list = referDao.findAll();
		if (list == null || list.isEmpty()) {
			return Collections.emptyList();
		}
		return list;
	}
}