package com.example.tianqi.util;

public interface HttpCallbackListener {
	void onFinish(String response);//当服务器成功响应请求时调用

	void onError(Exception e);//当网络出现错误时调用
}
