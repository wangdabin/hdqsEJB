package com.ceb.hdqs.service;

import javax.ejb.Local;

import com.ceb.hdqs.entity.GylsEO;

@Local
public interface IGylsService {

	public GylsEO genGuiyuanSerialNumber(String guiyuan);

	public String buildGuiyls(GylsEO entity);

	public String buildSlbhao(GylsEO entity);

	public String getJio1gyFromSlbhao(String handleNo);
}
