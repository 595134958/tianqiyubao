package com.example.tianqi.model;

/**
 * city(市信息表):id是自增长主键，cityName表示城市名，cityCode表示市级代号，
 * provinceId表示关联province表的外键hahahahah
 */
public class City {
	private int id;
	private String cityName;
	private String cityCode;
	private int provinceId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

}
