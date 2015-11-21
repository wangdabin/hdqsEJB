package com.ceb.bank.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import com.ceb.bank.constants.ChaxzlEnum;
import com.ceb.bank.constants.HBaseQueryConstants;
import com.ceb.bank.constants.KemuccEnum;
import com.ceb.bank.constants.KhzhlxEnum;
import com.ceb.bank.constants.ZhjnzlEnum;
import com.ceb.bank.context.ChaxzlContext;
import com.ceb.bank.context.RZhanghContext;
import com.ceb.bank.context.RowkeyContext;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;

public final class HdqsCommonUtils {
	private static final Logger log = Logger.getLogger(HdqsCommonUtils.class);

	private HdqsCommonUtils() {
	}

	/**
	 * 如果CHAXZL是客户号或证件查询时,需要遍历包含的客户账号,传入KHZHLX和KEHUZH.
	 * 
	 * @param record
	 * @param startTime
	 * @param endTime
	 * @param rCtx
	 * @return
	 */
	public static String getBlxTips(PybjyEO record, String startTime, String endTime, RZhanghContext rCtx) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(getChaxzlBlxContext(record, rCtx));
		buffer.append("账户" + rCtx.getZhangh() + "在" + startTime + "到" + endTime + "之间余额不连续");
		return buffer.toString();
	}

	/**
	 * 如果CHAXZL是客户号或证件查询时,需要遍历包含的客户账号,传入KHZHLX和KEHUZH.
	 * 
	 * @param record
	 * @param rCtx
	 * @return
	 */
	public static String getChaxzlBlxContext(PybjyEO record, RZhanghContext rCtx) {
		StringBuffer buffer = new StringBuffer();
		try {
			ChaxzlEnum chaxzl = ChaxzlEnum.valueOf(ChaxzlEnum.PREFIX + record.getChaxzl());
			switch (chaxzl) {
			case CHAXZL1:
				break;
			case CHAXZL2:
				KhzhlxEnum khzhlx2 = KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + record.getKhzhlx());
				buffer.append(khzhlx2.getPair().getSecond() + record.getKehuzh());
				buffer.append("的");
				break;
			case CHAXZL3:
				buffer.append(chaxzl.getPair().getSecond() + record.getKehuhao() + "的");
				KhzhlxEnum khzhlx3 = KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + rCtx.getKhzhlx());
				buffer.append(khzhlx3.getPair().getSecond() + rCtx.getKehuzh());
				buffer.append("的");
				break;
			case CHAXZL4:
				ZhjnzlEnum zhjnzl = ZhjnzlEnum.valueOf(ZhjnzlEnum.PREFIX + record.getZhjnzl());
				buffer.append(zhjnzl.getDisplay() + record.getZhjhao() + "的");
				KhzhlxEnum khzhlx4 = KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + rCtx.getKhzhlx());
				buffer.append(khzhlx4.getPair().getSecond() + rCtx.getKehuzh());
				buffer.append("的");
				break;
			default:
				break;
			}
		} catch (java.lang.IllegalArgumentException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static String getNoRecordTips(PybjyEO record) {
		StringBuffer buffer = new StringBuffer();
		try {
			ChaxzlEnum chaxzl = ChaxzlEnum.valueOf(ChaxzlEnum.PREFIX + record.getChaxzl());
			switch (chaxzl) {
			case CHAXZL1:
				buffer.append(chaxzl.getPair().getSecond() + ":" + record.getZhangh());
				buffer.append(",在我行无开户记录.");
				break;
			case CHAXZL2:
				KhzhlxEnum khzhlx2 = KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + record.getKhzhlx());
				buffer.append(khzhlx2.getPair().getSecond() + ":" + record.getKehuzh());
				buffer.append(",在我行无开户记录.");
				break;
			case CHAXZL3:
				buffer.append(chaxzl.getPair().getSecond() + ":" + record.getKehuhao());
				String zwmTip = StringUtils.isBlank(record.getKhzwm()) ? "" : ",中文名:" + record.getKhzwm();
				buffer.append(zwmTip);
				buffer.append(",在我行无开户记录.");
				break;
			case CHAXZL4:
				// **证件类型**证件号码**户名,在我行无开户记录
				ZhjnzlEnum zhjnzl = ZhjnzlEnum.valueOf(ZhjnzlEnum.PREFIX + record.getZhjnzl());
				buffer.append("**" + zhjnzl.getDisplay() + "**" + record.getZhjhao());
				String suffixText = StringUtils.isBlank(record.getKhzwm()) ? "" : "**" + record.getKhzwm();
				buffer.append(suffixText);
				buffer.append(",在我行无开户记录");
				break;
			default:
				break;
			}
		} catch (java.lang.IllegalArgumentException e) {
			e.printStackTrace();
		}

		return buffer.toString();
	}

	public static String getNoItemsTips(PybjyEO record, String zhangh) {
		StringBuffer buffer = new StringBuffer();
		if (!HdqsConstants.CHAXZL_ZHANGH.equals(record.getChaxzl())) {
			ChaxzlContext chaxzlCtx = getChaxzlContext(record);
			buffer.append(chaxzlCtx.getDisplay() + chaxzlCtx.getValue() + "的");
		}
		buffer.append("账号" + zhangh + "不存在交易明细.");
		return buffer.toString();
	}

	public static ChaxzlContext getChaxzlContext(PybjyEO record) {
		ChaxzlContext context = new ChaxzlContext();
		try {
			ChaxzlEnum chaxzl = ChaxzlEnum.valueOf(ChaxzlEnum.PREFIX + record.getChaxzl());
			context.setKey(chaxzl.getPair().getFirst());
			context.setDisplay(chaxzl.getPair().getSecond());
			switch (chaxzl) {
			case CHAXZL1:
				context.setValue(record.getZhangh());
				break;
			case CHAXZL2:
				KhzhlxEnum khzhlx = KhzhlxEnum.valueOf(KhzhlxEnum.PREFIX + record.getKhzhlx());
				context.setValue(record.getKehuzh());
				context.setDisplay(khzhlx.getPair().getSecond());
				break;
			case CHAXZL3:
				context.setValue(record.getKehuhao());
				break;
			case CHAXZL4:
				context.setValue(record.getZhjhao());
				ZhjnzlEnum zhjnzl = ZhjnzlEnum.valueOf(ZhjnzlEnum.PREFIX + record.getZhjnzl());
				context.setDisplay(zhjnzl.getDisplay());
				break;
			default:
				break;
			}
		} catch (java.lang.IllegalArgumentException e) {
			context.setKey("");
			context.setValue("");
			context.setDisplay("");
		}

		return context;
	}

	/**
	 * 根据KEMUCC字段属性来判定需要查询的明细表名称
	 * 
	 * <PRE>
	 * **KEMUCC        科目存储
	 * S-对私活期
	 * C-对公活期
	 * F-对私定期
	 * E-对公定期
	 * V-贷款
	 * Q-欠息
	 * I-内部帐
	 * O-表外帐
	 * T-拆借贴现投资帐
	 * Y-存放同业主文件
	 * </PRE>
	 * 
	 * @param record
	 *            查询条件
	 * @param ctx
	 *            账号上下文
	 */
	public static void injectTblNameFromKemucc(PybjyEO record, ZhanghContext ctx) {
		KemuccEnum kemucc = KemuccEnum.valueOf(ctx.getKemucc());
		String tblName = null;
		switch (kemucc) {
		case C:
			if (!"2".equals(record.getZhaoxz())) {// 非定期
				tblName = HBaseQueryConstants.TABLE_AGHMX;
			}
			break;
		case E:
			if (!"0".equals(record.getZhaoxz())) {// 非活期
				tblName = HBaseQueryConstants.TABLE_AGDMX;
			}
			break;
		case S:
			if (!"2".equals(record.getZhaoxz())) {// 非定期
				tblName = HBaseQueryConstants.TABLE_ASHMX;
			}
			break;
		case F:
			if (!"0".equals(record.getZhaoxz())) {// 非活期
				tblName = HBaseQueryConstants.TABLE_ASDMX;
			}
			break;
		default:
			break;
		}
		ctx.setTableName(tblName);
	}

	public static void cachePageRowkeyAndSize(PybjyEO record, Map<Integer, List<RowkeyContext>> pageInfo, int pageNum, Result[] res) {
		int start = (pageNum - 1) * record.getQueryNum() + 1;
		byte[] rowKey = res[0].getRow();
		RowkeyContext rowkeyCtx = new RowkeyContext();
		rowkeyCtx.setRowkey(rowKey);
		rowkeyCtx.setPageCount(res.length);

		log.debug("页码：" + pageNum + ",起始条数：" + start + ",rowkey：" + Bytes.toString(rowKey));
		if (pageInfo.get(start) == null) {
			List<RowkeyContext> list = new ArrayList<RowkeyContext>();
			list.add(rowkeyCtx);
			pageInfo.put(start, list);
		} else {
			pageInfo.get(start).add(rowkeyCtx);
		}
	}
}