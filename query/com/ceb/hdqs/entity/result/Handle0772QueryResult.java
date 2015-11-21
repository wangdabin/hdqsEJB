package com.ceb.hdqs.entity.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对私定期查询结果
 * 
 * @author user
 * 
 */
public class Handle0772QueryResult extends AbstractQueryResult implements Serializable {
	private static final long serialVersionUID = -7114512753248140260L;

	private List<PrivateFixQueryItem> itemList = new ArrayList<PrivateFixQueryItem>();

	public List<PrivateFixQueryItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<PrivateFixQueryItem> itemList) {
		this.itemList = itemList;
	}

	public String getKehuzh() {
		return kehuzh;
	}

	public void setKehuzh(String kehuzh) {
		this.kehuzh = kehuzh;
	}

	public long getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	public String getKehuhao() {
		return kehuhao;
	}

	public void setKehuhao(String kehuhao) {
		this.kehuhao = kehuhao;
	}

	public String getKhzwm() {
		return khzwm;
	}

	public void setKhzwm(String khzwm) {
		this.khzwm = khzwm;
	}

	private String kehuzh;
	private long totalNum;
	private String kehuhao;
	private String khzwm;

	@Override
	public String parseToString(String huobdh) {
		// TODO Auto-generated method stub
		return null;
	}

}
