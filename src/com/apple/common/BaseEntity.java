package com.apple.common;

import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 * 解析实体类 基类
 * @author apple_hsp
 *
 */
public abstract class BaseEntity {

	public final static String KEY_RESULT="result";
	public final static String KEY_LIST="list";
	public final static String KEY_DATE="date";
	public final static String KEY_NAME="name";
	public final static String KEY_DESC="desc";
	public final static String KEY_DATA="data";
	
	public  abstract void paser(String data) throws Exception ;

	public String err;

	public static  void paserKeys(JSONArray arr,LinkedHashMap<String, String> map)	throws Exception
	{	
		JSONArray keys=arr.getJSONArray(0);
		JSONArray values=arr.getJSONArray(1);
		int size =keys.length();
		for(int i=0;i<size;i++)
		{
			map.put(keys.getString(i), values.getString(i));
		}
	}
}
