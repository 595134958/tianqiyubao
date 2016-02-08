package com.example.tianqi.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

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

	/**
	 * �������������ص�������Ϣ��JSON����
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");// ������
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");// ����¶�
			String temp2 = weatherInfo.getString("temp2");// ����¶�
			String weatherDesp = weatherInfo.getString("weather");// ��������
			String publishTime = weatherInfo.getString("ptime");// ����ʱ��
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
					weatherDesp, publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��JSON�������������Ϣ�洢��SharedPreferences�ļ���
	 */
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		
		
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
		//SharedPreferences.Editor sp=context.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected",true);
		editor.putString("city_name",cityName );
		editor.putString("weather_code",weatherCode );
		editor.putString("temp1",temp1 );
		editor.putString("temp2",temp2 );
		editor.putString("weather_desp",weatherDesp );
		editor.putString("publish_time",publishTime );
		editor.putString("current_date", sdf.format(new Date()));//����ǰʱ��洢
		editor.commit();
	}
}
