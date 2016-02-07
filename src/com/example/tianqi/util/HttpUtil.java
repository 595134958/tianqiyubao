package com.example.tianqi.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {// �������ӹ�����
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);// ���ӳ�ʱ
					connection.setReadTimeout(8000);// ������Ӧ��ʱ, ��������,���������ٳٲ�����Ӧ					
					InputStream is = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(is));// ����������߶�ȡЧ��
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// �ص�onFinish()����
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						// �ص�onError()����
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();// �ر���������
					}
				}
			}
		}).start();

	}
}