package com.ceb.hdqs.query.entity;

public enum EnumSizeUnit {

	KB("KB"), MB("MB"), B("B");

	private String unit;

	EnumSizeUnit(String unit) {
		this.unit = unit;
	}

	public String getDisplay(){
		return this.unit;
	}
	
}
