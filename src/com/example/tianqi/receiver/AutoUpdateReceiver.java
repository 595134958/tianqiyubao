package com.example.tianqi.receiver;

import com.example.tianqi.service.AutoUpdateService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdateReceiver extends BroadcastReceiver{//广播

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i=new Intent(context,AutoUpdateService.class);
		context.startService(i);//启动服务
		
	}

}
