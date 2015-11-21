package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.field.VyktdField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 在0771,0772，和0777、0778中如果查询的出的客户账号的客户账号的类型是卡的，则查询VYKTD，查询出卡的发卡机构
 * 
 * @author user
 * 
 */
public class VyktdParser extends KhzhParser {
	private static final Log LOG = LogFactory.getLog(VyktdParser.class);

	public VyktdParser() {
		super(QueryConstants.TABLE_NAME_VYKTD);
	}

	public KehzhParserResult parseCondition(String kehuzh) throws IOException, ConditionNotExistException, Exception {
		if (StringUtils.isBlank(kehuzh)) {
			return null;
		}
		PybjyEO record = new PybjyEO();
		record.setKehuzh(kehuzh);
		LOG.debug("客户账号 =====：" + kehuzh);
		return parseCondition(record);
	}

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws IOException, ConditionNotExistException, Exception {
		List<ZhangHParseResult> cardParseResult = this.scan(buildParseScanner(record));
		if (cardParseResult == null || cardParseResult.isEmpty()) {
			KehzhParserResult khzhInfo = new KehzhParserResult();
			khzhInfo.setJIGOMC("");
			khzhInfo.setZHYYNG("");
			return khzhInfo;
		}
		ZhangHParseResult accParseResult = cardParseResult.get(0);
		String fkjgmc = QueryMethodUtils.getJGMC(accParseResult.getZHYYNG());

		KehzhParserResult khzhParseResult = new KehzhParserResult();
		khzhParseResult.setJIGOMC(fkjgmc);
		khzhParseResult.setZHYYNG(accParseResult.getZHYYNG());
		return khzhParseResult;
	}

	@Override
	protected ZhangHParseResult parse(Result result) throws IOException {
		ZhangHParseResult cardParseResult = new ZhangHParseResult();
		String yngyjg = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, VyktdField.YNGYJG));
		cardParseResult.setZHYYNG(yngyjg);
		LOG.debug("VYKTD查询出的账户营业机构是：" + yngyjg);
		return cardParseResult;
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		// 生成查询VYKTD的rowKey
		String cardNo = StringUtils.reverse(record.getKehuzh());
		Get get = new Get(Bytes.toBytes(cardNo));
		Scan scan = new Scan(get);
		try {
			LOG.debug(scan.toJSON());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scan;
	}

}
