package com.ceb.hdqs.po;

/**
 * 本类字段默认值不要修改,一些逻辑依赖默认值
 * 
 * @author user
 * 
 */
public class Authorize {
	// added by kehua
	// 前端授权 后台不授权的情况 授权原因及授权级别 设置为 空
	// 前台授权是（jiaoybz=1）后台授权否 这种情况 请将授权原因和授权级别 设置为空
	//public static final String NO_AUTH_MSG = "后台不授权|+|0|+|";
	public static final String NO_AUTH_MSG = "";

	private boolean needAuth = false;
	private int guiyjb = 0;// 授权柜员级别
	private String beizxx;// 授权原因

	public Authorize() {
		reset();
	}

	public boolean isNeedAuth() {
		return needAuth;
	}

	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}

	public int getGuiyjb() {
		return guiyjb;
	}

	public void setGuiyjb(int guiyjb) {
		this.guiyjb = guiyjb;
	}

	public String getBeizxx() {
		return beizxx;
	}

	public void setBeizxx(String beizxx) {
		this.beizxx = beizxx;
	}

	public String getPrintMsg() {
		if (getGuiyjb() == 0) {
			return NO_AUTH_MSG;
		} else {
			return this.getBeizxx() + "|+|" + this.getGuiyjb() + "|+|";
		}
	}

	public void reset() {
		this.setNeedAuth(false);
		this.setGuiyjb(0);
		this.setBeizxx("后台不授权");
	}
}