package com.company.demo.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.apple.activity.BaseActivity;
import com.apple.http.async.RequestParams;
import com.umeng.analytics.MobclickAgent;

public class DemoBaseActivity extends BaseActivity {
	
	public DemoApp mApp;

	private static String mToken;

	public DemoBaseActivity mBaseAct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApp = (DemoApp) getApplication();
		if (mParams == null)
			mParams = new RequestParams();
		mBaseAct = this;
	}

	public int i_curReqTimes;

	@Override
	protected void onStop() {
		if (!mApp.isAppOnForeground()) {
			bBackGround = true;
			onBackground();
		}
		MobclickAgent.onPause(this);
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mApp.isAppOnForeground()) {
			bBackGround = false;
			onForeground();
		}
		MobclickAgent.onResume(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
		}
		return false;
	}

	public void quit() {
		mApp.quit();
	}

	public RequestParams initParameter() {
		if (mParams != null)
			mParams.clear();
		return mParams;
	}

	public void onBackground() {
		mApp.onBackground();
	}

	public void onForeground() {
		mApp.onForeground();
	}

	@Override
	protected void initView(Bundle bundle) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initLisener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initData(Bundle bundle) {
		// TODO Auto-generated method stub

	}

}
