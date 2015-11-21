package com.ceb.hdqs.query.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 输出文件的的信息
 * 
 * @author user
 * 
 */
public abstract class HdqsDocument implements Serializable {
	private static final long serialVersionUID = 812499887931654315L;

	/**
	 * 文件名称
	 */
	public String fileName;
	/**
	 * 文件大小（B）
	 */
	public long documentSize;

	/**
	 * 当前文件中包含的每个账号，以及每个账号明细存储的页码
	 */
	public Map<DocKey, List<Integer>> zhanghPageInfo = new HashMap<DocKey, List<Integer>>();

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getAllPageNum() {
		int pageNum = 0;
		for (Entry<DocKey, List<Integer>> pageEntry : zhanghPageInfo.entrySet()) {
			pageNum += pageEntry.getValue().size();
		}
		return pageNum;
	}

	/**
	 * 获取当前文件中当前账号的页面数量
	 * 
	 * @param zhangh
	 * @return
	 */
	public long getPageNumByZhangh(DocKey zhangh) {

		List<Integer> pageList = zhanghPageInfo.get(zhangh);
		return pageList == null ? 0 : pageList.size();
	}

	// public void setPageNum(long pageNum) {
	// this.pageNum = pageNum;
	// }

	public long getDocumentSize() {
		return documentSize;
	}

	public void setDocumentSize(long documentSize) {
		this.documentSize = documentSize;
	}

	/**
	 * 获取当前账号在当前文档中的页面分布情况
	 * 
	 * @param zhangh
	 * @return
	 */
	public List<Integer> getPages(DocKey zhangh) {
		return this.zhanghPageInfo.get(zhangh);
	}

	/**
	 * 有可能一个文档有多个账号的明细信息，因此该方法实现在当前文档中记录每个账号明细的页面分布情况
	 * 
	 * @param zhangh
	 * @param pages
	 */
	public void addPages(DocKey zhangh, List<Integer> pages) {
		this.zhanghPageInfo.put(zhangh, pages);
	}

	/**
	 * 删除指定账号对应的页面信息
	 * 
	 * @param zhangh
	 */
	public abstract void deleteZhangItem(DocKey zhangh, String errorMsg);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HdqsDocument other = (HdqsDocument) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}

}