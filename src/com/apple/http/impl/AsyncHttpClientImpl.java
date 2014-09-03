package com.apple.http.impl;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.content.Context;

import com.apple.common.BaseHttpClient;
import com.apple.common.BaseHttpHandler;
import com.apple.common.HttpCallback;
import com.apple.http.async.AsyncHttpClient;
import com.apple.http.async.AsyncHttpResponseHandler;
import com.apple.http.async.RequestParams;

/**
 * AsyncHttpClient async网络申请实现类
 * 如果有新网络tcp请求，就要重新实现一个网络交互类
 * 
 * @author 胡少平
 * 
 */
public class AsyncHttpClientImpl implements BaseHttpClient {
	
	private  AsyncHttpClient client=null;
	
	//单例模式实现
	private static AsyncHttpClientImpl instance;

	public static AsyncHttpClientImpl getHupuHttpClient() {
		if (instance == null)
			instance = new AsyncHttpClientImpl();
		return instance;
	}
	
	private AsyncHttpClientImpl() {
		client = new AsyncHttpClient();
	}

	@Override
	public void get(int reqType, String url, RequestParams cacheParams,
			HttpCallback callback) {
		client.get(url,cacheParams, new BaseHttpHandler(callback), reqType);
	}

	@Override
	public void get(int reqType, Context context, String url,
			RequestParams cacheParams, HttpCallback callback) {
		// TODO Auto-generated method stub
		client.get(url, cacheParams,new BaseHttpHandler(callback), reqType);
	}

	@Override
	public void post(int reqType, Context context, String url,
			RequestParams params, HttpCallback callback) {
		// TODO Auto-generated method stub
		client.post(url,params, new BaseHttpHandler(callback), reqType);
	}

	@Override
	public void post(int reqType, Context context, String url,
			HttpEntity entity, String contentType, HttpCallback callback) {
		// TODO Auto-generated method stub
		client.post(context,url, entity,contentType,new BaseHttpHandler(callback), reqType);
	}

	@Override
	public void post(int reqType, Context context, String url,
			Map<String, String> clientHeaderMap, RequestParams params,
			String contentType, HttpCallback callback) {
		// TODO Auto-generated method stub
	}

	@Override
	public void post(int reqType, Context context, String url,
			Header[] headers, HttpEntity entity, String contentType,
			HttpCallback callback) {
		// TODO Auto-generated method stub
		client.post(context,url, headers,entity,contentType,new BaseHttpHandler(callback), reqType);
	}
	
}
