package com.ceb.hdqs.action.asyn;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ceb.bank.asyn.HandleAsyn0770;
import com.ceb.bank.asyn.HandleAsyn0771;
import com.ceb.bank.asyn.HandleAsyn0772;
import com.ceb.bank.asyn.HandleAsyn0777;
import com.ceb.bank.asyn.HandleAsyn0781;
import com.ceb.hdqs.entity.PybjyEO;
import com.ceb.hdqs.query.utils.QueryConfUtils;
import com.ceb.hdqs.query.utils.QueryConstants;

/**
 * 异步查询调度服务
 * 
 * @author user
 * 
 */
public class AsynQueryHandler {
	private static final Log log = LogFactory.getLog(AsynQueryHandler.class);
	private static final String ASYN_QUERY = "com.ceb.hdqs.action.asyn.AsynQuery";

	private volatile static boolean isFinish = true;
	private AsynHandlerPool pool;

	public AsynQueryHandler(AsynHandlerPool pool) {
		this.pool = pool;
	}

	/**
	 * 启动异步查询，开始查询输入条件
	 * 
	 * @param map
	 *            需要处理的集合Map<String, List<PybjyEO>> map key: 受理编号,List<PybjyEO>
	 *            每个受理编号对应的查询条件
	 */
	public synchronized void doAsynQuery(Map<String, List<PybjyEO>> map) {
		isFinish = false;
		try {
			List<IAsynchronizeQuery> list = buildBatchTasks(map);
			if (list == null || list.isEmpty()) {
				return;
			}
			pool.process(list);
			list.clear();
			list = null;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
			isFinish = true;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<IAsynchronizeQuery> buildBatchTasks(Map<String, List<PybjyEO>> map) {
		// 从oracle中获取待处理的任务,key为受理编号,value为对应的集合
		List<IAsynchronizeQuery> resultList = new ArrayList<IAsynchronizeQuery>();
		log.info("获取的异步处理批次为：" + map.size());
		int queryNum = QueryConfUtils.getActiveConfig().getInt(QueryConstants.HANDLE_LINE_PER_PAGE, 35);
		for (String handleNo : map.keySet()) {
			List<PybjyEO> tmpList = map.get(handleNo);

			String exCode = tmpList.get(0).getJiaoym();
			log.info("需要反射的exCode是：" + exCode);
			for (PybjyEO record : tmpList) {
				record.setQueryNum(queryNum);
			}
			if ("0770".equals(exCode)) {
				HandleAsyn0770 handleAsyn = new HandleAsyn0770(tmpList);
				handleAsyn.setTaskName(handleNo);
				resultList.add(handleAsyn);
			} else if ("0771".equals(exCode)) {
				HandleAsyn0771 handleAsyn = new HandleAsyn0771(tmpList);
				handleAsyn.setTaskName(handleNo);
				resultList.add(handleAsyn);
			} else if ("0772".equals(exCode)) {
				HandleAsyn0772 handleAsyn = new HandleAsyn0772(tmpList);
				handleAsyn.setTaskName(handleNo);
				resultList.add(handleAsyn);
			} else if ("0781".equals(exCode)) {
				HandleAsyn0781 handleAsyn = new HandleAsyn0781(tmpList);
				handleAsyn.setTaskName(handleNo);
				resultList.add(handleAsyn);
//			}  else if ("0777".equals(exCode)) {
//				HandleAsyn0777 handleAsyn = new HandleAsyn0777(tmpList);
//				handleAsyn.setTaskName(handleNo);
//				resultList.add(handleAsyn);
			} else {
				try {
					Class clazz = Class.forName(ASYN_QUERY + exCode);
					Constructor constructor = clazz.getDeclaredConstructor(List.class);
					constructor.setAccessible(true);
					AbstractAsynQuery absQuery = (AbstractAsynQuery) constructor.newInstance(tmpList);
					resultList.add(absQuery);
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
		return resultList;
	}

	public boolean isFinish() {
		return isFinish;
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Class.forName("com.ceb.hdqs.action.query.asynchronizequery.AsynQuery0770");
			Class<?> clazz = Class.forName("com.ceb.hdqs.action.query.asynchronizequery.AsynQuery0770");
			Constructor<?> constructor = clazz.getDeclaredConstructor(List.class);
			constructor.setAccessible(true);
			List<PybjyEO> records = new ArrayList<PybjyEO>();
			records.add(new PybjyEO());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}