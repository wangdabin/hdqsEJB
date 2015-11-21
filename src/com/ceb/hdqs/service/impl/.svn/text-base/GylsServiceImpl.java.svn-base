package com.ceb.hdqs.service.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.ceb.hdqs.dao.GylsDao;
import com.ceb.hdqs.entity.GylsEO;
import com.ceb.hdqs.service.GylsService;
import com.ceb.hdqs.service.IGylsService;
import com.ceb.hdqs.service.IJyrqService;

@Stateless(mappedName = "GylsService")
public class GylsServiceImpl implements IGylsService, GylsService {
	private static final int END_INDEX = 14;
	private static final int START_INDEX = 8;
	@EJB
	GylsDao referDao;
	@EJB
	IJyrqService jyrqService;

	public GylsEO genGuiyuanSerialNumber(String guiyuan) {
		String gyStr = guiyuan.substring(0, 6);
		String exDate = jyrqService.queryJyrq();

		GylsEO existObj = referDao.findByGuiyuanAndCreatedT(gyStr, exDate);
		if (existObj == null) {
			GylsEO newObj = new GylsEO();
			newObj.setGuiyuan(gyStr);
			newObj.setExDate(exDate);
			newObj.setSeq(1);
			return referDao.save(newObj);
		}
		existObj.setSeq(existObj.getSeq() + 1);
		referDao.update(existObj);

		return existObj;
	}

	public String buildGuiyls(GylsEO entity) {
		return entity.getGuiyuan() + String.format("%04d", entity.getSeq());
	}

	public String buildSlbhao(GylsEO entity) {
		return entity.getExDate() + entity.getGuiyuan() + String.format("%04d", entity.getSeq());
	}

	public String getJio1gyFromSlbhao(String handleNo) {
		return handleNo.substring(START_INDEX, END_INDEX);
	}
}