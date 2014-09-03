package com.apple.activity;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import com.apple.common.BaseHttpRes;
import com.apple.http.impl.AsyncHttpClientImpl;
import com.apple.utils.DeviceInfo;
import com.apple.utils.MySharedPreferencesMgr;



public abstract class BaseApplication extends Application implements
		UncaughtExceptionHandler {

	
	
	public AsyncHttpClientImpl mAsyncClient;
	
	private ArrayList<Activity> actList;
	public static BaseHttpRes httpRes;
	static String pakage;
	public static String mDeviceId;
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	
	
	public BaseApplication() {
		DeviceInfo.init(this);
		httpRes = initBaseHttpRes();
		actList = new ArrayList<Activity>();
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	
	
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		mDeviceId=getDeviceID(this.getApplicationContext());
		mAsyncClient=getAsyncClient();
		super.onCreate();
	}


	public boolean isAppOnForeground() {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();

		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}
	


	public void onBackground() {
		
	}

	public void onForeground() {
		
	}
	
	/**
	 * 得到系统版本号
	 * @return
	 */
	public int getVerCode() {
		int verCode = -1;
		try {
			verCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verCode;
	}
	/**
	 * 初始化请求常量
	 * 
	 * @return
	 */
	protected abstract BaseHttpRes initBaseHttpRes();
	/**
	 * 得到系统版本名称
	 * @return
	 */
	public String getVerName() {
		String verName = "";
		try {
			verName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return verName;
	}

	/**
	 * 控制应用退出操作
	 */
	@SuppressLint("NewApi")
	public void quit() {
		int version = android.os.Build.VERSION.SDK_INT;
//		for (Activity act : actList) {
//			if (!act.isFinishing())
//				act.finish();
//		}
//		actList.clear();
		if (version <= 7) {
			System.out.println("   version  < 7");
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.restartPackage(getPackageName());
		} else {
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			manager.killBackgroundProcesses(getPackageName());
		}
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

	public void addActivitToStack(Activity act) {
		actList.add(act);
	}

	public void removeFromStack(Activity act) {
		actList.remove(act);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		if (!treatErr(ex) && mDefaultHandler != null)
			mDefaultHandler.uncaughtException(thread, ex);
		else
			quit();
	}

	public boolean treatErr(Throwable ex) {
		return false;
	}

	/**
	 * 获取设备id
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		if (mDeviceId == null) {
			mDeviceId = DeviceInfo.getDeviceInfo(context);
			if (mDeviceId == null || mDeviceId.length() < 2) {
				mDeviceId = MySharedPreferencesMgr.getString("clientid", null);
				if (mDeviceId == null) {
					mDeviceId = DeviceInfo.getUUID();
					MySharedPreferencesMgr.setString("clientid", mDeviceId);
				}
			}
		}
		return mDeviceId;
	}
	
	/**
	 * 得到 http aynsc客户端 实例对象
	 * @return
	 */
	public AsyncHttpClientImpl getAsyncClient(){
		return AsyncHttpClientImpl.getHupuHttpClient();
	}
	
}
