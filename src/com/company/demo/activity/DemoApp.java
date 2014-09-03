package com.company.demo.activity;

import android.content.Context;

import com.apple.activity.BaseApplication;
import com.apple.common.BaseHttpRes;
import com.apple.http.impl.AsyncHttpClientImpl;
import com.apple.utils.DeviceInfo;
import com.apple.utils.MySharedPreferencesMgr;

public class DemoApp extends BaseApplication {
	
	
	public AsyncHttpClientImpl asyncImpl;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		asyncImpl=AsyncHttpClientImpl.getHupuHttpClient();
	}

	@Override
	protected BaseHttpRes initBaseHttpRes() {
		// TODO Auto-generated method stub
		return DemoHttpRes.getInstance();
	}

	
	
}
