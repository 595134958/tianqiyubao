package com.example.tianqi.model;

/**
 * province(省信息表):id是自增长主键，provinceName表示省名，provinceCode表示省级代号
 */
public class Province {
	private int id;
	private String provinceName;
	private String provinceCode;

	
	public Province() {
		super();
	}

	public Province(int id, String provinceName, String provinceCode) {
		super();
		this.id = id;
		this.provinceName = provinceName;
		this.provinceCode = provinceCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

}
