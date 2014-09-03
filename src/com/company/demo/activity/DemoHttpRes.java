package com.company.demo.activity;

import java.lang.reflect.Type;

import com.apple.common.BaseEntity;
import com.apple.common.BaseHttpRes;


public class DemoHttpRes extends BaseHttpRes {
	private static DemoHttpRes instance;
	// public static String BASE_URL = "http://newapp.mobile.hupu.com/";
	public static String BASE_URL = "http://023151.ichengyun.net/";
	public static int REQ_METHOD_POST = 10000;
	// get请求的reqType<1000;
	
	public final static int REQ_METHOD_GET_DEMO_HOME = 21;
	public static String REQ_URL_GET_DEMO_HOME = BASE_URL
			+ "app/findAllHonor4app.action";



	public static synchronized DemoHttpRes getInstance() {
		if (instance == null)
			instance = new DemoHttpRes();
		return instance;
	}

	private DemoHttpRes() {
	}

	public String getUrl(int reqType) {
		switch (reqType) {
		case REQ_METHOD_GET_DEMO_HOME:
			return REQ_URL_GET_DEMO_HOME;
		}
		return null;
	}

	/**
	 * 请求地址需要格式化的
	 * */
	public String getUrl(int mId, String format) {
		switch (mId) {
		// case REQ_METHOD_GET_MORE_REPLY:
		// return String.format(REQ_URL_GET_REPLY, format);
		default:
			return format + getUrl(mId);
		}
	}

	
	public BaseEntity getPaser(int type) {
		BaseEntity paser = null;
		switch (type) {
		case REQ_METHOD_GET_DEMO_HOME:
			paser =  new DemoEntity();
			break;
		}
		return paser;
	}

}
