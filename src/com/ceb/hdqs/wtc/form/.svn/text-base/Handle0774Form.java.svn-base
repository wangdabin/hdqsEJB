package com.ceb.hdqs.wtc.form;

public class Handle0774Form extends AbstractForm {
	private static final long serialVersionUID = 7176751920629114130L;
	private Integer wopmod;// WOP操作方式0-新增,1-查询,2-修改,3-删除
	private String handid;
	private String szkmbm;// 授权编码
	private String formjb;// 授权柜员级别
	private String beizxx;// 授权原因
	private String qudaoo;// 渠道
	private String weihrq;// 维护日期
	private String weihgy;// 维护柜员
	private String weihxm;// 维护姓名
	private String beiy04;// 备用字段1
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

	public String getSzkmbm() {
		return szkmbm;
	}

	public void setSzkmbm(String szkmbm) {
		this.szkmbm = szkmbm;
	}

	public String getFormjb() {
		return formjb;
	}

	public void setFormjb(String formjb) {
		this.formjb = formjb;
	}

	public String getBeizxx() {
		return beizxx;
	}

	public void setBeizxx(String beizxx) {
		this.beizxx = beizxx;
	}

	public String getQudaoo() {
		return qudaoo;
	}

	public void setQudaoo(String qudaoo) {
		this.qudaoo = qudaoo;
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

	public String getBeiy04() {
		return beiy04;
	}

	public void setBeiy04(String beiy04) {
		this.beiy04 = beiy04;
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
		StringBuilder buffer = new StringBuilder("**********0774***********\r\n");
		buffer.append("WOP操作方式(0-新增,1-查询,2-修改,3-删除):" + getWopmod());
		buffer.append(",对象ID:" + getHandid());
		buffer.append(",授权编码:" + getSzkmbm());
		buffer.append(",授权柜员级别:" + getFormjb());
		buffer.append(",授权原因:" + getBeizxx());
		buffer.append(",表单渠道:" + getQudaoo());
		buffer.append(",维护日期:" + getWeihrq());
		buffer.append(",维护柜员:" + getWeihgy());
		buffer.append(",维护姓名:" + getWeihxm());
		buffer.append(",备用字段1:" + getBeiy04());
		buffer.append(",记录状态:" + getJiluzt());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());

		return buffer.toString();
	}

	public static void main(String[] args) {
		Handle0774Form form = new Handle0774Form();
		form.setWopmod(1);
		switch (form.getWopmod()) {
		case 0:
			System.out.println("0-新增");
			break;
		case 1:
			System.out.println("1-查询");
			break;
		case 2:
			System.out.println("2-修改");
			break;
		case 3:
			System.out.println("3-删除");
			break;
		default:
			System.out.println("不支持的操作");
		}
	}
}