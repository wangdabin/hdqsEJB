package com.ceb.hdqs.service.cache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.po.DownLoadFilePO;

public class C0779RowkeyItem extends AbstractRowkeyItem {
	private List<PybjyEO> ascList = Collections.synchronizedList(new ArrayList<PybjyEO>());
	private int totalNum = 0;
	private int succNum = 0;
	private int failNum = 0;
	private long totalSize = 0;
	private String resultFile = "";
	private DownLoadFilePO dlFilePo = new DownLoadFilePO();

	public List<PybjyEO> getAscList() {
		return ascList;
	}

	public void setAscList(List<PybjyEO> ascList) {
		this.ascList = ascList;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getSuccNum() {
		return succNum;
	}

	public void setSuccNum(int succNum) {
		this.succNum = succNum;
	}

	public int getFailNum() {
		return failNum;
	}

	public void setFailNum(int failNum) {
		this.failNum = failNum;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

	public DownLoadFilePO getDlFilePo() {
		return dlFilePo;
	}

	public void setDlFilePo(DownLoadFilePO dlFilePo) {
		this.dlFilePo = dlFilePo;
	}

	public static void main(String[] args) {
		File file = new File("E:/tools/wls1035_generic.jar");
		System.out.println((file.length() / 1024) + "KB");
	}
}