package com.ceb.hdqs.query.entity;

import com.ceb.hdqs.action.query.output.PdfEditor;

/**
 * PDF文档
 * 
 * @author user
 * 
 */
public class PdfDocument extends HdqsDocument {
	private static final long serialVersionUID = -8204986501214320446L;

	@Override
	public void deleteZhangItem(DocKey zhangh, String errorMsg) {
		if (zhanghPageInfo.size() > 1) {// 说明当前文档中有多个账号的明细
			// 磁盘中删除该账号明细文件
			PdfEditor.deletePages(fileName, zhanghPageInfo.get(zhangh), errorMsg);
			// // 从内存记录中删除该账号
			// zhanghPageNum.remove(zhangh);

		} else if (zhanghPageInfo.size() == 1) {
			// 如果只有一个账号的信息，说明当前文档可以被删除掉
			PdfEditor.dropDocument(fileName);
		}
	}
}