package com.example.tianqi.util;

import android.text.TextUtils;

import com.example.tianqi.db.WeatherDB;
import com.example.tianqi.model.City;
import com.example.tianqi.model.County;
import com.example.tianqi.model.Province;

public class Utility {// �������������ص����ݵĹ�����
	/**
	 * �����ʹ�����������ص�ʡ������
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
					// �洢�����ݿ�
					weatherDB.insertProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ʹ�����������ص��м�����
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
					// �洢�����ݿ�
					weatherDB.insertCity(city);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * �����ʹ�����������ص��ؼ�����
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
					// �洢�����ݿ�
					weatherDB.insertCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}
