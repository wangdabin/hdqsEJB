package com.ceb.hdqs.query.entity;

import com.ceb.hdqs.action.query.output.XlsEditor;

/**
 * excel文件的统计信息
 * 
 * @author user
 * 
 */
public class XlsDocument extends HdqsDocument {
	private static final long serialVersionUID = 884131820852248662L;

	@Override
	public void deleteZhangItem(DocKey zhangh, String errorMsg) {
		if (zhanghPageInfo.size() > 1) {// 说明当前文档中有多个账号的明细
			// 磁盘中删除该账号明细文件
			XlsEditor.deletePages(fileName, zhanghPageInfo.get(zhangh), errorMsg);
			// 从内存记录中删除该账号
			zhanghPageInfo.remove(zhangh);
		} else if (zhanghPageInfo.size() == 1) {
			// 如果只有一个账号的信息，说明当前文档可以被删除掉
			XlsEditor.dropDocument(fileName);
		}
	}
}