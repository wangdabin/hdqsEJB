package com.ceb.hdqs.action.query.output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * excel文档编辑器
 * 
 * @author user
 * 
 */
public class XlsEditor {
	private static final Log log = LogFactory.getLog(XlsEditor.class);

	/**
	 * 删除指定账号的信息，并且在删除明细的地方添加账号的不连续信息
	 * 
	 * @param xlsName
	 *            被删除的文件名称
	 * @param sheetNum
	 *            需要被删除的页码
	 * @param errorMsg
	 *            需要填充进去的不连续说明
	 */
	public static void deletePages(String xlsName, List<Integer> sheetNum, String errorMsg) {
		XSSFWorkbook book = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		log.info("删除的文档是： " + xlsName);
		for (Integer num : sheetNum) {
			log.info("删除的页签是： " + num);
		}
		Collections.sort(sheetNum, new Comparator<Integer>() {

			@Override
			public int compare(Integer o1, Integer o2) {
				return o1 - o2 == 0 ? 0 : (o1 > o2 ? -1 : 1);
			}
		});
		try {
			// 执行删除操作
			fis = new FileInputStream(new File(xlsName));
			book = new XSSFWorkbook(fis);
			for (int sheetN : sheetNum) {
				book.removeSheetAt(sheetN - 1);
			}
			// 打开输出流,将修改输出
			fos = new FileOutputStream(new File(xlsName));
			book.write(fos);
			fos.flush();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 删除包含不连续明细的完整文档
	 * 
	 * @param pdfName
	 *            被删除的文件名称
	 */
	public static void dropDocument(String xlsName) {
		File file = new File(xlsName);
		if (file.exists())
			file.delete();
	}

	public static void main(String[] args) {
		List<Integer> num = new ArrayList<Integer>();
		num.add(2);
		num.add(3);

		XlsEditor.deletePages("D:\\pdfTest\\delete.xlsx", num, "");

	}
}
