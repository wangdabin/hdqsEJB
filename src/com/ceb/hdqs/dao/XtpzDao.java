package com.ceb.hdqs.dao;

import java.util.List;

import com.ceb.hdqs.entity.XtpzEO;

public interface XtpzDao {
	public void save(XtpzEO entity);

	public void delete(Long id);

	public XtpzEO update(XtpzEO entity);

	public XtpzEO findRecordById(long id);

	public XtpzEO findRecordByName(String name);

	public List<XtpzEO> findAll();
}