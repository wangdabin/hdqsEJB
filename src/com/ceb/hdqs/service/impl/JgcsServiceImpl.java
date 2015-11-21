package com.ceb.hdqs.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ceb.hdqs.dao.JgcsDao;
import com.ceb.hdqs.entity.JgcsEO;
import com.ceb.hdqs.service.IJgcsService;
import com.ceb.hdqs.service.JgcsService;
import com.ceb.hdqs.vo.JgcsVO;

@Stateless(mappedName = "JgcsService")
public class JgcsServiceImpl implements IJgcsService, JgcsService {
	private static final Logger log = Logger.getLogger(JgcsServiceImpl.class);

	private static final Map<String, String> yngyjgCache = Collections.synchronizedMap(new HashMap<String, String>());
	private static final Map<String, List<String>> zngwsjCache = Collections.synchronizedMap(new HashMap<String, List<String>>());
	private final ReentrantLock lock = new ReentrantLock();
	private static final String JGYYJB_ZONGHANG = "0";
	@EJB
	JgcsDao referDao;

	public void initCache() {
		List<JgcsEO> totalList = referDao.findAll();
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			for (int i = 0, size = totalList.size(); i < size; i++) {
				JgcsEO tmpObj = totalList.get(i);
				yngyjgCache.put(tmpObj.getYngyjg(), tmpObj.getZngwsj());
				if (StringUtils.isNotBlank(tmpObj.getZngwsj())) {
					List<String> yngyjgList = null;
					if (zngwsjCache.containsKey(tmpObj.getZngwsj())) {
						yngyjgList = zngwsjCache.get(tmpObj.getZngwsj());
					} else {
						yngyjgList = new ArrayList<String>();
						zngwsjCache.put(tmpObj.getZngwsj(), yngyjgList);
					}
					yngyjgList.add(tmpObj.getYngyjg());
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void refreshCache() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			clearCache();
			initCache();
		} finally {
			lock.unlock();
		}
	}

	private void clearCache() {
		final ReentrantLock lock = this.lock;
		lock.lock();
		try {
			yngyjgCache.clear();
			Set<String> set = zngwsjCache.keySet();
			if (set != null) {
				Iterator<String> iter = set.iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					List<String> list = zngwsjCache.get(key);
					list.clear();
					list = null;
					zngwsjCache.put(key, null);
				}
				zngwsjCache.clear();
			}
		} finally {
			lock.unlock();
		}
	}

	public void saveOrUpdate(JgcsVO entity) {
		JgcsEO existObj = findByYngyjg(entity.getYngyjg());
		try {
			if (existObj == null) {
				JgcsEO newObj = new JgcsEO();
				BeanUtils.copyProperties(newObj, entity);
				save(newObj);
			} else {
				BeanUtils.copyProperties(existObj, entity);
				update(existObj);
			}
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			log.error(e.getMessage(), e);
		}
	}

	public void save(JgcsEO entity) {
		referDao.save(entity);
	}

	public void update(JgcsEO entity) {
		referDao.update(entity);
	}

	public JgcsEO findById(Long id) {
		return referDao.findById(id);
	}

	public JgcsEO findByYngyjg(String yngyjg) {
		return referDao.findByYngyjg(yngyjg);
	}

	public List<String> findChildren(String yngyjg) {
		List<String> childList = new ArrayList<String>();
		childList.add(yngyjg);
		if (!zngwsjCache.containsKey(yngyjg)) {
			return childList;
		}
		List<String> yngyjgList = zngwsjCache.get(yngyjg);
		for (int i = 0, size = yngyjgList.size(); i < size; i++) {
			childList.addAll(findChildren(yngyjgList.get(i)));
		}

		return childList;
	}

	// public List<String> findChildren(String yngyjg) throws
	// HdqsServiceException {
	// List<String> list = new ArrayList<String>();
	// list.add(yngyjg);
	// List<JgcsEO> objList = referDao.findChildren(yngyjg);
	// if (objList == null || objList.isEmpty()) {
	// return list;
	// }
	// for (JgcsEO obj : objList) {
	// list.addAll(findChildren(obj.getYngyjg()));
	// }
	// return list;
	// }
	public List<JgcsEO> findAll() {
		List<JgcsEO> list = referDao.findAll();
		if (list == null) {
			return Collections.emptyList();
		}
		return list;
	}

	public void removeAll() {
		referDao.deleteAll();
	}

	public boolean isParentQuery(String parentJG, String childJG) {
		if (parentJG.equals(childJG)) {// 同机构的可以查询
			return true;
		}
		if (!yngyjgCache.containsKey(childJG)) {
			return false;
		}
		String zngwsj = yngyjgCache.get(childJG);
		return isParentQuery(parentJG, zngwsj);
	}

	// public boolean isParentQuery(String parentJG, String childJG) throws
	// HdqsServiceException {
	// if (parentJG.equals(childJG)) {// 同机构的可以查询
	// return true;
	// }
	// JgcsEO qJgcsEO = findByYngyjg(childJG);
	// if (qJgcsEO == null) {
	// throw new HdqsServiceException("查询机构" + childJG + "在机构参数表中不存在");
	// }
	// while (StringUtils.isNotBlank(qJgcsEO.getZngwsj())) {
	// return isParentQuery(parentJG, qJgcsEO.getZngwsj());
	// }
	//
	// return false;
	// }

	public boolean isZonghangQuery(String yngyjg) {
		JgcsEO jgcsEO = findByYngyjg(yngyjg);
		if (jgcsEO == null) {
			return false;
		}
		if (jgcsEO.getJgyyjb().equals(JGYYJB_ZONGHANG)) {
			return true;
		}
		return false;
	}
}