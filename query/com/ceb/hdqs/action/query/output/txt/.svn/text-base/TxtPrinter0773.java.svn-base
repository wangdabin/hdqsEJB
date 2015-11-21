package com.ceb.hdqs.action.query.output.txt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.ceb.hdqs.action.query.output.TxtMaker;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.entity.result.ReplacementCardItemResult;
import com.ceb.hdqs.query.entity.NoItemPage;
import com.ceb.hdqs.query.entity.Page;
import com.ceb.hdqs.query.entity.PageHeader;
import com.ceb.hdqs.query.utils.DbcSbcUtils;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public class TxtPrinter0773 extends TxtMaker<ReplacementCardItemResult> {

	public TxtPrinter0773(PybjyEO record) {
		super(record);
	}

	@Override
	public void write(Page<ReplacementCardItemResult> page) {
		pageCount++;
		if (page instanceof NoItemPage) {
			printer.printf("%-2s", page.getTip());
		} else {
			PageHeader header = page.getHeader();
			printer.printf("%63s\r\n\r\n", "中国光大银行换卡登记表");
			DbcSbcUtils.fixLeftPrint(printer, "客户号:" + header.getKehhao(), 41);
			DbcSbcUtils.fixRightPrint(printer,
					"打印日期:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 21);
			printer.printf("\r\n");
			DbcSbcUtils.fixLeftPrint(printer, "客户中文名:" + header.getKhzwm(), 20);
			printer.printf("\r\n");
			DbcSbcUtils.fixLeftPrint(printer, "备注信息:" + header.getTips(), 80);
			printer.printf("\r\n");
			printer.printf(
					"%1s%1s\r\n",
					"",
					"=============================================================================================================================");
			printer.printf("%1s%-21s%19s%21s%20s%23s\r\n", "", "客户账号", "记录状态", "借贷记卡标志", "卡种类", "发卡日期");
			printer.printf(
					"%1s%1s\r\n",
					"",
					"-----------------------------------------------------------------------------------------------------------------------------");
			for (ReplacementCardItemResult item : page.getPageItem()) {
				// 客户账号 记录状态 借贷记卡标志 卡种类 发卡日期
				printer.printf("%1s", "");
				printer.printf("%-21s", item.getKahaoo());
				printer.printf("%1s", "");
				DbcSbcUtils.fixRightPrint(printer, QueryMethodUtils.jlztFormat(item.getJiluzt()), 12);
				printer.printf("%1s", "");
				DbcSbcUtils.fixRightPrint(printer, QueryMethodUtils.jdjkbzFormat(item.getJdjkbz()), 12);
				printer.printf("%1s", "");
				DbcSbcUtils.fixRightPrint(printer, QueryMethodUtils.kaazlFormat(item.getKaaazl()), 12);
				printer.printf("%2s", "");
				printer.printf("%24s", item.getFakarq());
				printer.printf("\r\n\r\n");
			}
			printer.printf(
					"%1s%1s\r\n",
					"",
					"=============================================================================================================================");
			printer.printf("%1s%2s\r\n\r\n", "", "柜员:" + header.getGuiyuan());
			printer.printf("%120s\r\n", "第" + pageCount + "页");
		}
		printer.println(SWITCH_PAGER);

	}

	public static void main(String[] args) throws Exception {
		PrintStream printer = new PrintStream(new FileOutputStream(new File("d:/printer.txt")), true, DEFAULT_CHARSET);
		printer.printf("%63s\r\n\r\n", "中国光大银行换卡登记表");
		DbcSbcUtils.fixLeftPrint(printer, "客户号:621492020002282822222", 41);
		DbcSbcUtils
				.fixRightPrint(printer, "打印日期:" + QueryMethodUtils.formatDateAndTime(System.currentTimeMillis()), 21);
		printer.printf("\r\n");
		DbcSbcUtils.fixLeftPrint(printer, "客户中文名:梁朝伟", 20);
		printer.printf("\r\n");
		DbcSbcUtils.fixLeftPrint(printer, "备注信息:被小三怀孕逼宫被小三怀孕逼宫被小三怀孕逼宫被小三怀孕逼宫被小三怀孕逼宫12", 120);
		printer.printf("\r\n");
		printer.printf(
				"%1s%1s\r\n",
				"",
				"=============================================================================================================================");
		printer.printf("%1s%-21s%19s%21s%20s%23s\r\n", "", "客户账号", "记录状态", "借贷记卡标志", "卡种类", "发卡日期");
		printer.printf(
				"%1s%1s\r\n",
				"",
				"-----------------------------------------------------------------------------------------------------------------------------");
		int count = 15;
		for (int i = 0; i < count; i++) {
			// 客户账号 记录状态 借贷记卡标志 卡种类 发卡日期
			printer.printf("%1s", "");
			printer.printf("%-21s", "621492020002282822222");
			printer.printf("%1s", "");
			DbcSbcUtils.fixRightPrint(printer, "1-销户", 12);
			printer.printf("%1s", "");
			DbcSbcUtils.fixRightPrint(printer, "2-借记卡", 12);
			printer.printf("%1s", "");
			DbcSbcUtils.fixRightPrint(printer, "2-借记卡", 12);
			printer.printf("%2s", "");
			printer.printf("%24s", "20130606");
			printer.printf("\r\n\r\n");
		}
		printer.printf(
				"%1s%1s\r\n",
				"",
				"=============================================================================================================================");
		printer.printf("%1s%2s\r\n\r\n", "", "柜员:028888");
		printer.printf("%120s\r\n", "第1页");
		printer.println(SWITCH_PAGER);
	}
}
