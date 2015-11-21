package com.ceb.hdqs.wtc.form;

public class Handle0780Form extends AbstractForm {
	private static final long serialVersionUID = 5748888564502244919L;
	private Integer wopmod;// WOP操作方式0-新增,1-查询,2-修改,3-删除
	private String handid;
	private String guiydh;// 柜员代号
	private String guiyxm;// 柜员姓名
	private String gylxdm;// 柜员角色代码
	private String qyngrq;// 启用日期
	private String jshurq;// 结束日期
	private String weihrq;// 维护日期
	private String weihgy;// 维护柜员
	private String weihxm;// 维护姓名
	private String jiluzt;// 记录状态

	public Integer getWopmod() {
		return wopmod;
	}

	public void setWopmod(Integer wopmod) {
		this.wopmod = wopmod;
	}

	public String getHandid() {
		return handid;
	}

	public void setHandid(String handid) {
		this.handid = handid;
	}

	public String getGuiydh() {
		return guiydh;
	}

	public void setGuiydh(String guiydh) {
		this.guiydh = guiydh;
	}

	public String getGuiyxm() {
		return guiyxm;
	}

	public void setGuiyxm(String guiyxm) {
		this.guiyxm = guiyxm;
	}

	public String getGylxdm() {
		return gylxdm;
	}

	public void setGylxdm(String gylxdm) {
		this.gylxdm = gylxdm;
	}

	public String getQyngrq() {
		return qyngrq;
	}

	public void setQyngrq(String qyngrq) {
		this.qyngrq = qyngrq;
	}

	public String getJshurq() {
		return jshurq;
	}

	public void setJshurq(String jshurq) {
		this.jshurq = jshurq;
	}

	public String getWeihrq() {
		return weihrq;
	}

	public void setWeihrq(String weihrq) {
		this.weihrq = weihrq;
	}

	public String getWeihgy() {
		return weihgy;
	}

	public void setWeihgy(String weihgy) {
		this.weihgy = weihgy;
	}

	public String getWeihxm() {
		return weihxm;
	}

	public void setWeihxm(String weihxm) {
		this.weihxm = weihxm;
	}

	public String getJiluzt() {
		return jiluzt;
	}

	public void setJiluzt(String jiluzt) {
		this.jiluzt = jiluzt;
	}

	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0780***********\r\n");
		buffer.append("WOP操作方式(0-新增,1-查询,2-修改,3-删除):" + getWopmod());
		buffer.append(",对象ID:" + getHandid());
		buffer.append(",柜员代号:" + getGuiydh());
		buffer.append(",柜员姓名:" + getGuiyxm());
		buffer.append(",柜员角色代码:" + getGylxdm());
		buffer.append(",启用日期:" + getQyngrq());
		buffer.append(",终止日期:" + getJshurq());
		buffer.append(",维护日期:" + getWeihrq());
		buffer.append(",维护柜员:" + getWeihgy());
		buffer.append(",维护姓名:" + getWeihxm());
		buffer.append(",记录状态:" + getJiluzt());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());

		return buffer.toString();
	}
}