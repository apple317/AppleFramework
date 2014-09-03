package com.apple.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.apple.activity.BaseApplication;
import com.apple.http.async.AsyncHttpResponseHandler;

public class BaseHttpHandler extends AsyncHttpResponseHandler {

	private HttpCallback response;
	public int messageID1;
	public int messageID2;

	public BaseHttpHandler(HttpCallback response) {
		this.response = response;
	}

	@Override
	public void onFinish() {
		super.onFinish();
	}

	@Override
	public void onSuccess(HttpEntity content, int reqType) {
		super.onSuccess(content, reqType);
		if (response!= null) {
			BaseEntity entity=JsonPaserFactory.paserObj(content, reqType);
			try {
				response.onSuccess(EntityUtils.toString(content), entity, reqType);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onFailure(Throwable error, String content, int reqType) {
		super.onFailure(error, content, reqType);
		if (response != null) {
			response.onFailure(error, content, reqType);
		}
	}

	
}
