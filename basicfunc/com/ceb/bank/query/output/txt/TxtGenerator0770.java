package com.ceb.bank.query.output.txt;

import java.util.List;

import com.ceb.bank.context.OutPdfContext;
import com.ceb.bank.context.Output0770Context;
import com.ceb.bank.context.ZhanghContext;
import com.ceb.bank.item.Item0770;
import com.ceb.bank.utils.HdqsCommonUtils;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.DbcSbcUtils;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class TxtGenerator0770 extends TxtGenerator<Item0770> {
	@Override
	public void writeNoItems(PybjyEO record, OutPdfContext pageCtx, Object zhCtx) throws Exception {
		init(record);
		if (zhCtx == null) {
			write(pageCtx);
		} else {
			write(record, pageCtx, null, zhCtx);
		}
		appendZhanghTail(pageCtx);
		close();
	}

	private void write(OutPdfContext ctx) {
		Output0770Context pageCtx = (Output0770Context) ctx;
		getPrinter().printf("%103s\r\n\r\n", "中国光大银行对公帐户对账单(本对账单仅供参考)");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "账户名称:", 69);
		DbcSbcUtils.fixRightPrint(getPrinter(), "对账日期:" + pageCtx.getCxqsrq() + "-" + pageCtx.getCxzzrq(), 28);
		getPrinter().printf("\r\n");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "账    号:", 69);
		DbcSbcUtils.fixRightPrint(getPrinter(), "打印时间:" + QueryMethodUtils.formatDateAndTime(pageCtx.getPrintDate()), 28);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");

		getPrinter().printf("%5s%9s%11s%11s%17s%17s%20s%5s%29s%23s\r\n", "交易日期", "摘要", "凭证种类", "凭证序号", "借方发生额", "贷方发生额", "余额", "交易柜员",
				"对方账号", "对方名称");
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		for (String tip : pageCtx.getTips()) {
			getPrinter().printf(tip);
			getPrinter().printf("\r\n");
		}
	}

	@Override
	public void write(PybjyEO record, OutPdfContext ctx, List<Item0770> itemList, Object zhCtx) {
		Output0770Context pageCtx = (Output0770Context) ctx;
		ZhanghContext zhanghCtx = (ZhanghContext) zhCtx;
		getPrinter().printf("%103s\r\n\r\n", "中国光大银行对公帐户对账单(本对账单仅供参考)");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "账户名称:" + zhanghCtx.getKehzwm(), 69);
		DbcSbcUtils.fixRightPrint(getPrinter(), "对账日期:" + pageCtx.getCxqsrq() + "-" + pageCtx.getCxzzrq(), 28);
		getPrinter().printf("\r\n");
		getPrinter().printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(getPrinter(), "账    号:" + pageCtx.getZhangh(), 69);
		DbcSbcUtils.fixRightPrint(getPrinter(), "打印时间:" + QueryMethodUtils.formatDateAndTime(pageCtx.getPrintDate()), 28);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");

		getPrinter().printf("%5s%9s%11s%11s%17s%17s%20s%5s%29s%23s\r\n", "交易日期", "摘要", "凭证种类", "凭证序号", "借方发生额", "贷方发生额", "余额", "交易柜员",
				"对方账号", "对方名称");
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		if (itemList == null || itemList.isEmpty()) {
			String tips = HdqsCommonUtils.getNoItemsTips(record, zhanghCtx.getZhangh());
			getPrinter().printf(tips);
			getPrinter().printf("\r\n");
		} else {
			String huobdh = zhanghCtx.getHuobdh();
			for (Item0770 item : itemList) {
				// 交易日期 摘要 凭证种类 凭证序号 借方发生额 余额 交易柜员 对方账号 对方名称
				getPrinter().printf("%1s", "");
				getPrinter().printf("%-8s", item.getJioyrq());
				DbcSbcUtils.fixLeftPrint(getPrinter(), item.getZhyodm(), 8);
				getPrinter().printf("%2s", "");
				getPrinter().printf("%7s", item.getPngzlx());
				getPrinter().printf("%1s", "");
				getPrinter().printf("%14s", item.getPngzhh());
				if (item.getJiedbz().equals("0")) {
					getPrinter().printf("%1s%21s%1s%21s", "", QueryMethodUtils.decimalFormat(item.getJio1je(), huobdh), "", "0.00");
				} else {
					getPrinter().printf("%1s%21s%1s%21s", "", "0.00", "", QueryMethodUtils.decimalFormat(item.getJio1je(), huobdh));
				}
				getPrinter().printf("%1s", "");
				getPrinter().printf("%21s", QueryMethodUtils.decimalFormat(item.getZhhuye(), huobdh));
				getPrinter().printf("%1s", "");
				getPrinter().printf("%8s", item.getJio1gy());

				DbcSbcUtils.fixRightPrint(getPrinter(), item.getDuifzh(), 16);
				DbcSbcUtils.fixLeftPrint(getPrinter(), item.getDuifmc(), 21);
				getPrinter().printf("\r\n\r\n");
			}
		}
	}

	@Override
	public void appendPageTail(OutPdfContext ctx) {
		Output0770Context pageCtx = (Output0770Context) ctx;
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		getPrinter().printf("%2s%2s", "", "柜员:" + pageCtx.getJio1gy());
		getPrinter().printf("%182s\r\n", "第" + pageCtx.getPageNum() + "页");
		getPrinter().println(PAGER_SWITCHOR);
	}

	@Override
	public void appendZhanghTail(OutPdfContext ctx) {
		Output0770Context pageCtx = (Output0770Context) ctx;
		getPrinter()
				.printf("%1s\r\n",
						"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 添加当前账号统计信息

		DbcSbcUtils.fixRightPrint(getPrinter(), "账户笔数：" + pageCtx.getCurrentTotal() + "账户页数：" + pageCtx.getCurrentPageNum(), 98);
		getPrinter().printf("\r\n");
		getPrinter()
				.printf("%1s\r\n",
						"=========================================================================================================================================================================================================");

		getPrinter().printf("%2s%2s", "", "柜员:" + pageCtx.getJio1gy());
		getPrinter().printf("%182s\r\n", "第" + pageCtx.getPageNum() + "页");
		getPrinter().println(PAGER_SWITCHOR);
	}

	@Override
	public void appendLastTail(OutPdfContext pageCtx) {
		// do nothing
	}
}