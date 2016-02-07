package com.example.tianqi.util;

import android.text.TextUtils;

import com.example.tianqi.db.WeatherDB;
import com.example.tianqi.model.City;
import com.example.tianqi.model.County;
import com.example.tianqi.model.Province;

public class Utility {// 解析服务器返回的数据的工具类
	/**
	 * 解析和处理服务器返回的省级数据
	 */

	public synchronized static boolean handleProvincesResponse(
			WeatherDB weatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// 存储到数据库
					weatherDB.insertProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCitiesResponse(
	WeatherDB weatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String p : allCities) {
					String[] array = p.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					// 存储到数据库
					weatherDB.insertCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public synchronized static boolean handleCountiesResponse(
			WeatherDB weatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String p : allCounties) {
					String[] array = p.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					// 存储到数据库
					weatherDB.insertCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}
