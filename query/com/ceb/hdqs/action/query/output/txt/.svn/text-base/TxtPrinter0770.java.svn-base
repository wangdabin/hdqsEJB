package com.ceb.hdqs.action.query.output.txt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.commons.lang.StringUtils;

import com.ceb.hdqs.action.query.output.TxtMaker;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.CorporateQueryItem;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.utils.DbcSbcUtils;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class TxtPrinter0770 extends TxtMaker<CorporateQueryItem> {

	public TxtPrinter0770(PybjyEO record) {
		super(record);
	}

	@Override
	public void write(Page<CorporateQueryItem> page) {
		pageCount++;
		PageHeader header = page.getHeader();
		printer.printf("%103s\r\n\r\n", "中国光大银行对公帐户对账单(本对账单仅供参考)");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "账户名称:" + header.getKhzwm(), 69);
		DbcSbcUtils.fixRightPrint(printer, "对账日期:" + header.getCxqsrq() + "-" + header.getCxzzrq(), 28);
		printer.printf("\r\n");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "账    号:" + header.getZhangh(), 69);
		DbcSbcUtils
				.fixRightPrint(printer, "打印时间:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 28);
		printer.printf("\r\n");
		printer.printf(
				"%1s\r\n",
				"=========================================================================================================================================================================================================");

		printer.printf("%5s%9s%11s%11s%17s%17s%20s%5s%29s%23s\r\n", "交易日期", "摘要", "凭证种类", "凭证序号", "借方发生额", "贷方发生额",
				"余额", "交易柜员", "对方账号", "对方名称");
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		String huobdh = header.getHbdh();
		if (StringUtils.isNotBlank(page.getHeader().getTips())) {
			printer.printf(page.getHeader().getTips());
			printer.printf("\r\n");
		} else {
			for (CorporateQueryItem item : page.getPageItem()) {
				// 交易日期 摘要 凭证种类 凭证序号 借方发生额 余额 交易柜员 对方账号 对方名称
				printer.printf("%1s", "");
				printer.printf("%-8s", item.getJIOYRQ());
				DbcSbcUtils.fixLeftPrint(printer, item.getZHYODM(), 8);
				printer.printf("%2s", "");
				printer.printf("%7s", item.getPNZZLX());
				printer.printf("%1s", "");
				printer.printf("%14s", item.getPNGZHH());
				if (item.getJIEDBZ().equals("0")) {
					printer.printf("%1s%21s%1s%21s", "", QueryMethodUtils.decimalFormat(item.getJIO1JE(), huobdh), "",
							"0.00");
				} else {
					printer.printf("%1s%21s%1s%21s", "", "0.00", "",
							QueryMethodUtils.decimalFormat(item.getJIO1JE(), huobdh));
				}
				printer.printf("%1s", "");
				printer.printf("%21s", QueryMethodUtils.decimalFormat(item.getZHHUYE(), huobdh));
				printer.printf("%1s", "");
				printer.printf("%8s", item.getJIO1GY());

				// printer.printf("%1s", "");
				// printer.printf("%32s", item.getDUIFZH());
				DbcSbcUtils.fixRightPrint(printer, item.getDUIFZH(), 16);
				DbcSbcUtils.fixLeftPrint(printer, item.getDUIFMC(), 21);
				printer.printf("\r\n\r\n");
			}
		}
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 添加当前账号统计信息
		if (page.getParseResult().getFinished() == 1) {

			DbcSbcUtils.fixRightPrint(printer, "账户笔数：" + page.getParseResult().getItemCount() + "账户页数："
					+ page.getParseResult().getPageTotal(), 98);
			printer.printf("\r\n");
			printer.printf(
					"%1s\r\n",
					"=========================================================================================================================================================================================================");
		}
		printer.printf("%2s%2s", "", "柜员:" + page.getHeader().getGuiyuan());
		printer.printf("%182s\r\n", "第" + pageCount + "页");
		printer.println(SWITCH_PAGER);

	}

	public static void main(String[] args) throws Exception {
		char newPage = (char) 0x0c;
		PrintStream printer = new PrintStream(new FileOutputStream(new File("d:/printer.txt")), true, DEFAULT_CHARSET);
		printer.printf("%103s\r\n\r\n", "中国光大银行对公帐户对帐单(本对帐单仅供参考)");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "账号名称：梁朝伟", 69);
		DbcSbcUtils.fixRightPrint(printer, "对账日期：20130606-20130909", 28);
		printer.printf("\r\n");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "账    号：621492020002282822222", 69);
		DbcSbcUtils
				.fixRightPrint(printer, "打印时间：" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 28);
		printer.printf("\r\n");
		printer.printf(
				"%1s\r\n",
				"=========================================================================================================================================================================================================");

		printer.printf("%5s%9s%11s%11s%17s%17s%20s%5s%29s%23s\r\n", "交易日期", "摘要", "凭证种类", "凭证序号", "借方发生额", "贷方发生额",
				"余额", "交易柜员", "对方账号", "对方名称");
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		int count = 15;
		for (int i = 0; i < count; i++) {
			// 交易日期 摘要 凭证种类 凭证序号 借方发生额 余额 交易柜员 对方账号 对方名称
			printer.printf("%1s", "");
			printer.printf("%-8s", "20139999");
			DbcSbcUtils.fixLeftPrint(printer, "支付信用卡备注信息测试测试测试", 8);
			printer.printf("%2s", "");
			printer.printf("%7s", "653");
			printer.printf("%1s", "");
			printer.printf("%14s", "25725799712184");
			printer.printf("%1s", "");
			printer.printf("%21s", QueryMethodUtils.decimalFormat("1650000000.00", 2));
			printer.printf("%1s", "");
			printer.printf("%21s", QueryMethodUtils.decimalFormat("0.00", 2));
			printer.printf("%1s", "");
			printer.printf("%21s", QueryMethodUtils.decimalFormat("1650000000.00", 2));
			printer.printf("%1s", "");
			printer.printf("%8s", "589756");

			DbcSbcUtils.fixRightPrint(printer, "62284801111815928129900000000000", 16);
			DbcSbcUtils.fixLeftPrint(printer, "对方姓名对方姓名对方姓名对方姓名对方姓名对方姓对方姓名对方姓名对方姓名对方姓名对方姓名对方姓", 21);
			printer.printf("\r\n\r\n");
		}
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 添加当前账号统计信息
		boolean flag = true;
		if (flag) {
			DbcSbcUtils.fixRightPrint(printer, "账户笔数：188882    账户页数：1222", 98);
			printer.printf("\r\n");
			printer.printf(
					"%1s\r\n",
					"=========================================================================================================================================================================================================");
		}

		printer.printf("%2s%2s", "", "柜员:010999");
		printer.printf("%182s\r\n", "第1页");
		printer.println(newPage);
	}
}
