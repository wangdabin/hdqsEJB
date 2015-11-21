package com.ceb.hdqs.query.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;

import com.ceb.hdqs.action.syn.manager.IManager;
import com.ceb.hdqs.action.syn.manager.ManagerFactory;
import com.ceb.hdqs.action.syn.vo.Host;

/**
 * The QueryConfUtils Class
 * 
 * @author user
 * 
 */
public class QueryConfUtils {
	private static final Log LOG = LogFactory.getLog(QueryConfUtils.class);

	/*------------------------------ INIT    CONFIGURATION --------------------------------------------*/
	private static final Map<String, Configuration> clusterConfig = new HashMap<String, Configuration>();

	private static String activeClusterFlag = null;
	private static String standbyClusterFlag = null;
	public static String weblogic_domain_home = null;
	private static String clusterNameNodes = null;
	public static String LOGO_PATH = null;

	/**
	 * 初始化查询需要的配置信息
	 * 
	 * @param zkQuormLocal
	 * @param zkQuormRemote
	 */
	public static void initConfig(String zkQuormLocal, String zkQuormRemote) {
		LOG.info("获取Active集群的信息");

		LOG.info("加载weblogic domain home");
		String temp = System.getenv("DOMAIN_HOME");
		weblogic_domain_home = (temp == null ? System.getProperty("DOMAIN_HOME") : temp);
		if (weblogic_domain_home == null) {
			throw new RuntimeException("can't get weblgic domain home ");
		}
		LOGO_PATH = weblogic_domain_home + File.separator + "config" + File.separator + "logo.jpg";
		LOG.info("开始加载集群的配置文件");
		try {
			String[] tempNamenodes = clusterNameNodes.split(",");
			for (String nameNode : tempNamenodes) {
				File configFile = new File(weblogic_domain_home + File.separator + "config" + File.separator + QueryConstants.CUSTOMER_CONFIGURATION_FILE + nameNode + ".xml");
				if (!configFile.exists()) {
					throw new RuntimeException("Load cluster config file " + configFile.getName() + "failed! ");
				}
				Configuration conf = new Configuration(false);

				conf.addResource(new FileInputStream(configFile));
				LOG.debug("Load  configuration resource " + configFile.getAbsolutePath() + "完成！");
				clusterConfig.put(nameNode.trim(), conf);
				LOG.debug("ClusterConfig  size  keys is：");
				for (Entry<String, Configuration> entry : clusterConfig.entrySet()) {
					LOG.debug(entry.getKey());
				}
			}

			// 获取Active的集群
			LOG.info("zkQuormLocal  is:" + zkQuormLocal);
			LOG.info("zkQuormRemote  is:" + zkQuormRemote);

			getActiveClusterFlagByRPC(zkQuormLocal, zkQuormRemote);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			System.out.println("***************HDQS will exit!***************");
			e.printStackTrace();
			System.out.println("*********************************************");
			System.exit(-1);
		}

	}

	/**
	 * 更改主备集群的标志
	 * 
	 * @param activeHost
	 *            主集群
	 * @param standbyHost
	 *            备份集群
	 */
	public synchronized static void setClusterInformation(String activeHost, String standbyHost, String clusterNameNodes) {
		LOG.info("Will switch active = " + activeHost + " standby = " + standbyHost);
		// 写成动态加载配置文件的形式，通过获取主机名
		QueryConfUtils.activeClusterFlag = activeHost;
		QueryConfUtils.standbyClusterFlag = standbyHost;
		QueryConfUtils.clusterNameNodes = clusterNameNodes;
	}

	/**
	 * 获取当前主集群的configuration对象
	 * 
	 * @return
	 */
	public static synchronized Configuration getActiveConfig() {
		return clusterConfig.get(activeClusterFlag);
	}

	/**
	 * 获取当前备份集群的configuration对象
	 * 
	 * @return
	 */
	public static synchronized Configuration getStandbyConfig() {
		LOG.debug("处于Standby状态的集群名称：" + standbyClusterFlag);
		return clusterConfig.get(standbyClusterFlag);

	}

	public static String getActiveClusterFlag() {
		return activeClusterFlag;
	}

	public static String getStandbyClusterFlag() {
		return standbyClusterFlag;
	}

	/**
	 * 获取主备集群的配置信息
	 * 
	 * @return
	 */
	public static Map<String, Configuration> getClusterConfig() {
		return clusterConfig;
	}

	/**
	 * 利用RPC方式获取当前正在处于Active状态的
	 * 
	 * @throws Exception
	 */
	public static synchronized void getActiveClusterFlagByRPC(String local, String remote) throws Exception {
		try {
			IManager manager = ManagerFactory.getManger(local, remote);
			if (manager == null) {
				throw new RuntimeException("init HDQS error ,can't load active or standby cluster  information!!");
			}
			Host host = manager.getActive();
			activeClusterFlag = host.getHost();
			standbyClusterFlag = manager.getStandBy().getHost();
			LOG.info("处于Active的集群是：" + activeClusterFlag);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void main(String[] args) {
		// getActiveClusterFlagByRPC();
	}
}
