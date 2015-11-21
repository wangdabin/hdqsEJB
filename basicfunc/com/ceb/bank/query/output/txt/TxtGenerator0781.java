package com.ceb.bank.query.output.txt;

import java.util.List;

import com.ceb.bank.context.OutPdfContext;
import com.ceb.bank.context.Output0781Context;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0781;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.DbcSbcUtils;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class TxtGenerator0781 extends TxtGenerator<Item0781> {

	@Override
	public void writeNoItems(PybjyEO record, OutPdfContext pageCtx, Object zhCtx) throws Exception {
		init(record);
		if (zhCtx == null) {
			write(record, pageCtx);
		} else {
			write(record, pageCtx, null, zhCtx);
		}
		appendLastTail(pageCtx);
		close();
	}

	private void write(PybjyEO record, OutPdfContext ctx) {
		Output0781Context pageCtx = (Output0781Context) ctx;
		getPrinter().printf("%101s\r\n\r\n", "中国光大银行单位卡对账单(本对帐单仅供参考)");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "客户姓名:", 82);
		DbcSbcUtils.fixLeftPrint(getPrinter(), "对账日期:" + pageCtx.getCxqsrq() + "-" + pageCtx.getCxzzrq(), 50);

		getPrinter().printf("\r\n");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "发卡/折机构:", 82);
		DbcSbcUtils.fixLeftPrint(getPrinter(), "打印时间:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 50);
		getPrinter().printf("\r\n");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "客户账号:" + record.getKehuzh(), 82);
		DbcSbcUtils.fixLeftPrint(getPrinter(), "系统账号:", 50);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "币种:", 55);
		DbcSbcUtils.fixRightPrint(getPrinter(), "钞汇标志:", 34);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		getPrinter().printf("%1s%-17s%5s%7s%20s%18s%18s%17s%43s%12s\r\n", "", "客户账号", "交易日期", "交易地点", "存入金额", "转出金额", "账户余额", "摘要", "对方账号",
				"对方名称");
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (String tip : pageCtx.getTips()) {
			getPrinter().printf(tip);
			getPrinter().printf("\r\n");
		}
	}

	@Override
	public void write(PybjyEO record, OutPdfContext ctx, List<Item0781> itemList, Object zhCtx) {
		Output0781Context pageCtx = (Output0781Context) ctx;
		ZhanghContext zhanghCtx = (ZhanghContext) zhCtx;
		getPrinter().printf("%101s\r\n\r\n", "中国光大银行单位卡对账单(本对帐单仅供参考)");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "客户姓名:" + zhanghCtx.getKehzwm(), 82);
		DbcSbcUtils.fixLeftPrint(getPrinter(), "对账日期:" + pageCtx.getCxqsrq() + "-" + pageCtx.getCxzzrq(), 50);

		getPrinter().printf("\r\n");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "发卡/折机构:" + zhanghCtx.getYngyjg() + " " + zhanghCtx.getJigomc(), 82);
		DbcSbcUtils.fixLeftPrint(getPrinter(), "打印时间:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 50);
		getPrinter().printf("\r\n");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "客户账号:" + zhanghCtx.getKehuzh(), 82);
		DbcSbcUtils.fixLeftPrint(getPrinter(), "系统账号:" + zhanghCtx.getZhangh(), 50);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "币种:" + QueryMethodUtils.huobdhFormat(zhanghCtx.getHuobdh()), 55);
		DbcSbcUtils.fixRightPrint(getPrinter(), "钞汇标志:" + QueryMethodUtils.chuibzFormat(zhanghCtx.getChuibz()), 34);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		getPrinter().printf("%1s%-17s%5s%7s%20s%18s%18s%17s%43s%12s\r\n", "", "客户账号", "交易日期", "交易地点", "存入金额", "转出金额", "账户余额", "摘要", "对方账号",
				"对方名称");
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		if (itemList == null || itemList.isEmpty()) {
			String tips = HdqsCommonUtils.getNoItemsTips(record, zhanghCtx.getZhangh());
			getPrinter().printf(tips);
			getPrinter().printf("\r\n");
		} else {
			String huobdh = zhanghCtx.getHuobdh();
			for (Item0781 item : itemList) {
				getPrinter().printf("%1s", "");
				getPrinter().printf("%-21s", zhanghCtx.getKehuzh());

				getPrinter().printf("%1s", "");
				getPrinter().printf("%8s", item.getJioyrq());
				DbcSbcUtils.fixLeftPrint(getPrinter(), "", 6);
				if (item.getJiedbz().equals("1")) {
					getPrinter().printf("%1s%21s%1s%21s", "", QueryMethodUtils.decimalFormat(item.getJio1je(), huobdh), "", "0.00");
				} else {
					getPrinter().printf("%1s%21s%1s%21s", "", "0.00", "", QueryMethodUtils.decimalFormat(item.getJio1je(), huobdh));
				}

				getPrinter().printf("%1s", "");
				getPrinter().printf("%21s", QueryMethodUtils.decimalFormat(item.getZhhuye(), huobdh));
				DbcSbcUtils.fixLeftPrint(getPrinter(), item.getZhyodm(), 16);
				DbcSbcUtils.fixRightPrint(getPrinter(), item.getDuifzh(), 16);
				DbcSbcUtils.fixLeftPrint(getPrinter(), item.getDuifmc(), 12);
				getPrinter().printf("\r\n\r\n");
			}
		}
	}

	@Override
	public void appendPageTail(OutPdfContext ctx) {
		Output0781Context pageCtx = (Output0781Context) ctx;
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		getPrinter().printf("%2s%2s", "", "柜员:" + pageCtx.getJio1gy());
		getPrinter().printf("%179s\r\n", "第" + pageCtx.getPageNum() + "页");
		getPrinter().println(PAGER_SWITCHOR);
	}

	@Override
	public void appendZhanghTail(OutPdfContext ctx) {
		Output0781Context pageCtx = (Output0781Context) ctx;
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 添加当前账号统计信息
		DbcSbcUtils.fixRightPrint(getPrinter(), "账户笔数：" + pageCtx.getCurrentTotal() + " 账户页数：" + pageCtx.getCurrentPageNum(), 96);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");

		getPrinter().printf("%2s%2s", "", "柜员:" + pageCtx.getJio1gy());
		getPrinter().printf("%179s\r\n", "第" + pageCtx.getPageNum() + "页");
		getPrinter().println(PAGER_SWITCHOR);
	}

	@Override
	public void appendLastTail(OutPdfContext ctx) {
		Output0781Context pageCtx = (Output0781Context) ctx;
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 添加当前账号统计信息
		DbcSbcUtils.fixRightPrint(getPrinter(), "账户笔数：" + pageCtx.getCurrentTotal() + " 账户页数：" + pageCtx.getCurrentPageNum(), 96);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");

		// 添加当前账号的上级条件的统计信息
		DbcSbcUtils.fixRightPrint(getPrinter(),
				"总账户数:" + pageCtx.getZhanghNum() + " 总笔数:" + pageCtx.getTotal() + " 总页数:" + pageCtx.getPageNum(), 96);
		getPrinter().printf("\r\n\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"========================================================================对帐单流水打印结束==========================最后一页=============================================================================");

		getPrinter().printf("%2s%2s", "", "柜员:" + pageCtx.getJio1gy());
		getPrinter().printf("%179s\r\n", "第" + pageCtx.getPageNum() + "页");
		getPrinter().println(PAGER_SWITCHOR);
	}
}