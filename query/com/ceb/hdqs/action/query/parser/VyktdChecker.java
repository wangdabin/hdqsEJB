package com.ceb.hdqs.action.query.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.UnCorporationCardException;
import com.ceb.hdqs.entity.field.VyktdField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 检查是否是单位卡
 * 
 * @author user
 * 
 */
public class VyktdChecker extends AbstractQuery<KehzhParserResult> {
	/**
	 * **KAAADX 卡对象 0-个人卡 1-单位卡
	 */
	public static final String KAAADX_S = "0";
	public static final String KAAADX_G = "1";

	public VyktdChecker() {
		super(QueryConstants.TABLE_NAME_VYKTD);
	}

	@Override
	protected KehzhParserResult parse(Result result) throws IOException {
		String YNGYJG = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, VyktdField.YNGYJG));
		String KAAADX = Bytes.toString(result.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE, VyktdField.KAAADX));

		KehzhParserResult obj = new KehzhParserResult();
		obj.setZHYYNG(YNGYJG);
		obj.setKaaadx(KAAADX);
		obj.setJIGOMC(QueryMethodUtils.getJGMC(YNGYJG));
		return obj;
	}

	/**
	 * 验证是否是单位卡
	 * 
	 * @param cardNo
	 * @param result
	 * @return
	 * @throws IOException
	 * @throws ConditionNotExistException
	 */
	public KehzhParserResult validCard(String cardNo) throws IOException, ConditionNotExistException, UnCorporationCardException {
		KehzhParserResult obj = this.doGet(StringUtils.reverse(cardNo.trim()));

		if (obj == null) {
			throw new ConditionNotExistException("卡号" + cardNo + "不存在!");
		} else if (!KAAADX_G.equals(obj.getKaaadx())) {
			throw new UnCorporationCardException("输入的卡号非单位卡!");
		}

		return obj;
	}
}