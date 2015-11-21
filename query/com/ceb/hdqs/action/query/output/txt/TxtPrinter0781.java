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

public class TxtPrinter0781 extends TxtMaker<CorporateQueryItem> {

	public TxtPrinter0781(PybjyEO record) {
		super(record);
	}

	@Override
	public void write(Page<CorporateQueryItem> page) {
		pageCount++;

		PageHeader header = page.getHeader();
		printer.printf("%101s\r\n\r\n", "中国光大银行单位卡对账单(本对帐单仅供参考)");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "客户姓名:" + header.getKhzwm(), 82);
		DbcSbcUtils.fixLeftPrint(printer, "对账日期:" + header.getCxqsrq() + "-" + header.getCxzzrq(), 50);

		printer.printf("\r\n");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "发卡/折机构:" + header.getFkjgHao() + " " + header.getFkjgName(), 82);
		DbcSbcUtils.fixLeftPrint(printer, "打印时间:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 50);
		printer.printf("\r\n");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "客户账号:" + page.getHeader().getKhzh(), 82);
		DbcSbcUtils.fixLeftPrint(printer, "系统账号:" + page.getHeader().getZhangh(), 50);
		printer.printf("\r\n");
		printer.printf(
				"%1s\r\n",
				"=========================================================================================================================================================================================================");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "币种:" + QueryMethodUtils.huobdhFormat(header.getHbdh()), 55);
		DbcSbcUtils.fixRightPrint(printer, "钞汇标志:" + QueryMethodUtils.chuibzFormat(header.getChbz()), 34);
		printer.printf("\r\n");
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		printer.printf("%1s%-17s%5s%7s%20s%18s%18s%17s%43s%12s\r\n", "", "客户账号", "交易日期", "交易地点", "存入金额", "转出金额", "账户余额", "摘要", "对方账号",
				"对方名称");
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		String huobdh = header.getHbdh();
		if (StringUtils.isNotBlank(page.getHeader().getTips())) {
			printer.printf(page.getHeader().getTips());
			printer.printf("\r\n");
		} else {
			for (CorporateQueryItem item : page.getPageItem()) {
				printer.printf("%1s", "");
				printer.printf("%-21s", page.getHeader().getKhzh());

				printer.printf("%1s", "");
				printer.printf("%8s", item.getJIOYRQ());
				DbcSbcUtils.fixLeftPrint(printer, "", 6);
				if (item.getJIEDBZ().equals("1")) {
					printer.printf("%1s%21s%1s%21s", "", QueryMethodUtils.decimalFormat(item.getJIO1JE(), huobdh), "", "0.00");
				} else {
					printer.printf("%1s%21s%1s%21s", "", "0.00", "", QueryMethodUtils.decimalFormat(item.getJIO1JE(), huobdh));
				}

				printer.printf("%1s", "");
				printer.printf("%21s", QueryMethodUtils.decimalFormat(item.getZHHUYE(), huobdh));
				DbcSbcUtils.fixLeftPrint(printer, item.getZHYODM(), 16);
				// printer.printf("%1s", "");
				// printer.printf("%32s", item.getDUIFZH());
				DbcSbcUtils.fixRightPrint(printer, item.getDUIFZH(), 16);
				DbcSbcUtils.fixLeftPrint(printer, item.getDUIFMC(), 12);
				printer.printf("\r\n\r\n");
			}
		}
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		// 添加当前账号统计信息
		if (page.getParseResult().getFinished() == 1) {
			DbcSbcUtils.fixRightPrint(printer, "账户笔数：" + page.getParseResult().getItemCount() + " 账户页数："
					+ page.getParseResult().getPageTotal(), 96);
			printer.printf("\r\n");
			printer.printf(
					"%1s\r\n",
					"=========================================================================================================================================================================================================");
		}
		// 添加当前账号的上级条件的统计信息
		if (page.getParseResult().getQueriedZhNum() == page.getParseResult().getZhanghTotal()) {
			DbcSbcUtils.fixRightPrint(printer, "总账户数:" + page.getParseResult().getZhanghTotal() + " 总笔数:"
					+ page.getParseResult().getAllItemCount() + " 总页数:" + page.getParseResult().getAllPageCount(), 96);
			printer.printf("\r\n\r\n");
			printer.printf(
					"%1s\r\n",
					"========================================================================对帐单流水打印结束==========================最后一页=============================================================================");
		}
		printer.printf("%2s%2s", "", "柜员:" + header.getGuiyuan());
		printer.printf("%179s\r\n", "第" + pageCount + "页");
		printer.println(SWITCH_PAGER);
	}

	public static void main(String[] args) throws Exception {
		char newPage = (char) 0x0c;
		PrintStream printer = new PrintStream(new FileOutputStream(new File("d:/printer.txt")), true, DEFAULT_CHARSET);
		printer.printf("%101s\r\n\r\n", "中国光大银行单位卡对帐单(本对帐单仅供参考)");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "姓名:梁朝伟", 82);
		DbcSbcUtils.fixLeftPrint(printer, "对账日期:20130606-20130909", 50);
		printer.printf("\r\n");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "发卡/折机构:1245 中国光大银行", 82);

		DbcSbcUtils.fixLeftPrint(printer, "打印时间:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 50);
		printer.printf("\r\n");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "客户账号:621492020002282800000", 82);
		DbcSbcUtils.fixLeftPrint(printer, "系统账号:621492020002282800000", 50);
		printer.printf("\r\n");
		printer.printf(
				"%1s\r\n",
				"=========================================================================================================================================================================================================");
		printer.printf("%2s", "");
		DbcSbcUtils.fixLeftPrint(printer, "币种：人民币", 55);
		DbcSbcUtils.fixRightPrint(printer, "钞汇标志：钞户", 34);
		printer.printf("\r\n");
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

		printer.printf("%1s%-17s%5s%7s%20s%18s%18s%17s%43s%12s\r\n", "", "客户账号", "交易日期", "交易地点", "存入金额", "转出金额", "账户余额", "摘要", "对方账号",
				"对方名称");
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		int count = 15;
		for (int i = 0; i < count; i++) {
			printer.printf("%1s", "");
			printer.printf("%-21s", "621492020002282899900");
			printer.printf("%1s", "");
			printer.printf("%8s", "20130909");
			DbcSbcUtils.fixLeftPrint(printer, "交易地点交易地点交易地点交易地点交易地点交易地点", 6);
			printer.printf("%1s", "");
			printer.printf("%21s", QueryMethodUtils.decimalFormat("1650000000.00", 2));
			printer.printf("%1s", "");
			printer.printf("%21s", QueryMethodUtils.decimalFormat("0.00", 2));
			printer.printf("%1s", "");
			printer.printf("%21s", QueryMethodUtils.decimalFormat("1650000000.00", 2));
			DbcSbcUtils.fixLeftPrint(printer, "摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要摘要", 16);

			DbcSbcUtils.fixRightPrint(printer, "对方账号对方账号对方账号对方账号对方账号对方账号", 16);
			DbcSbcUtils.fixLeftPrint(printer, "对方姓名对方名称对方姓名对方名称对方姓名对方名称对方姓名对方名称", 12);
			printer.printf("\r\n\r\n");
		}
		printer.printf(
				"%1s\r\n",
				"---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		if (true) {
			DbcSbcUtils.fixRightPrint(printer, "账户笔数：122    账户页数：122", 96);
			printer.printf("\r\n");
			printer.printf(
					"%1s\r\n",
					"=========================================================================================================================================================================================================");

		}
		// 添加当前账号的上级条件的统计信息
		if (true) {
			DbcSbcUtils.fixRightPrint(printer, "总账户数:1222  总笔数:1222  总页数:1111", 96);
			printer.printf("\r\n\r\n");
			printer.printf(
					"%1s\r\n",
					"========================================================================对帐单流水打印结束==========================最后一页=============================================================================");
		}
		// printer.printf("%1s\r\n",
		// "----------------------------------------------------------------------------------------------------------------------------------------------");
		printer.printf("%2s%2s", "", "柜员:010987");
		printer.printf("%179s\r\n", "第1页");
		printer.println(newPage);

		System.out.println("over");
	}
}
