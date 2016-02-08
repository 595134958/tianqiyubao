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
	private ProgressDialog progressDialog;// �Ի���
	private ArrayAdapter<String> adapter;
	private WeatherDB weatherDB;
	private List<String> dataList = new ArrayList<String>();
	private List<Province> provinceList;// ʡ�б�
	private List<City> cityList;// ���б�
	private List<County> countyList;// ���б�
	private Province selectedProvince;// ѡ�е�ʡ��
	private City selectedCity;// ѡ�еĳ���
	private int currentLevel;// ��ǰѡ�еļ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_area);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (prefs.getBoolean("city_selected", false)) {//���city_selected����true��ֱ����ת��������Ϣҳ��
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
		}

		titleText = (TextView) findViewById(R.id.title_text);
		listView = (ListView) findViewById(R.id.list_view);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		weatherDB = WeatherDB.getInstance(this);// �������ݿ�ʵ��
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);// ���ѡ�е�ʡ��
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);// ��ǰѡ�еĳ���
					queryCounties();
				} else if (currentLevel == LEVEL_COUNTY) {	
					String countyCode = countyList.get(position)
							.getCountyCode();// ��ȡѡ�е��ؼ�����
					Intent intent = new Intent(ChooseAreaActivity.this,
							WeatherActivity.class);
					intent.putExtra("county_code", countyCode);// ��WeatherActivity�����ѡ�е��ؼ�����
					startActivity(intent);
					finish();
				}
			}
		});

		queryProvinces();// ����ʡ������

	}

	/**
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	private void queryProvinces() {
		provinceList = weatherDB.queryProvince();// �����ݿ��ȡȫ�����е�ʡ����Ϣ
		if (provinceList.size() > 0) {
			dataList.clear();// ��ռ����������
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());// ��ʡ���洢��dataList��

			}
			adapter.notifyDataSetChanged();// ˢ��adapter
			// listView.setSelection(0);
			titleText.setText("�й�");// ���ı���
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");// ���������ϲ�ѯ
		}
	}

	/**
	 * ��ѯѡ��ʡ�����еĳ��У����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	protected void queryCities() {
		cityList = weatherDB.queryCity(selectedProvince.getId());// �����ݿ��ȡѡ��ʡ�����г��е���Ϣ
		if (cityList.size() > 0) {
			dataList.clear();// ��ռ����������
			for (City city : cityList) {
				dataList.add(city.getCityName());// ���������洢��dataList��
			}
			adapter.notifyDataSetChanged();// ˢ��adapter
			// listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());// ���ı���
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");// ���������ϲ�ѯ
		}
	}

	/**
	 * ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	private void queryCounties() {
		countyList = weatherDB.queryCounty(selectedCity.getId());// �����ݿ��ȡѡ�����������ص���Ϣ
		if (countyList.size() > 0) {
			dataList.clear();// ��ռ����������
			for (County county : countyList) {
				dataList.add(county.getCountyName());// �������洢��dataList��
			}
			adapter.notifyDataSetChanged();// ˢ��adapter
			// listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());// ���ı���
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");// ���������ϲ�ѯ
		}
	}

	/**
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
	 */
	private void queryFromServer(final String code, final String type) {

		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();// ���ý��ȶԻ���
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
					// ͨ��runOnUiThread�����ص����̴߳����߼�
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();// �رս��ȶԻ���
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
				// ͨ��runOnUiThread�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();// �رս��ȶԻ���
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
	}

	/**
	 * ��ʾ���ȶԻ���
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setTitle("��ʾ");
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);// ���ܵ��ȡ���Ի���
		}
		progressDialog.show();
	}

	/**
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	/**
	 * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳�
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();// ���ص����б�
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();// ���ص�ʡ�б�
		} else {
			finish();
		}
	}
}
