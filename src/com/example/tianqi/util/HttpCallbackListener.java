package com.example.tianqi.util;

public interface HttpCallbackListener {
	void onFinish(String response);//���������ɹ���Ӧ����ʱ����

	void onError(Exception e);//��������ִ���ʱ����
}
