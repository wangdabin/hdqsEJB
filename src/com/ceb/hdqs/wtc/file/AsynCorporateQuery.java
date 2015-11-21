package com.ceb.hdqs.wtc.file;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.HdqsWtcException;

public class AsynCorporateQuery extends AsynQuery {
	private static final Logger log = Logger.getLogger(AsynCorporateQuery.class);

	protected PybjyEO initRecord(String[] contents, int lineNum, PybjyEO entity) throws HdqsWtcException {
		PybjyEO record = new PybjyEO();
		try {
			BeanUtils.copyProperties(record, entity);
			/**
			 * Caused by: <openjpa-1.1.1-SNAPSHOT-r422266:965591 nonfatal store
			 * error> org.apache.openjpa.persistence.EntityExistsException:
			 * Attempt to persist detached object
			 * "com.ceb.hdqs.entity.AsyQueryRecord@4444ad54". If this is a new
			 * instance, make sure any versino and/or auto-generated primary key
			 * fields are null/default when persisting.
			 */
			record.setId(null);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
		record.setChaxlx(contents[0].trim());
		record.setChaxzl(contents[1].trim());
		
		if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHANGH)) {
			record.setZhangh(contents[2].trim());
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUZH)) {
			record.setKehuzh(contents[2].trim());
			record.setKhzhlx(HdqsConstants.KHZHLX_DUIGONGYIHAOTONG);
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUHAO)) {
			record.setKehuhao(contents[2].trim());
		} else {
			throw new HdqsWtcException("line[" + lineNum + "]查询条件种类不正确");
		}
		record.setStartDate(contents[3].trim());
		record.setEndDate(contents[4].trim());

		return record;
	}

	/**
	 * <pre>
	 * 第0字段：查询类型，0-客户查询，1-内部查询，2-监管查询 （必输）
	 * 第1字段：查询条件种类： 1：账号 2：对公一号通号 3：客户号 （必输）
	 * 第2字段：查询条件内容：对应查询条件种类内容（必输）
	 * 第3字段：开始日期（必输）
	 * 第4字段：结束日期（必输）
	 * </pre>
	 */
	protected void validLine(String[] contents, int lineNum) throws HdqsWtcException {
		if (contents.length != 5) {
			throw new HdqsWtcException("line[" + lineNum + "]长度不正确");
		}
		if (HdqsUtils.isBlank(contents[0]) || HdqsUtils.isBlank(contents[1]) || HdqsUtils.isBlank(contents[2]) || HdqsUtils.isBlank(contents[3]) || HdqsUtils.isBlank(contents[4])) {
			throw new HdqsWtcException("line[" + lineNum + "]必填项不能为空");
		}
		String chaxlx = contents[0].trim();
		if (!chaxlx.equals("0") && !chaxlx.equals("1") && !chaxlx.equals("2")) {
			throw new HdqsWtcException("line[" + lineNum + "]输入不正确的查询类型");
		}
		String chaxzl = contents[1].trim();
		if (!chaxzl.equals("1") && !chaxzl.equals("2") && !chaxzl.equals("3")) {
			throw new HdqsWtcException("line[" + lineNum + "]输入不正确的查询条件种类");
		}
		if (!HdqsUtils.validateDate(contents[3], contents[4])) {
			throw new HdqsWtcException("line[" + lineNum + "]查询日期格式不正确");
		}
		ConfigLoader.fitTimeLimit(contents[4], contents[3], "line[" + lineNum + "]");
	}
}