package com.apple.common;

import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.content.Context;

import com.apple.http.async.RequestParams;

/**
 * 请求对象模板接口
 * @author hushaoping
 *
 */
public interface BaseHttpClient {
	
	public void post(int reqType,Context context, String url, RequestParams params,
			HttpCallback callback);
	
	public void post(int reqType,Context context, String url, HttpEntity entity,
			String contentType, HttpCallback callback);
	
	public void post(int reqType,Context context, String url,
			Map<String, String> clientHeaderMap, RequestParams params,
			String contentType, HttpCallback callback);
	
	public void post(int reqType,Context context, String url, Header[] headers,
			HttpEntity entity, String contentType,
			HttpCallback callback);
	
	public void get(int reqType,String url, RequestParams cacheParams,
			HttpCallback callback);
	
	public void get(int reqType,Context context, String url, RequestParams cacheParams,
			HttpCallback callback);
	
	
}
