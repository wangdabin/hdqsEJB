package com.ceb.hdqs.wtc;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例工厂模式，提供交易访问接口
 * 
 * @author user
 * 
 */
public class QueryBeanFactory {
	/**
	 * 加载时提供初始化操作，加载成功后提供get操作
	 */
	private final Map<String, AbstractQueryBean> cache = new HashMap<String, AbstractQueryBean>();

	private QueryBeanFactory() {
		init0770Bean();
		init0771Bean();
		init0772Bean();
		init0773Bean();
		init0774Bean();
		init0775Bean();
		init0776Bean();
		init0777Bean();
		init0778Bean();
		init0779Bean();
		init0780Bean();
		init0781Bean();
	}

	private static class InstanceHolder {
		private static QueryBeanFactory instance = new QueryBeanFactory();
	}

	public static QueryBeanFactory getInstance() {
		return InstanceHolder.instance;
	}

	public AbstractQueryBean getBean(String exCode) {
		return cache.get(exCode);
	}

	private void init0770Bean() {
		Handle0770QueryBean queryBean = new Handle0770QueryBean();
		queryBean.setFieldCount(17);
		queryBean.setExCode("0770");
		queryBean.setFileRecv("O07701");
		queryBean.setFileSent("O07702");
		queryBean.setFileAsyn("O07703");
		queryBean.setFileTable("F07701");
		queryBean.setFilePrint("O07704");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0771Bean() {
		Handle0771QueryBean queryBean = new Handle0771QueryBean();
		queryBean.setFieldCount(18);
		queryBean.setExCode("0771");
		queryBean.setFileRecv("O07711");
		queryBean.setFileSent("O07712");
		queryBean.setFileAsyn("O07713");
		queryBean.setFileTable("F07711");
		queryBean.setFilePrint("O07714");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0772Bean() {
		Handle0772QueryBean queryBean = new Handle0772QueryBean();
		queryBean.setFieldCount(18);
		queryBean.setExCode("0772");
		queryBean.setFileRecv("O07721");
		queryBean.setFileSent("O07722");
		queryBean.setFileAsyn("O07723");
		queryBean.setFileTable("F07721");
		queryBean.setFilePrint("O07724");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0773Bean() {
		Handle0773QueryBean queryBean = new Handle0773QueryBean();
		queryBean.setFieldCount(5);
		queryBean.setExCode("0773");
		queryBean.setFileRecv("O07731");
		queryBean.setFileSent("O07732");
		queryBean.setFileTable("F07732");
		queryBean.setFilePrint("O07734");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0774Bean() {
		Handle0774QueryBean queryBean = new Handle0774QueryBean();
		queryBean.setFieldCount(10);
		queryBean.setExCode("0774");
		queryBean.setFileRecv("O07741");
		queryBean.setFileSent("O07742");
		queryBean.setFileTable("F07741");
		queryBean.setFileSubmit("O07743");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0775Bean() {
		Handle0775QueryBean queryBean = new Handle0775QueryBean();
		queryBean.setFieldCount(9);
		queryBean.setExCode("0775");
		queryBean.setFileRecv("O07751");
		queryBean.setFileSent("O07752");
		queryBean.setFileTable("F07751");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0776Bean() {
		Handle0776QueryBean queryBean = new Handle0776QueryBean();
		queryBean.setFieldCount(11);
		queryBean.setExCode("0776");
		queryBean.setFileRecv("O07761");
		queryBean.setFileSent("O07762");
		queryBean.setFileTable("F07761");
		queryBean.setFileDownLoad("O07763");
		queryBean.setFileDLTable("F07762");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0777Bean() {
		Handle0777QueryBean queryBean = new Handle0777QueryBean();
		queryBean.setExCode("0777");
		queryBean.setFileRecv("O07771");
		queryBean.setFileSent("O07772");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0778Bean() {
		Handle0778QueryBean queryBean = new Handle0778QueryBean();
		queryBean.setExCode("0778");
		queryBean.setFileRecv("O07781");
		queryBean.setFileSent("O07782");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0779Bean() {
		Handle0779QueryBean queryBean = new Handle0779QueryBean();
		queryBean.setFieldCount(11);
		queryBean.setExCode("0779");
		queryBean.setFileRecv("O07791");
		queryBean.setFileSent("O07792");
		queryBean.setFileTable("F07791");
		queryBean.setFileDownLoad("O07793");
		queryBean.setFileDLTable("F07792");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0780Bean() {
		Handle0780QueryBean queryBean = new Handle0780QueryBean();
		queryBean.setFieldCount(10);
		queryBean.setExCode("0780");
		queryBean.setFileRecv("O07801");
		queryBean.setFileSent("O07802");
		queryBean.setFileTable("F07801");
		queryBean.setFileSubmit("O07803");
		cache.put(queryBean.getExCode(), queryBean);
	}

	private void init0781Bean() {
		Handle0781QueryBean queryBean = new Handle0781QueryBean();
		queryBean.setFieldCount(21);
		queryBean.setExCode("0781");
		queryBean.setFileRecv("O07811");
		queryBean.setFileSent("O07812");
		queryBean.setFileAsyn("O07813");
		queryBean.setFileTable("F07811");
		queryBean.setFilePrint("O07814");
		cache.put(queryBean.getExCode(), queryBean);
	}
}