package com.example.tianqi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherSQLiteOpenHelper extends SQLiteOpenHelper {
	/**
	 * province(ʡ��Ϣ��):id��������������province_name��ʾʡ����province_code��ʾʡ������
	 */
	public static final String CREATE_PROVINCE = "create table province("
			+ "id integer primary key autoincrement," + "province_name text,"
			+ "province_code text)";
	/**
	 * city(����Ϣ��):id��������������city_name��ʾ��������city_code��ʾ�м����ţ�
	 * province_id��ʾ����province������
	 */
	public static final String CREATE_CITY = "create table city("
			+ "id integer primary key autoincrement," + "city_name text,"
			+ "city_code text," + "province_id integer)";
	/**
	 * county(����Ϣ��):id��������������county_name��ʾ������county_code��ʾ�ؼ����ţ�
	 * city_id��ʾ����city������
	 */
	public static final String CREATE_COUNTY = "create table county("
			+ "id integer primary key autoincrement," + "county_name text,"
			+ "county_code text," + "city_id integer)";

	public WeatherSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	// ���ݿ��һ�α�����ʱ���õķ���
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTY);
	}

	// �����ݿ�İ汾�ŷ����仯ʱ����
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
