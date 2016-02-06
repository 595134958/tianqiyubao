package com.example.tianqi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherSQLiteOpenHelper extends SQLiteOpenHelper {
	/**
	 * province(省信息表):id是自增长主键，province_name表示省名，province_code表示省级代号
	 */
	public static final String CREATE_PROVINCE = "create table province("
			+ "id integer primary key autoincrement," + "province_name text,"
			+ "province_code text)";
	/**
	 * city(市信息表):id是自增长主键，city_name表示城市名，city_code表示市级代号，
	 * province_id表示关联province表的外键
	 */
	public static final String CREATE_CITY = "create table city("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "city_code text," + "province_id integer)";
	/**
	 * county(县信息表):id是自增长主键，county_name表示县名，county_code表示县级代号，
	 * city_id表示关联city表的外键
	 */
	public static final String CREATE_COUNTY = "create table county("
			+ "id integer primary key autoincrement," + "county_name text,"
			+ "county_code text," + "city_id integer)";

	public WeatherSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// 数据库第一次被创建时调用的方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	// 当数据库的版本号发生变化时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
