package com.ceb.hdqs.po;

public class DownLoadFilePO {
	private Long itemCount = 0L;
	private String filePath;
	private Boolean isExistExl = false;
	private Long pageCount = 0L;
	private int fileCount;

	public Long getItemCount() {
		return itemCount;
	}

	public void setItemCount(Long itemCount) {
		this.itemCount = itemCount;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Boolean getIsExistExl() {
		return isExistExl;
	}

	public void setIsExistExl(Boolean isExistExl) {
		this.isExistExl = isExistExl;
	}

	public Long getPageCount() {
		return pageCount;
	}

	public void setPageCount(Long pageCount) {
		this.pageCount = pageCount;
	}

	public int getFileCount() {
		return fileCount;
	}

	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}
}