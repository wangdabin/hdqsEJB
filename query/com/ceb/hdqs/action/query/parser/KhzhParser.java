package com.ceb.hdqs.action.query.parser;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;

/**
 * 查询账户解析表返回对应的字段信息，默认情况下的ParseCondation直接为0777,0778提供解析服务
 * 该类的子类中单独实现了解析0771和0772业务的ParseCondation业务
 * 
 * @author user
 * 
 */
public abstract class KhzhParser extends AbstractQuery<ZhangHParseResult> implements IconditionProcessor {

	public KhzhParser(String tableName) {
		super(tableName);
	}

	@Override
	protected abstract ZhangHParseResult parse(Result result) throws IOException;

	@Override
	public abstract KehzhParserResult parseCondition(PybjyEO record) throws IOException,
			ConditionNotExistException, Exception;

	@Override
	public abstract Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException;

}