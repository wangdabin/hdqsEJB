package com.ceb.bank.context;

public class Output0777Context extends OutPdfContext {
	private static final long serialVersionUID = -6826782541935929808L;
	private ZhanghContext zhanghCtx = new ZhanghContext();
	private KehuzhContext kehuzhCtx = new KehuzhContext();
	private KehuhaoContext kehuhaoCtx = new KehuhaoContext();
	private String zhangh;// 保存查询条件中的ZHANGH,NRA的一些特殊条件,KEHUHAO或KEHUZH查询时，遍历ZHANGH时需要对此字段赋值

	public ZhanghContext getZhanghCtx() {
		return zhanghCtx;
	}

	public void setZhanghCtx(ZhanghContext zhanghCtx) {
		this.zhanghCtx = zhanghCtx;
	}

	public KehuzhContext getKehuzhCtx() {
		return kehuzhCtx;
	}

	public void setKehuzhCtx(KehuzhContext kehuzhCtx) {
		this.kehuzhCtx = kehuzhCtx;
	}

	public KehuhaoContext getKehuhaoCtx() {
		return kehuhaoCtx;
	}

	public void setKehuhaoCtx(KehuhaoContext kehuhaoCtx) {
		this.kehuhaoCtx = kehuhaoCtx;
	}

	public String getZhangh() {
		return zhangh;
	}

	public void setZhangh(String zhangh) {
		this.zhangh = zhangh;
	}
}