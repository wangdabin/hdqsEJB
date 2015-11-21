package com.ceb.hdqs.action.query.parser;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.hdqs.action.asyn.AsynQuery0778;
import com.ceb.hdqs.action.query.IconditionProcessor;
import com.ceb.hdqs.action.query.exception.ConditionNotExistException;
import com.ceb.hdqs.action.query.exception.InvalidRecordParameterException;
import com.ceb.hdqs.action.query0771.HandleQuery0771;
import com.ceb.hdqs.action.query0772.HandleQuery0772;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 解析0778业务的客户账号 0778对私业务{@link AsynQuery0778}输入条件为客户账号时
 * <p>
 * 解析规则：
 * <p>
 * 首先根据输入的客户账号查询AKHZH，查询出客户账号的类型、对应的账号，然后根据账号查询解析AZHJX查询出账号的账户营业机构（
 * 如果查询的客户账号类型是卡，则根据客户账号查询VTKTD查询发卡机构名称）；通过查询AZHJX查询出每个账号的Kemucc确定是活期查询还是定期查询。
 * 当输入的账户性质是活期时，查询流程根据对私活期查询{@link HandleQuery0771}进行查询
 * 当输入的账户性质是定期时，查询流程根据对私定期查询{@link HandleQuery0772}进行查询
 * 
 * @author user
 * 
 */
public class KhzhParserOf0778 implements IconditionProcessor {
	private static final Log LOG = LogFactory.getLog(KhzhParserOf0778.class);

	private KhzhParserAsyn khzhParser;

	public KhzhParserOf0778() {
		khzhParser = new KhzhParserAsyn() {
			@Override
			public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
				// TODO Auto-generated method stub
				return KhzhParserOf0778.this.buildParseScanner(record);
			}
		};
	}

	@Override
	public Scan buildParseScanner(PybjyEO record) throws InvalidRecordParameterException {
		LOG.debug("开始生成解析的Scanner");
		String rowKeySplit = QueryConfUtils.getActiveConfig().get(QueryConstants.ROWKEY_SPLITTER, "|");
		String rowkey = buildKhzhParserRowKey(record);
		Scan scanner = new Scan();
		scanner.setStartRow(Bytes.toBytes(rowkey + rowKeySplit + "#"));
		scanner.setStopRow(Bytes.toBytes(rowkey + rowKeySplit + ":"));
		/*
		 * 开始设置货币币种
		 */
		if (!record.getHuobdh().equals("00")) {
			String huobdh = record.getHuobdh();
			LOG.debug("需要查询的货币代号是：" + huobdh);
			SingleColumnValueFilter huobiFilter = new SingleColumnValueFilter(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
					Bytes.toBytes("HUOBDH"), CompareOp.EQUAL, Bytes.toBytes(huobdh));
			FilterList list = new FilterList();
			list.addFilter(huobiFilter);
			scanner.setFilter(list);
		}
		// 打印scanner测试生成的Scan对象是否正确
		try {
			LOG.debug(scanner.toJSON());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scanner;
	}

	/**
	 * 生成解析客户账号的rowkey
	 * 
	 * @param condation
	 * @return
	 */
	private String buildKhzhParserRowKey(PybjyEO condation) {
		String account = StringUtils.reverse(condation.getKehuzh());
		return account;
	}

	@Override
	public KehzhParserResult parseCondition(PybjyEO record) throws IOException, ConditionNotExistException, Exception {
		// 首先判断输入的是否是存单系列
		if (record.getKhzhlx().equals(QueryConstants._0772_KHZHLX_CUNDAN) || record.getKhzhlx().equals(QueryConstants._0772_KHZHLX_CXCKCZ)
				|| record.getKhzhlx().equals(QueryConstants._0772_KHZHLX_GUOZHAI)) {
			// 如果输入的客户账号类型是存单、国债、储蓄存款存折则查询表AZHJX_0772进行解析
			LOG.info("输入条件属于存单、国债、储蓄存款存折，进入Khzh_zhParser过程");
			// doSmothing to Parse zhangh
			Khzh_zhParser parser = new Khzh_zhParser();

			// parser.parseCondition(condition);
			return parser.parseCondition(record);
		} else {
			return khzhParser.parseCondition(record);
		}

	}
}
