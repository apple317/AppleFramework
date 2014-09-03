package com.apple.utils;

import android.util.Log;

public class SystemLog {

	public static boolean isDebug;

	public static void p(String msg) {
		if (isDebug)
			System.out.println(msg);
	}

	public static void d(String tag, int v) {
		if (isDebug)
			Log.d(tag, "" + v);
	}

	public static void d(String tag, String v) {
		if (isDebug)
			Log.d(tag, v);
	}

	public static void e(String tag, int v) {
		if (isDebug)
			Log.e(tag, "" + v);
	}

	public static void e(String tag, String v) {
		if (isDebug)
			Log.e(tag, v);
	}
	
	public static void d(String v)
	{
		if (isDebug)
			Log.d("HUPUAPP", v);
	}
}
