package com.apple.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TimeUtile {

	public static String gettime(String data) {
		String sR = "";

		Date date = new Date();

		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String times = from.format(date);
		try {
			long re_mm = getCompareDate(
					from.format(new Date((Long.valueOf(data) * 1000))), times);
			if (re_mm <= 0) {
				sR = "1分钟前";
			}
			if (re_mm > 0 && re_mm < 60) {
				sR = String.valueOf(re_mm) + "分钟前";
			}

			if (re_mm < 60 * 24 && re_mm >= 60) {
				sR = String.valueOf(re_mm / 60) + "小时前";
			}

			if (re_mm >= 60 * 24 && re_mm < 60 * 24 * 7) {
				sR = String.valueOf(re_mm / (60 * 24)) + "天前";
			}

			if (re_mm >= 60 * 24 * 7) {
				sR = getPosttime(Long.valueOf(data));
			}
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return sR;
	}

	public static String getPosttime(long data) {
//		StringBuilder builder = new StringBuilder();
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(data * 1000L);
//		Log.i("HU","getPosttime==="+data);
//		int month = mCalendar.get(Calendar.MONTH)+1;
//		int day = mCalendar.get(Calendar.DAY_OF_MONTH);
//		int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
//		int minute = mCalendar.get(Calendar.MINUTE);
//		builder.append(month < 10 ? "0" + month : month);
//		builder.append("-");
//		builder.append(day < 10 ? "0" + day : day);
//		builder.append(" ");
//		builder.append(hour < 10 ? "0" + hour : hour);
//		builder.append(":");
//		builder.append(minute < 10 ? "0" + minute : minute);
//		return builder.toString();
		return new java.text.SimpleDateFormat("MM-dd HH:mm").format(mCalendar.getTime());
	}

	public static String getReplytime(String data) {

		String sR = "";

		Date date = new Date();

		SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String times = from.format(date);

		try {
			long re_mm = getCompareDate(data, times);
			if (re_mm <= 0) {
				sR = "1分钟前";
			}
			if (re_mm > 0 && re_mm < 60) {
				sR = String.valueOf(re_mm) + "分钟前";
			}

			if (re_mm < 60 * 24 && re_mm >= 60) {
				sR = String.valueOf(re_mm / 60) + "小时前";
			}

			if (re_mm >= 60 * 24 && re_mm < 60 * 24 * 7) {
				sR = String.valueOf(re_mm / (60 * 24)) + "天前";
			}

			if (re_mm >= 60 * 24 * 7) {
				sR = data;
			}

		} catch (ParseException e) {

			e.printStackTrace();
		}

		return sR;
	}

	public static long getCompareDate(String startDate, String endDate)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = formatter.parse(startDate);
		Date date2 = formatter.parse(endDate);
		long l = date2.getTime() - date1.getTime();
		long d = l / (60 * 1000);
		return d;
	}

	public static String onGetTimeAgo(long time) {
		long re_mm = (System.currentTimeMillis() - time) / (60 * 1000);
		String sR = "刚刚";
		if (re_mm <= 0) {
			sR = "刚刚";
		}
		if (re_mm > 0 && re_mm < 60) {
			sR = String.valueOf(re_mm) + "分钟前";
		}

		if (re_mm < 60 * 24 && re_mm >= 60) {
			sR = String.valueOf(re_mm / 60) + "小时前";
		}

		if (re_mm >= 60 * 24 && re_mm < 60 * 24 * 7) {
			sR = String.valueOf(re_mm / (60 * 24)) + "天前";
		}

		if (re_mm >= 60 * 24 * 7 && re_mm < 60 * 24 * 30) {
			sR = String.valueOf(re_mm / (60 * 24 * 7)) + "周前";
		}

		if (re_mm >= 60 * 24 * 30 && re_mm < 60 * 24 * 365) {
			sR = String.valueOf(re_mm / (60 * 24 * 30)) + "个月前";
		}

		if (re_mm >= 60 * 24 * 365) {
			sR = String.valueOf(re_mm / (60 * 24 * 365)) + "年前";
		}
		return sR;
	}
}
