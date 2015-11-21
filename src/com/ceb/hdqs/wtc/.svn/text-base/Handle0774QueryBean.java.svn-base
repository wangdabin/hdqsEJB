package com.ceb.hdqs.wtc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import weblogic.wtc.jatmi.Reply;
import weblogic.wtc.jatmi.TPServiceInformation;
import weblogic.wtc.jatmi.TypedCArray;

import com.ceb.hdqs.constants.WopOperationCode;
import com.ceb.hdqs.entity.PjyjlEO;
import com.ceb.hdqs.entity.SqbmEO;
import com.ceb.hdqs.service.AuthorizeService;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0774Form;

public class Handle0774QueryBean extends AbstractQueryBean {
	public Handle0774QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	public Reply service(TPServiceInformation tpServiceInfor) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		printRecvHeaderPkt(recvSop);

		Handle0774Form form = buildForm(recvSop);
		PjyjlEO record = buildExchangeRecord(recvSop, form);
		printRecvFormPkt(form, record);
		try {
			validateField(form);
		} catch (HdqsWtcException e) {
			getLogger().info(getExCode() + " sop invalid: " + e.getMessage());
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		}
		getLogger().info("Start query:" + getExCode());
		TimerUtils timer = new TimerUtils();
		timer.start();
		PjyjlEO sRecord = saveExchangeRecord(record);
		try {
			AuthorizeService service = (AuthorizeService) JNDIUtils.lookup(AuthorizeService.class);
			WopOperationCode wopCode = WopOperationCode.codeToType(form.getWopmod());
			switch (wopCode) {
			case SAVE:
				validateAddField(form);
				service.save(form);
				getTpServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, "新增授权编码记录成功");
				break;
			case QUERY:
				List<SqbmEO> list = service.findByProperty(form, form.getQishbs() - 1, form.getCxunbs());
				long count = service.getCountByProperty(form);
				getTPServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, list, count);
				break;
			case UPDATE:
				validateUpdateField(form);
				service.update(form);
				getTpServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, "修改授权编码记录成功");
				break;
			case DELETE:
				validateDeleteField(form);
				service.delete(form);
				getTpServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, "删除授权编码记录成功");
				break;
			default:
				getLogger().error("不支持的操作");
				return getExceptionBuffer(tpServiceInfor, sendSop, "不支持的操作");
			}
		} catch (HdqsWtcException e) {
			timer.stop();
			getLogger().error(this.getExCode() + " sop invalid: " + e.getMessage());
			updateExchangeRunErrStatus(sRecord);
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		} catch (HdqsServiceException e) {
			timer.stop();
			getLogger().error(e.getMessage());
			updateExchangeRunErrStatus(sRecord);
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		} catch (Exception e) {
			timer.stop();
			getLogger().error(e.getMessage(), e);
			updateExchangeRunErrStatus(sRecord);
			return getExceptionBuffer(tpServiceInfor, sendSop, "后台查询异常");
		}
		timer.stop();
		getLogger().info(getExCode() + " query complete, Cost time(ms)=" + timer.getExecutionTime());
		updateExchangeEndTime(sRecord);
		return tpServiceInfor;
	}

	private Handle0774Form buildForm(SopIntf recvSop) {
		Handle0774Form form = new Handle0774Form();
		String wopmod = recvSop.getStr(getFileRecv(), "WOPMOD").trim();
		if (HdqsUtils.isNotBlank(wopmod)) {
			form.setWopmod(Integer.valueOf(wopmod));
		}
		form.setHandid(recvSop.getStr(getFileRecv(), "HANDID").trim());
		form.setSzkmbm(recvSop.getStr(getFileRecv(), "SZKMBM").trim());
		form.setFormjb(recvSop.getStr(getFileRecv(), "GUIYJB").trim());
		form.setBeizxx(recvSop.getStr(getFileRecv(), "BEIZXX").trim());
		form.setQudaoo(recvSop.getStr(getFileRecv(), "QUDAOO").trim());
		form.setWeihrq(recvSop.getStr(getFileRecv(), "WEIHRQ").trim());
		form.setWeihgy(recvSop.getStr(getFileRecv(), "WEIHGY").trim());
		form.setBeiy04(recvSop.getStr(getFileRecv(), "BEIY04").trim());
		form.setWeihxm(recvSop.getStr(getFileRecv(), "WEIHXM").trim());
		form.setJiluzt(recvSop.getStr(getFileRecv(), "JILUZT").trim());
		String startNum = recvSop.getStr(getFileRecv(), "QISHBS").trim();
		if (HdqsUtils.isNotBlank(startNum)) {
			form.setQishbs(Integer.parseInt(startNum));
		}
		String queryNum = recvSop.getStr(getFileRecv(), "CXUNBS").trim();
		if (HdqsUtils.isNotBlank(queryNum)) {
			form.setCxunbs(Integer.parseInt(queryNum));
		}

		return form;
	}

	/**
	 * 操作方式验证
	 * 
	 * @param sendSop
	 * @param wopmod
	 */
	private void validateField(Handle0774Form form) throws HdqsWtcException {
		if (form.getWopmod() == null) {
			throw new HdqsWtcException("WOP操作方式不能为空");
		}
	}

	/**
	 * 必输字段验证
	 * 
	 * @param sendSop
	 * @param form
	 */
	private void validateAddField(Handle0774Form form) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getSzkmbm())) {
			throw new HdqsWtcException("授权编码不能为空");
		}
		if (HdqsUtils.isBlank(form.getFormjb())) {
			throw new HdqsWtcException("授权柜员级别不能为空");
		}
		if (HdqsUtils.isBlank(form.getBeizxx())) {
			throw new HdqsWtcException("授权原因不能为空");
		}
		if (HdqsUtils.isBlank(form.getQudaoo())) {
			throw new HdqsWtcException("渠道不能为空");
		}
		if (form.getSzkmbm().length() > 10) {
			throw new HdqsWtcException("授权编码长度不能超过10个字符");
		}
		if (form.getBeizxx().length() > 128) {
			throw new HdqsWtcException("授权原因长度不能超过128个字符");
		}
		if (form.getQudaoo().length() > 3) {
			throw new HdqsWtcException("渠道长度不能超过3个字符");
		}
		if (form.getBeiy04().length() > 4) {
			throw new HdqsWtcException("备用字段1长度不能超过4个字符");
		}
		if (form.getWeihxm().length() > 22) {
			throw new HdqsWtcException("维护姓名长度不能超过22个字符");
		}
		if (form.getJiluzt().length() > 1) {
			throw new HdqsWtcException("记录状态长度不能超过1个字符");
		}
	}

	/**
	 * 必输字段验证
	 * 
	 * @param sendSop
	 * @param form
	 */
	private void validateUpdateField(Handle0774Form form) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getHandid())) {
			throw new HdqsWtcException("对象ID不能为空");
		}
		if (HdqsUtils.isBlank(form.getSzkmbm())) {
			throw new HdqsWtcException("授权编码不能为空");
		}
		if (HdqsUtils.isBlank(form.getFormjb())) {
			throw new HdqsWtcException("授权柜员级别不能为空");
		}
		if (HdqsUtils.isBlank(form.getBeizxx())) {
			throw new HdqsWtcException("授权原因不能为空");
		}
		if (HdqsUtils.isBlank(form.getQudaoo())) {
			throw new HdqsWtcException("渠道不能为空");
		}
		if (form.getSzkmbm().length() > 10) {
			throw new HdqsWtcException("授权编码长度不能超过10个字节");
		}
		if (form.getBeizxx().length() > 128) {
			throw new HdqsWtcException("授权原因长度不能超过128个字节");
		}
		if (form.getQudaoo().length() > 3) {
			throw new HdqsWtcException("渠道长度不能超过3个字节");
		}
		if (form.getBeiy04().length() > 4) {
			throw new HdqsWtcException("备用字段1长度不能超过4个字节");
		}
		if (form.getWeihxm().length() > 20) {
			throw new HdqsWtcException("维护姓名长度不能超过20个字节");
		}
		if (form.getJiluzt().length() > 1) {
			throw new HdqsWtcException("记录状态长度不能超过1个字节");
		}
	}

	/**
	 * 必输字段验证
	 * 
	 * @param sendSop
	 * @param form
	 */
	private void validateDeleteField(Handle0774Form form) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getHandid())) {
			throw new HdqsWtcException("对象ID不能为空");
		}
	}

	/**
	 * 根据查询结果获取
	 * 
	 */
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, PjyjlEO record, SopIntf sendSop, List<SqbmEO> list,
			long count) {
		String[] inmap = new String[] { getFileSent() };
		byte[] sendData = new byte[1024];
		if (list == null || list.isEmpty()) {
			getLogger().warn("存在[0]条记录");
			setErrorMessageForSop(sendSop, "存在[0]条记录");
		} else {
			sendData = new byte[200 + 148 + list.size() * 197];
			setNormalMessageForSop(sendSop, record, list, count);
		}
		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		return tpServiceInfor;
	}

	/**
	 * 报文正常输出
	 * 
	 * @param sendSop
	 * @param list
	 * @param guiyuanSEQ
	 * @param count
	 */
	private void setNormalMessageForSop(SopIntf sendSop, PjyjlEO record, List<SqbmEO> list, long count) {
		sendSop.put(null, "JIAOYM", getExCode());
		long currentT = System.currentTimeMillis();
		sendSop.put(null, "JIOYRQ", DateTimeFormatUtils.formatDate(currentT, "yyyymmdd"));
		sendSop.put(null, "JIOYSJ", Integer.valueOf(DateTimeFormatUtils.formatDate(currentT, "HHmmss")));
		sendSop.put(null, "GUIYLS", record.getGuiyls());
		Short FDCWJYXH = 0;
		sendSop.put(null, "CWJYXH", FDCWJYXH);
		sendSop.put(null, SopIntf.TPU_RETCODE, SopIntf.SOP_SUCC);
		sendSop.put(getFileSent(), "BISHUU", String.valueOf(count));
		Map<String, List<String>> map = convertResultToMap(list);
		sendSop.put(getFileSent(), getFileTable(), "HANDID", getStrArrayFromList(map.get("0")));
		sendSop.put(getFileSent(), getFileTable(), "SZKMBM", getStrArrayFromList(map.get("1")));
		sendSop.put(getFileSent(), getFileTable(), "GUIYJB", getStrArrayFromList(map.get("2")));
		sendSop.put(getFileSent(), getFileTable(), "BEIZXX", getStrArrayFromList(map.get("3")));
		sendSop.put(getFileSent(), getFileTable(), "QUDAOO", getStrArrayFromList(map.get("4")));
		sendSop.put(getFileSent(), getFileTable(), "WEIHRQ", getStrArrayFromList(map.get("5")));
		sendSop.put(getFileSent(), getFileTable(), "WEIHGY", getStrArrayFromList(map.get("6")));
		sendSop.put(getFileSent(), getFileTable(), "BEIY04", getStrArrayFromList(map.get("7")));
		sendSop.put(getFileSent(), getFileTable(), "WEIHXM", getStrArrayFromList(map.get("8")));
		sendSop.put(getFileSent(), getFileTable(), "JILUZT", getStrArrayFromList(map.get("9")));
	}

	/**
	 * 将遍历结果对象转换map对象
	 * 
	 * @param iter
	 * @return
	 */
	private Map<String, List<String>> convertResultToMap(List<SqbmEO> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < getFieldCount(); i++) {
			map.put("" + i, new ArrayList<String>());
		}
		for (SqbmEO item : list) {
			map.get("0").add(item.getId() + "");
			map.get("1").add(item.getSzkmbm() == null ? "" : item.getSzkmbm().trim());
			map.get("2").add(item.getGuiyjb() + "");
			map.get("3").add(item.getBeizxx() == null ? "" : item.getBeizxx().trim());
			map.get("4").add(item.getQudaoo() == null ? "" : item.getQudaoo().trim());
			map.get("5").add(item.getWeihrq() == null ? "" : item.getWeihrq().trim());
			map.get("6").add(item.getWeihgy() == null ? "" : item.getWeihgy().trim());
			map.get("7").add(item.getBeiy04() == null ? "" : item.getBeiy04().trim());
			map.get("8").add(item.getWeihxm() == null ? "" : item.getWeihxm().trim());
			map.get("9").add(item.getJiluzt() == null ? "" : item.getJiluzt().trim());
		}
		return map;
	}
}