package com.ceb.hdqs.service;

import java.util.List;

import javax.ejb.Remote;

import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.wtc.form.Handle0774Form;

@Remote
public interface AuthorizeService {
	public void save(Handle0774Form form) throws HdqsServiceException;

	public void delete(Handle0774Form form) throws HdqsServiceException;

	public SqbmEO update(Handle0774Form form) throws HdqsServiceException;

	public SqbmEO findByAuthorizedCode(String szkmbm, String qudaoo);

	public long getCountByProperty(Handle0774Form form);

	public List<SqbmEO> findByProperty(Handle0774Form form);

	public List<SqbmEO> findByProperty(Handle0774Form form, int startIdx, int count);

	public List<SqbmEO> findAll();
}
