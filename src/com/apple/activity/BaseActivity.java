package com.apple.activity;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.apple.common.HttpCallback;
import com.apple.common.BaseHttpClient;
import com.apple.http.async.RequestParams;
import com.apple.http.impl.AsyncHttpClientImpl;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity implements
		HttpCallback {

	public RequestParams mParams;
	public Click click;
	HashMap<String, String> UMENG_MAP = new HashMap<String, String>();
	private Context mcontext;
	protected boolean bBackGround;
	public BaseApplication app;
	public String mDeviceId;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		click = new Click();
		mcontext=this.getApplicationContext();
		if (mParams == null)
			mParams = new RequestParams();
		app=(BaseApplication) getApplication();
		mDeviceId =app.mDeviceId;
		initView(bundle);
		initLisener();
		initData(bundle);
	}

	

	@Override
	public void onSuccess(String content, Object object, int reqType) {
		// TODO Auto-generated method stub
	}

	public void setOnClickListener(int id) {
		if (click == null)
			click = new Click();
		findViewById(id).setOnClickListener(click);
	}

	private class Click implements OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			treatClickEvent(id);
			treatClickEvent(v);
		}
	}

	/**
	 * @param categray
	 *            eventID
	 * @param key
	 *            key
	 * @param value
	 *            value
	 * */
	public void sendUmeng(String categray, String key, String value) {
		UMENG_MAP = new HashMap<String, String>();
		UMENG_MAP.put(key, value);
		MobclickAgent.onEvent(this, categray, UMENG_MAP);
	}

	public void sendUmeng(String categray, String key) {
		MobclickAgent.onEvent(this, categray, key);
	}

	public void showToast(String s) {
		Toast toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
		toast.show();
	}

	public void treatClickEvent(int id) {

	}

	public void treatClickEvent(View v) {

	}

	/**
	 * 初始化布局和控件
	 * 
	 * @param bundle
	 */
	protected abstract void initView(Bundle bundle);

	/**
	 * 初始化监听
	 */
	protected abstract void initLisener();

	/**
	 * 初始化数据
	 */
	protected abstract void initData(Bundle bundle);
	
	

	@Override
	public void onFailure(Throwable error, String content, int reqType) {
		// TODO Auto-generated method stub

	}

	public void sendRequest(int reqType, String url, RequestParams params,
			BaseHttpClient baseHttpClient, boolean showDialog) {
		// TODO Auto-generated method stub
		if(baseHttpClient!=null){
			if (reqType > 10000) {
				baseHttpClient.post(reqType, this, url, params, this);
			} else {
				baseHttpClient.get(reqType, url, params, this);
			}
		}
	}

	
	/**
	 * 得到 http aynsc客户端 实例对象
	 * @return
	 */
	public AsyncHttpClientImpl getAsyncClient(){
		return app.mAsyncClient;
	}
	
}
