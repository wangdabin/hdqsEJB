package com.ceb.hdqs.wtc.file;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.wtc.HdqsWtcException;

public class AsynPrivateQuery extends AsynQuery {
	private static final Logger log = Logger.getLogger(AsynPrivateQuery.class);

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
		if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUZH)) {
			record.setKehuzh(contents[2].trim());
			record.setKhzhlx(contents[3].trim());
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_KEHUHAO)) {
			record.setKehuhao(contents[2].trim());
			record.setKhzwm(contents[4].trim());
		} else if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHJNZL)) {
			record.setZhjhao(contents[2].trim());
			record.setZhjnzl(contents[3].trim());
			record.setKhzwm(contents[4].trim());
		} else {
			throw new HdqsWtcException("line[" + lineNum + "]查询查询条件种类不正确");
		}
		record.setZhaoxz(contents[5].trim());
		record.setHuobdh(contents[6].trim());
		record.setStartDate(contents[7].trim());
		record.setEndDate(contents[8].trim());
		return record;
	}

	/**
	 * <pre>
	 * 第0字段：查询类型，0-客户查询，1-内部查询，2-监管查询 
	 * 第1字段：查询条件种类： 2：客户账号 3：客户号4：证件号
	 * 第2字段：查询条件内容：对应查询条件种类内容 
	 * 第3字段：如果是查询条件种类为4：代表身份证件类型,如果查询条件为2:代表客户账号类型：1-卡 2-活期一本通 3-定期一本通 4-储蓄存款存折 5-存单 6-国债 
	 * 第4字段：如果是查询条件种类为4：代表中文名 
	 * 第5字段：代表账号性质 0活期,2定期,3全部
	 * 第6字段：代表查询币种 ,货币代号(货币种类)01-人民币 12-英镑 13-港币 14-美元 00-全部
	 * 第7字段：开始日期 
	 * 第8字段：结束日期
	 * </pre>
	 */
	protected void validLine(String[] contents, int lineNum) throws HdqsWtcException {
		if (contents.length != 9) {
			throw new HdqsWtcException("line[" + lineNum + "]长度不正确");
		}
		if (HdqsUtils.isBlank(contents[0]) || HdqsUtils.isBlank(contents[1]) || HdqsUtils.isBlank(contents[2]) || HdqsUtils.isBlank(contents[5]) || HdqsUtils.isBlank(contents[6])
				|| HdqsUtils.isBlank(contents[7]) || HdqsUtils.isBlank(contents[8])) {
			throw new HdqsWtcException("line[" + lineNum + "]必填项不能为空");
		}
		String chaxlx = contents[0].trim();
		if (!chaxlx.equals("0") && !chaxlx.equals("1") && !chaxlx.equals("2")) {
			throw new HdqsWtcException("line[" + lineNum + "]输入不正确的查询类型");
		}
		String chaxzl = contents[1].trim();
		if (!chaxzl.equals("2") && !chaxzl.equals("3") && !chaxzl.equals("4")) {
			throw new HdqsWtcException("line[" + lineNum + "]输入不正确的查询条件种类");
		}

		if (chaxzl.equals(HdqsConstants.CHAXZL_KEHUZH)) {
			String khzhlx = contents[3].trim();
			if (HdqsUtils.isBlank(khzhlx)) {
				throw new HdqsWtcException("line[" + lineNum + "]客户账号类型不能为空");
			}
			int khzhlxInt = Integer.parseInt(khzhlx);
			if (khzhlxInt < 1 || khzhlxInt > 6) {
				throw new HdqsWtcException("line[" + lineNum + "]输入不正确的客户账号类型");
			}

		} else if (chaxzl.equals(HdqsConstants.CHAXZL_ZHJNZL)) {
			if (HdqsUtils.isBlank(contents[3].trim())) {
				throw new HdqsWtcException("line[" + lineNum + "]身份证件类型不能为空");
			}
			//deleted by chengqi
//			String zhjnzl = contents[3].trim();
//			if (zhjnzl.equals("1")) {// 身份证
//				int length = contents[2].trim().length();// 身份证号码有15位和18位，有时候会在身份证后添加一个字母
//				if (length > 20 || length < 15) {
//					throw new HdqsWtcException("line[" + lineNum + "]身份证件号长度不正确");
//				}
//			}
		}
		String zhaoxz = contents[5].trim();
		if (!zhaoxz.equals("0") && !zhaoxz.equals("2") && !zhaoxz.equals("3")) {
			throw new HdqsWtcException("line[" + lineNum + "]输入不正确的账号性质");
		}

		if (!HdqsUtils.validateDate(contents[7], contents[8])) {
			throw new HdqsWtcException("line[" + lineNum + "]查询日期格式不正确");
		}
		ConfigLoader.fitTimeLimit(contents[8], contents[7], "line[" + lineNum + "]");
	}
}