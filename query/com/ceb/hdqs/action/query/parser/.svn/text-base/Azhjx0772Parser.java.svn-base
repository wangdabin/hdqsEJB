package com.ceb.hdqs.action.query.parser;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.entity.field.AzhjxField;
import com.ceb.hdqs.entity.result.KehzhParserResult;
import com.ceb.hdqs.entity.result.ZhangHParseResult;
import com.ceb.hdqs.query.entity.EnumQueryKind;
import com.ceb.hdqs.query.entity.SkyPair;
import com.ceb.hdqs.query.utils.QueryConstants;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

/**
 * 在0771,0772获取账户营业机构的过程中，查询AZHJX0772的账号进行查询
 * 
 * @author user
 * 
 */
public class Azhjx0772Parser extends AbstractQuery<KehzhParserResult> {
	private static final Log LOG = LogFactory.getLog(Azhjx0772Parser.class);

	public Azhjx0772Parser() {
		super(QueryConstants.TABLE_NAME_AZHJX0772);
	}

	/**
	 * 添加账户营业机构和账户营业机构名称
	 * 
	 * @param khzhParseResult
	 * @throws IOException
	 */
	public void addKhzhParseInfo(KehzhParserResult khzhParseResult) throws IOException {
		Map<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghParse = khzhParseResult.getZhanghParseResult();
		HTableInterface table = null;
		try {
			table = this.getHTable();
			for (Entry<SkyPair<String, EnumQueryKind>, ZhangHParseResult> zhanghResult : zhanghParse.entrySet()) {
				ZhangHParseResult parseZhangh = zhanghResult.getValue();

				String zhangh = StringUtils.reverse(parseZhangh.getZHANGH());
				Scan scan = new Scan();
				scan.setStartRow(Bytes.toBytes(zhangh + "|#"));
				scan.setStopRow(Bytes.toBytes(zhangh + "|~"));
				LOG.debug("需要解析的账号是：" + scan.toJSON());
				ResultScanner scanner = table.getScanner(scan);
				Result res = scanner.next();
				if (res != null) {
					String zhyyng = Bytes.toString(res.getValue(QueryConstants.HBASE_TABLE_FAMILY_BYTE,
							AzhjxField.ZHYYJG));
					String jgmc = QueryMethodUtils.getJGMC(zhyyng);

					LOG.debug("账号" + parseZhangh.getZHANGH() + "解析出的账户营业机构号是 " + zhyyng + " 名称是：" + jgmc);

					parseZhangh.setZHYYNG(zhyyng);
					parseZhangh.setJIGOMC(jgmc);
					khzhParseResult.setZHYYNG(zhyyng);
					khzhParseResult.setJIGOMC(jgmc);
				} else {
					LOG.debug("账号的营业机构为空!");
					continue;
				}
			}
		} finally {
			if (table != null) {
				this.release(table);
			}
		}
	}

	@Override
	protected KehzhParserResult parse(Result result) throws IOException {
		return null;
	}

}
