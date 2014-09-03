/**
 * 公共父类弹出框
 */
package com.apple.activity;

import com.apple.common.BaseHttpClient;
import com.apple.common.HttpCallback;
import com.apple.http.async.RequestParams;
import com.apple.utils.DeviceInfo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


/**
 * @author hushaoping
 * 
 */
public class BaseDialog extends Dialog implements
HttpCallback{
	
	DialogClick click;
	Context context;
	public BaseDialog(Context mContext, int style) {
		super(mContext, style);
		context=mContext;
		click = new DialogClick();
	}

	/** 设置控件点击监听器 */
	public void setOnClickListener(int id) {
		if (click == null)
			click = new DialogClick();
		this.getWindow().findViewById(id).setOnClickListener(click);
	}

	private class DialogClick implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			treatClickEvent(id);
			treatClickEvent(v);
		}
	}

	/** 处理点击事件 */
	public void treatClickEvent(int id) {

	}

	/** 处理点击事件 */
	public void treatClickEvent(View v) {

	}

	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	/**
	 * 显示对话框
	 * */
	public void goShow() {
		this.setCanceledOnTouchOutside(true);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		show();
		
	}

	@Override
	public void onSuccess(String content, Object object, int reqType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailure(Throwable error, String content, int reqType) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void sendRequest(int reqType, String url, RequestParams params,
			BaseHttpClient baseHttpClient, boolean showDialog) {
		// TODO Auto-generated method stub
		if (DeviceInfo.isNetWorkEnable(context)) {
			if(baseHttpClient!=null){
				if (reqType > 10000) {
					baseHttpClient.post(reqType, context, url, params, this);
				} else {
					baseHttpClient.get(reqType,context, url, params, this);
				}
			}
		}
	}
	
}
