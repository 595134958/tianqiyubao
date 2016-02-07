package com.example.tianqi.db;

import java.util.ArrayList;
import java.util.List;

import com.example.tianqi.model.City;
import com.example.tianqi.model.County;
import com.example.tianqi.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WeatherDB {
	public static final String DB_NAME = "weather";// 数据库名
	public static final int VERSION = 1;// 数据库版本
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;

	private WeatherDB(Context context) {
		WeatherSQLiteOpenHelper dbHelper = new WeatherSQLiteOpenHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	// 这样可以保证全局范围内只有一个WeatherDB实例
	public synchronized static WeatherDB getInstance(Context context) {
		if (weatherDB == null) {
			weatherDB = new WeatherDB(context);
		}
		return weatherDB;
	}

	// 将province实例存储到数据库
	public void insertProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}
	}

	// 从数据库读取全国所有的省份信息
	public List<Province> queryProvince() {
		List<Province> listProvince = new ArrayList<Province>();
		Cursor cursor = db
				.query("province", null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String provinceName = cursor.getString(cursor
					.getColumnIndex("province_name"));
			String provinceCode = cursor.getString(cursor
					.getColumnIndex("province_code"));
			Province province = new Province(id, provinceName, provinceCode);
			listProvince.add(province);
		}
		if (cursor != null) {
			cursor.close();
		}
		return listProvince;
	}

	// 将City实例存储到数据库
	public void insertCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("city", null, values);
		}
	}

	// 从数据库读取全国所有的城市信息
	public List<City> queryCity(int provinceId) {
		List<City> listCity = new ArrayList<City>();
		Cursor cursor = db.query("city", null, "province_id=?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String cityName = cursor.getString(cursor
					.getColumnIndex("city_name"));
			String cityCode = cursor.getString(cursor
					.getColumnIndex("city_code"));
			City city = new City();
			city.setId(id);
			city.setCityName(cityName);
			city.setCityCode(cityCode);
			listCity.add(city);
		}
		if (cursor != null) {
			cursor.close();
		}
		return listCity;
	}

	// 将County实例存储到数据库
	public void insertCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("county", null, values);
		}
	}

	// 从数据库读取全国所有的县信息
	public List<County> queryCounty(int cityId) {
		List<County> listCounty = new ArrayList<County>();
		Cursor cursor = db.query("county", null, "city_id=?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String countyName = cursor.getString(cursor
					.getColumnIndex("county_name"));
			String countyCode = cursor.getString(cursor
					.getColumnIndex("county_code"));
			County county = new County();
			county.setId(id);
			county.setCountyName(countyName);
			county.setCountyCode(countyCode);
			listCounty.add(county);
		}
		if (cursor != null) {
			cursor.close();
		}
		return listCounty;
	}
}
