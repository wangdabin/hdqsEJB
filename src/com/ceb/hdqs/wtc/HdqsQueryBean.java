package com.ceb.hdqs.wtc;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPException;
import weblogic.wtc.jatmi.TPServiceInformation;

import com.ceb.hdqs.config.ConfigLoader;
import com.ceb.hdqs.sop.SopIntf;

public class HdqsQueryBean implements SessionBean {
	private static final long serialVersionUID = 8096373861244820710L;
	private static final Logger log = Logger.getLogger(HdqsQueryBean.class);
	private volatile static boolean isRestart = true;

	private AbstractQueryBean defaultBean = new DefaultQueryBean();

	public Reply service(TPServiceInformation tpServiceInfor) throws TPException, RemoteException {
		if (isRestart) {
			// 根据hdqs.properties配置文件重新设置log4j输出级别
			ConfigLoader.getInstance().refreshLog4j();
			isRestart = false;
		}
		SopIntf recvSop = defaultBean.getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		defaultBean.setSystemHeadForSop(recvSop, sendSop);

		String exCode = recvSop.getStr(null, "JIAOYM");
		/*
		 * String regKey = "needvalid." + exCode + ".mac"; String regValue =
		 * ConfigLoader.getInstance().getProperty(regKey); if
		 * (HdqsUtils.isNotBlank(regValue) &&
		 * regValue.trim().equalsIgnoreCase("true")) { String
		 * validateReturnValue = MACUtils.validateMAC(recvSop,
		 * serviceData.carray); if
		 * (validateReturnValue.equals(MACUtils.MAC_EXCEPTION_VALUE)) { return
		 * getExceptionBuffer(tpServiceInfor, sendData, inmap, sendSop,
		 * "mac验证出现异常"); } else if
		 * (!validateReturnValue.equals(MACUtils.MAC_SUCCESS_VALUE)) { return
		 * getExceptionBuffer(tpServiceInfor, sendData, inmap, sendSop,
		 * "mac验证失败"); } }
		 */
		AbstractQueryBean queryBean = QueryBeanFactory.getInstance().getBean(exCode);
		if (queryBean == null) {
			log.error("输入的交易码" + exCode + "不支持");
			return defaultBean.getExceptionBuffer(tpServiceInfor, recvSop, "输入的交易码不支持");
		}
		if (!ConfigLoader.getInstance().getProperty("menu." + exCode + ".switch").equalsIgnoreCase("true")) {
			log.error("菜单" + exCode + "暂未开放");
			return defaultBean.getExceptionBuffer(tpServiceInfor, recvSop, "菜单" + exCode + "暂未开放");
		}
		try {
			return queryBean.service(tpServiceInfor);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return defaultBean.getExceptionBuffer(tpServiceInfor, sendSop, "后台异常");
		}
	}

	public void ejbActivate() throws EJBException, RemoteException {

	}

	public void ejbPassivate() throws EJBException, RemoteException {

	}

	public void ejbRemove() throws EJBException, RemoteException {

	}

	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {

	}
}