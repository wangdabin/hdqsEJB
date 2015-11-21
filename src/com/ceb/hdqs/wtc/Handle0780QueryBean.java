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
import com.ceb.hdqs.entity.SqcsEO;
import com.ceb.hdqs.service.HdqsServiceException;
import com.ceb.hdqs.service.SqcsService;
import com.ceb.hdqs.sop.SopIntf;
import com.ceb.hdqs.utils.DateTimeFormatUtils;
import com.ceb.hdqs.utils.HdqsUtils;
import com.ceb.hdqs.utils.JNDIUtils;
import com.ceb.hdqs.utils.TimerUtils;
import com.ceb.hdqs.wtc.form.Handle0780Form;

public class Handle0780QueryBean extends AbstractQueryBean {
	public Handle0780QueryBean() {
		setLogger(Logger.getLogger(getClass()));
	}

	public Reply service(TPServiceInformation tpServiceInfor) throws Exception {
		SopIntf recvSop = getSopFromWTC(tpServiceInfor);
		SopIntf sendSop = new SopIntf();
		setSystemHeadForSop(recvSop, sendSop);
		printRecvHeaderPkt(recvSop);

		Handle0780Form form = buildForm(recvSop);
		PjyjlEO record = buildExchangeRecord(recvSop, form);
		printRecvFormPkt(form, record);
		try {
			validateField(form);
		} catch (HdqsWtcException e) {
			getLogger().info(this.getExCode() + " sop invalid: " + e.getMessage());
			return getExceptionBuffer(tpServiceInfor, sendSop, e.getMessage());
		}
		getLogger().info("Start query:" + getExCode());
		TimerUtils timer = new TimerUtils();
		timer.start();
		PjyjlEO sRecord = saveExchangeRecord(record);
		try {
			SqcsService service = (SqcsService) JNDIUtils.lookup(SqcsService.class);
			WopOperationCode wopCode = WopOperationCode.codeToType(form.getWopmod());
			switch (wopCode) {
			case SAVE:
				validateAddField(form, record);
				service.save(form);
				getTpServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, "新增权限参数记录成功");
				break;
			case QUERY:
				List<SqcsEO> list = service.findByProperty(form, form.getQishbs() - 1, form.getCxunbs());
				long count = service.getCountByProperty(form);
				getTPServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, list, count);
				break;
			case UPDATE:
				validateUpdateField(form, record);
				service.update(form);
				getTpServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, "更新权限参数记录成功");

				break;
			case DELETE:
				validateDeleteField(form, record);
				service.delete(form);
				getTpServiceInfoAsResult(tpServiceInfor, sRecord, sendSop, "删除权限参数记录成功");

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

	private Handle0780Form buildForm(SopIntf recvSop) {
		Handle0780Form form = new Handle0780Form();
		String wopmod = recvSop.getStr(getFileRecv(), "WOPMOD").trim();
		if (HdqsUtils.isNotBlank(wopmod)) {
			form.setWopmod(Integer.valueOf(wopmod));
		}
		form.setHandid(recvSop.getStr(getFileRecv(), "HANDID").trim());
		form.setGuiydh(recvSop.getStr(getFileRecv(), "GUIYDH").trim());
		form.setGuiyxm(recvSop.getStr(getFileRecv(), "GUIYXM").trim());
		form.setGylxdm(recvSop.getStr(getFileRecv(), "GYLXDM").trim());
		form.setQyngrq(recvSop.getStr(getFileRecv(), "QYNGRQ").trim());
		form.setJshurq(recvSop.getStr(getFileRecv(), "JSHURQ").trim());
		form.setWeihrq(recvSop.getStr(getFileRecv(), "WEIHRQ").trim());
		form.setWeihgy(recvSop.getStr(getFileRecv(), "WEIHGY").trim());
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

	private void validateField(Handle0780Form form) throws HdqsWtcException {
		if (form.getWopmod() == null) {
			throw new HdqsWtcException("WOP操作方式不能为空");
		}
	}

	private void validateAddField(Handle0780Form form, PjyjlEO record) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getGuiydh())) {
			throw new HdqsWtcException("柜员代号不能为空");
		}
		if (HdqsUtils.isBlank(form.getGuiyxm())) {
			throw new HdqsWtcException("柜员姓名不能为空");
		}
		if (HdqsUtils.isBlank(form.getGylxdm())) {
			throw new HdqsWtcException("柜员角色不能为空");
		}
		if (HdqsUtils.isBlank(form.getQyngrq())) {
			throw new HdqsWtcException("权限起始日期不能为空");
		}
		if (HdqsUtils.isBlank(form.getJshurq())) {
			throw new HdqsWtcException("权限终止日期不能为空");
		}
		if (form.getGuiydh().length() > 6) {
			throw new HdqsWtcException("柜员代号长度不能超过6个字符");
		}
		if (form.getGuiyxm().length() > 22) {
			throw new HdqsWtcException("柜员姓名长度不能超过22个字符");
		}
		if (form.getGylxdm().length() > 2) {
			throw new HdqsWtcException("柜员角色长度不能超过2个字符");
		}
		if (form.getWeihxm().length() > 22) {
			throw new HdqsWtcException("维护人员姓名长度不能超过22个字符");
		}
		if (form.getJiluzt().length() > 1) {
			throw new HdqsWtcException("记录状态长度不能超过1个字符");
		}
	}

	private void validateUpdateField(Handle0780Form form, PjyjlEO record) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getHandid())) {
			throw new HdqsWtcException("对象ID不能为空");
		}
		if (HdqsUtils.isBlank(form.getGuiydh())) {
			throw new HdqsWtcException("柜员代号不能为空");
		}
		if (HdqsUtils.isBlank(form.getGuiyxm())) {
			throw new HdqsWtcException("柜员姓名不能为空");
		}
		if (HdqsUtils.isBlank(form.getGylxdm())) {
			throw new HdqsWtcException("柜员角色不能为空");
		}
		if (HdqsUtils.isBlank(form.getQyngrq())) {
			throw new HdqsWtcException("权限起始日期不能为空");
		}
		if (HdqsUtils.isBlank(form.getJshurq())) {
			throw new HdqsWtcException("权限终止日期不能为空");
		}
		if (form.getGuiydh().length() > 6) {
			throw new HdqsWtcException("柜员代号长度不能超过6个字符");
		}
		if (form.getGuiyxm().length() > 22) {
			throw new HdqsWtcException("柜员姓名长度不能超过22个字符");
		}
		if (form.getGylxdm().length() > 2) {
			throw new HdqsWtcException("柜员角色长度不能超过2个字符");
		}
		if (form.getWeihxm().length() > 22) {
			throw new HdqsWtcException("维护人员姓名长度不能超过22个字符");
		}
		if (form.getJiluzt().length() > 1) {
			throw new HdqsWtcException("记录状态长度不能超过1个字符");
		}
	}

	private void validateDeleteField(Handle0780Form form, PjyjlEO record) throws HdqsWtcException {
		if (HdqsUtils.isBlank(form.getHandid())) {
			throw new HdqsWtcException("对象ID不能为空");
		}
		if (HdqsUtils.isBlank(form.getGuiydh())) {
			throw new HdqsWtcException("柜员代号不能为空");
		}
	}

	/**
	 * 根据查询结果获取
	 * 
	 */
	private Reply getTPServiceInfoAsResult(TPServiceInformation tpServiceInfor, PjyjlEO record, SopIntf sendSop, List<SqcsEO> list,
			long count) {
		String[] inmap = new String[] { getFileSent() };
		byte[] sendData = new byte[1024];
		if (list == null || list.isEmpty()) {
			getLogger().warn("存在[0]条记录");
			setErrorMessageForSop(sendSop, "存在[0]条记录");
		} else {
			sendData = new byte[200 + 148 + list.size() * 594];
			setNormalMessageForSop(sendSop, record, list, count);
		}
		TypedCArray replyBuffer = getTypedCArray(sendData, inmap, sendSop);
		tpServiceInfor.setReplyBuffer(replyBuffer);
		return tpServiceInfor;
	}

	private void setNormalMessageForSop(SopIntf sendSop, PjyjlEO record, List<SqcsEO> list, long count) {
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
		sendSop.put(getFileSent(), getFileTable(), "GUIYDH", getStrArrayFromList(map.get("1")));
		sendSop.put(getFileSent(), getFileTable(), "GUIYXM", getStrArrayFromList(map.get("2")));
		sendSop.put(getFileSent(), getFileTable(), "GYLXDM", getStrArrayFromList(map.get("3")));
		sendSop.put(getFileSent(), getFileTable(), "QYNGRQ", getStrArrayFromList(map.get("4")));
		sendSop.put(getFileSent(), getFileTable(), "JSHURQ", getStrArrayFromList(map.get("5")));
		sendSop.put(getFileSent(), getFileTable(), "WEIHRQ", getStrArrayFromList(map.get("6")));
		sendSop.put(getFileSent(), getFileTable(), "WEIHGY", getStrArrayFromList(map.get("7")));
		sendSop.put(getFileSent(), getFileTable(), "WEIHXM", getStrArrayFromList(map.get("8")));
		sendSop.put(getFileSent(), getFileTable(), "JILUZT", getStrArrayFromList(map.get("9")));
	}

	/**
	 * 将遍历结果对象转换map对象
	 * 
	 * @param iter
	 * @return
	 */
	private Map<String, List<String>> convertResultToMap(List<SqcsEO> list) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		for (int i = 0; i < getFieldCount(); i++) {
			map.put("" + i, new ArrayList<String>());
		}
		for (SqcsEO item : list) {
			map.get("0").add(item.getId() + "");
			map.get("1").add(item.getGuiydh() == null ? "" : item.getGuiydh());
			map.get("2").add(item.getGuiyxm() == null ? "" : item.getGuiyxm());
			map.get("3").add(item.getGylxdm() == null ? "" : item.getGylxdm());
			map.get("4").add(item.getQyngrq() == null ? "" : item.getQyngrq());
			map.get("5").add(item.getJshurq() == null ? "" : item.getJshurq());
			map.get("6").add(item.getWeihrq() == null ? "" : item.getWeihrq());
			map.get("7").add(item.getWeihgy() == null ? "" : item.getWeihgy());
			map.get("8").add(item.getWeihxm() == null ? "" : item.getWeihxm());
			map.get("9").add(item.getJiluzt() == null ? "" : item.getJiluzt());
		}
		return map;
	}
}