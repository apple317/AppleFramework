package com.apple.common;

import java.lang.reflect.Type;



public abstract class BaseHttpRes {
	
	/**
	 * 根据reqType获取URL
	 * @param reqType
	 * @return
	 */
	public abstract String getUrl(int reqType);
	
	
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
	/**
	 * 根据reqType获取返回解析对象
	 * @param type
	 * @return
	 */
	public abstract  Object getPaser(int type);
	
}
