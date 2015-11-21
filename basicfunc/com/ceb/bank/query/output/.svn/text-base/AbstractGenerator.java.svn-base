package com.ceb.bank.query.output;

import java.io.File;

import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryMethodUtils;

public abstract class AbstractGenerator {
	private File file;

	public String buildOutputDir(PybjyEO record) {
		String outputDir = QueryMethodUtils.generateOutputDir(record);
		File dirFile = new File(outputDir);
		dirFile.mkdirs();
		return outputDir;
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
