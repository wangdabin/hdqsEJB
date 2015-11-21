package com.ceb.hdqs.wtc.form;

public class Handle0779Form extends AbstractForm {
	private static final long serialVersionUID = -3674422146696444131L;
	public static final String CZFSHI_QUERY = "0";
	public static final String CZFSHI_DOWNLOAD = "1";
	private String czfshi;// 操作方式：0、一般查询;1、下载查询 ;
	private String slbhao;
	private String fileType;// 1 PDF 2 excel 3两者都有

	public String getCzfshi() {
		return czfshi;
	}

	public void setCzfshi(String czfshi) {
		this.czfshi = czfshi;
	}

	public String getSlbhao() {
		return slbhao;
	}

	public void setSlbhao(String slbhao) {
		this.slbhao = slbhao;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String toLog() {
		StringBuilder buffer = new StringBuilder("**********0776&0779***********\r\n");
		buffer.append("操作方式:" + this.getCzfshi());
		buffer.append(",受理编号:" + getSlbhao());
		buffer.append(",文件类型:" + getFileType());
		buffer.append(",起始笔数:" + getQishbs());
		buffer.append(",查询笔数:" + getCxunbs());

		return buffer.toString();
	}
}