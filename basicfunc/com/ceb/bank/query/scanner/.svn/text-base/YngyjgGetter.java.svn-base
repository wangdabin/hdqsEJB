package com.ceb.bank.query.scanner;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.context.YngyjgContext;
import com.ceb.bank.hfield.HVyktdField;
import com.ceb.bank.htable.AbstractQuery;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class YngyjgGetter extends AbstractQuery<String> {

	public YngyjgGetter() {
		super(HBaseQueryConstants.TABLE_VYKTD);
	}

	public YngyjgContext query(String kehuzh) throws IOException {
		String yngyjg = doGet(buildGet(kehuzh));
		if (StringUtils.isBlank(yngyjg)) {
			return null;
		}
		String jigomc = QueryMethodUtils.getJGMC(yngyjg);

		YngyjgContext context = new YngyjgContext();
		context.setYngyjg(yngyjg);
		context.setJigomc(jigomc);
		return context;
	}

	@Override
	protected String parse(Result result) throws IOException {
		String yngyjg = Bytes.toString(result.getValue(HBaseQueryConstants.FAMILIER_BYTE_F, HVyktdField.YNGYJG));
		return yngyjg;
	}

	private Get buildGet(String kehuzh) {
		Get get = new Get(Bytes.toBytes(StringUtils.reverse(kehuzh)));
		return get;
	}
}