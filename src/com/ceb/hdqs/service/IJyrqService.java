package com.ceb.hdqs.service;

import javax.ejb.Local;

@Local
public interface IJyrqService {

	public String queryJyrq();

	public String saveOrUpdate();

	public String updateJyrq();

	public String forceUpdate(String jioyrq);

}