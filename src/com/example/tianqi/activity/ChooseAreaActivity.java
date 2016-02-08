package com.example.tianqi.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tianqi.R;
import com.example.tianqi.db.WeatherDB;
import com.example.tianqi.model.City;
import com.example.tianqi.model.County;
import com.example.tianqi.model.Province;
import com.example.tianqi.util.HttpCallbackListener;
import com.example.tianqi.util.HttpUtil;
import com.example.tianqi.util.Utility;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private TextView titleText;
	private ListView listView;
	private ProgressDialog progressDialog;// 对话框
	private ArrayAdapter<String> adapter;
	private WeatherDB weatherDB;
	private List<String> dataList = new ArrayList<String>();
	private List<Province> provinceList;// 省列表
	private List<City> cityList;// 市列表
	private List<County> countyList;// 县列表
	private Province selectedProvince;// 选中的省份
	private City selectedCity;// 选中的城市
	private int currentLevel;// 当前选中的级别

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("city_selected", false)) {//如果city_selected等于true，直接跳转到天气信息页面
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
		}

		titleText = (TextView) findViewById(R.id.title_text);
		listView = (ListView) findViewById(R.id.list_view);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		weatherDB = WeatherDB.getInstance(this);// 创建数据库实例
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);// 点击选中的省份
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);// 当前选中的城市
					queryCounties();
				} else if (currentLevel == LEVEL_COUNTY) {	
					String countyCode = countyList.get(position)
							.getCountyCode();// 获取选中的县级代号
					Intent intent = new Intent(ChooseAreaActivity.this,
							WeatherActivity.class);
					intent.putExtra("county_code", countyCode);// 向WeatherActivity活动传递选中的县级代号
					startActivity(intent);
					finish();
				}
			}
		});

		queryProvinces();// 加载省级数据

	}

	/**
	 * 查询全国所有的省，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryProvinces() {
		provinceList = weatherDB.queryProvince();// 从数据库读取全国所有的省份信息
		if (provinceList.size() > 0) {
			dataList.clear();// 清空集合里的数据
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());// 将省名存储到dataList中

			}
			adapter.notifyDataSetChanged();// 刷新adapter
			// listView.setSelection(0);
			titleText.setText("中国");// 更改标题
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");// 到服务器上查询
		}
	}

	/**
	 * 查询选中省内所有的城市，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	protected void queryCities() {
		cityList = weatherDB.queryCity(selectedProvince.getId());// 从数据库读取选中省内所有城市的信息
		if (cityList.size() > 0) {
			dataList.clear();// 清空集合里的数据
			for (City city : cityList) {
				dataList.add(city.getCityName());// 将城市名存储到dataList中
			}
			adapter.notifyDataSetChanged();// 刷新adapter
			// listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());// 更改标题
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");// 到服务器上查询
		}
	}

	/**
	 * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
	 */
	private void queryCounties() {
		countyList = weatherDB.queryCounty(selectedCity.getId());// 从数据库读取选中市内所有县的信息
		if (countyList.size() > 0) {
			dataList.clear();// 清空集合里的数据
			for (County county : countyList) {
				dataList.add(county.getCountyName());// 将县名存储到dataList中
			}
			adapter.notifyDataSetChanged();// 刷新adapter
			// listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());// 更改标题
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");// 到服务器上查询
		}
	}

	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 */
	private void queryFromServer(final String code, final String type) {

		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();// 调用进度对话框
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(weatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(weatherDB, response,
							selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(weatherDB,
							response, selectedCity.getId());
				}

				if (result) {
					// 通过runOnUiThread方法回到主线程处理逻辑
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();// 关闭进度对话框
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}

						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// 通过runOnUiThread方法回到主线程处理逻辑
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();// 关闭进度对话框
						Toast.makeText(ChooseAreaActivity.this, "加载失败",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
	}

	/**
	 * 显示进度对话框
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("提示");
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);// 不能点击取消对话框
		}
		progressDialog.show();
	}

	/**
	 * 关闭进度对话框
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * 捕获Back按键，根据当前的级别来判断，此时应该返回市列表、省列表、还是直接退出
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();// 返回到市列表
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();// 返回到省列表
		} else {
			finish();
		}
	}
}
