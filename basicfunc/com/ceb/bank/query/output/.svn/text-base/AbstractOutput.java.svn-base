package com.ceb.bank.query.output;

import org.apache.log4j.Logger;

import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.utils.TimerUtils;

public abstract class AbstractOutput {
	private static final Logger log = Logger.getLogger(AbstractOutput.class);

	public String execute(PybjyEO record) throws Exception {
		log.info("开始输出文件处理...");
		TimerUtils timer = new TimerUtils();
		timer.start();
		String filePath = null;
		try {
			if (HdqsConstants.SHFOBZ_PRINT.equals(record.getShfobz())) {
				filePath = handlePrint(record);
			} else if (HdqsConstants.SHFOBZ_DOWNLOAD.equals(record.getShfobz())) {
				filePath = handleDownload(record);
			} else {
				throw new UnsupportedOperationException("不支持的输出文件处理");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			timer.stop();
			log.info("输出文件处理结束,耗时: " + timer.getExecutionTime() + "ms.");
			log.info("输出文件路径:" + filePath);
		}
		return filePath;
	}

	protected abstract String handleDownload(PybjyEO record) throws Exception;

	protected abstract String handlePrint(PybjyEO record) throws Exception;
}