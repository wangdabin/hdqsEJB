package com.ceb.hdqs.service;

import javax.ejb.Remote;

@Remote
public interface JyrqService {
	public String queryJyrq();

	public String saveOrUpdate();

	public String updateJyrq();

	public String forceUpdate(String jioyrq);

}