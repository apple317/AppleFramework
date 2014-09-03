package com.apple.utils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 申请 参数正序数 然后md5加密
 * @author apple_hsp
 *
 */
public class RequestUtils {

	public static String getRequestSign(Map<String, String> map) {
		if(map==null) return "";
		List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(
				map.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1,
					Map.Entry<String, String> o2) {
				// return (o2.getValue() - o1.getValue());
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < infoIds.size(); i++) {
			Map.Entry<String, String> map1 = (Map.Entry<String, String>) infoIds
					.get(i);
			builder.append(map1.getKey() + map1.getValue());
		}
		return MD5Util.md5(builder.toString());
	}

	
}
