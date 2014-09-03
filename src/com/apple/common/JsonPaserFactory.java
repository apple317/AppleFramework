package com.apple.common;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.apple.activity.BaseApplication;

import android.util.Log;


public class JsonPaserFactory {

	public static BaseEntity paserObj(String s, int type) {
		if (s == null || s.trim().toString().equals(""))
			return null;
		BaseEntity entity = (BaseEntity) BaseApplication.httpRes.getPaser(type);
		try {
			entity.paser(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

	public static BaseEntity paserObj(HttpEntity en, int type) {
		BaseEntity entity = null;
		if (en != null) {
			try {
				entity = paserObj(EntityUtils.toString(en), type);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return entity;
	}
}
