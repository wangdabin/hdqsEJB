package com.ceb.hdqs.service;

import java.util.List;

import javax.ejb.Local;

import com.ceb.hdqs.entity.SqcsEO;
import com.ceb.hdqs.wtc.form.Handle0780Form;

@Local
public interface ISqcsService {
	public void save(Handle0780Form form) throws HdqsServiceException;

	public void delete(Handle0780Form form) throws HdqsServiceException;

	public SqcsEO update(Handle0780Form form) throws HdqsServiceException;

	public List<SqcsEO> findByProperty(Handle0780Form form);

	public List<SqcsEO> findByProperty(Handle0780Form form, int startIdx, int count);

	public List<SqcsEO> findAll();

	public boolean authorize(String jio1gy);

	public long getCountByProperty(Handle0780Form form);
}