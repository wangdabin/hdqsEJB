package com.ceb.hdqs.wtc;

import org.apache.log4j.Logger;

import com.ceb.hdqs.sop.SopIntf;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPServiceInformation;

public class DefaultQueryBean extends AbstractQueryBean {

	public DefaultQueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	@Override
	public Reply service(TPServiceInformation tpServiceInfo) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfo);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		return getExceptionBuffer(tpServiceInfo, sendSop, "目前该交易暂不支持");
	}
}