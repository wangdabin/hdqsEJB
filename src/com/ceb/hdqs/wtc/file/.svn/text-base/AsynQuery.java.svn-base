package com.ceb.hdqs.wtc.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.action.query.parser.BkhzjParser;
import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.config.RegisterTable;
import com.ceb.hdqs.constants.HdqsConstants;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.wtc.HdqsWtcException;

public abstract class AsynQuery {
	private static final Logger log = Logger.getLogger(AsynQuery.class);

	protected static final String SPLIT_CHAR = "\\|\\+\\|";

	public List<PybjyEO> getRecordList(File file, PybjyEO vo) throws HdqsWtcException {
		ConfigLoader instance = ConfigLoader.getInstance();
		List<PybjyEO> list = new ArrayList<PybjyEO>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
					instance.getProperty(RegisterTable.ASY_HANDLE_FILE_CHARSET)));
			String line = null;
			int lineNum = 0;
			Map<String, String> lineMap = new HashMap<String, String>();
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (StringUtils.isNotBlank(line)) {
					lineNum++;
					if (!lineMap.containsKey(line)) {
						lineMap.put(line, null);
						String[] contents = line.split(SPLIT_CHAR);
						validLine(contents, lineNum);
						PybjyEO record = initRecord(contents, lineNum, vo);
						record.setRecvFile(file.getAbsolutePath());
						splitRecord(list, record);
					} else {
						log.info("重复项(" + lineNum + ")" + line);
					}
				}
			}
			lineMap.clear();
			lineMap = null;
		} catch (FileNotFoundException e) {
			log.info(e.getMessage(), e);
			throw new HdqsWtcException(file.getName() + "文件不存在");
		} catch (IOException e) {
			log.info(e.getMessage(), e);
			throw new HdqsWtcException(file.getName() + "IO 异常");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		return list;
	}

	public void splitRecord(List<PybjyEO> list, PybjyEO record) {

		if (record.getChaxzl().equals(HdqsConstants.CHAXZL_ZHJNZL)) {
			BkhzjParser arser = new BkhzjParser();
			Set<String> keHuHaoSet = arser.getkehuhaoResults(record);
			if (!keHuHaoSet.isEmpty()) {
				Iterator<String> iterator = keHuHaoSet.iterator();
				while (iterator.hasNext()) {
					String keHuHao = iterator.next();
					PybjyEO temRecord = new PybjyEO();
					try {
						BeanUtils.copyProperties(temRecord, record);
					} catch (IllegalAccessException e) {
						log.info(e.getMessage());
					} catch (InvocationTargetException e) {
						log.info(e.getMessage());
					}
					temRecord.setKehuhao(keHuHao);
					temRecord.setId(null);
					list.add(temRecord);
				}
			} else {
				list.add(record);
			}

		} else {
			list.add(record);
		}
	}

	protected abstract PybjyEO initRecord(String[] contents, int lineNum, PybjyEO record) throws HdqsWtcException;

	protected abstract void validLine(String[] contents, int lineNum) throws HdqsWtcException;
}