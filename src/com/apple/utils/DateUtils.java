package com.apple.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data utility class
 * @author ran.zhao
 * @version 1.0.0
 * @since 2013-03-19
 */
public class DateUtils {
	/** 20130225 */
	public static final int FORMAT_TO_DAY = 0;
	/** 2013022511 */
	public static final int FORMAT_TO_HOUR = 1;
	/** 201302251134 */
	public static final int FORMAT_TO_MINUTE = 2;
	/** 20130225113420 */
	public static final int FORMAT_TO_SECOND = 3;

    /** 0字符串表示 */
    public static final String ZERO_SINGLE_STRING = "0";

    /** 00 字符串表示 */
    public static final String  ZERO_DOUBLE_STRING = "00";

	private static final String[] SIMPLE_TIME_FORMAT = {
            "yyyy-MM-dd", "yyyy-MM-dd-HH", "yyyy-MM-dd-HH-mm", "yyyy-MM-dd-HH-mm-ss"
    };

	/**
	 * 转换当前日期时间为字符串 2013022511
	 * @param format 转换后的格式类型
	 * @return 转换后的字符
	 */
	public static String formatcurrentDate(int format) {
		return formatDate(new Date(), format, "");
	}

	/**
	 * 转换当前日期时间为字符串 2013{delimiter}02{delimiter}25{delimiter}11
	 * @param format 转换后的格式类型
     * @param delimiter 分隔符
	 * @return 转换后的字符
	 */
	public static String currentTimeToString(int format, String delimiter) {
		return formatDate(new Date(), format, delimiter);
	}

	/**
	 * 转换指定日期时间为字符串 2013022511
	 * @param timeMilliSeconds 微秒单位的日期时间
	 * @param format 转换后的格式类型。
     *             <p>{@link #FORMAT_TO_DAY}</p>
     *             <p>{@link #FORMAT_TO_HOUR}</p>
     *             <p>{@link #FORMAT_TO_MINUTE}</p>
     *             <p>{@link #FORMAT_TO_SECOND}</p>
	 * @return 转换后的字符串
	 */
	public static String formatDate(long timeMilliSeconds, int format) {
		return formatDate(new Date(timeMilliSeconds), format, "");
    }

	/**
     * 转换指定日期时间为字符串 2013{delimiter}02{delimiter}25{delimiter}11
	 * @param timeMilliSeconds 微秒单位的日期时间
	 * @param format 转换后的格式类型。
     *             <p>{@link #FORMAT_TO_DAY}</p>
     *             <p>{@link #FORMAT_TO_HOUR}</p>
     *             <p>{@link #FORMAT_TO_MINUTE}</p>
     *             <p>{@link #FORMAT_TO_SECOND}</p>
     * @param delimiter 分隔符
	 * @return 转换后的字符串
	 */
	public static String formatDate(long timeMilliSeconds, int format, String delimiter) {
		return formatDate(new Date(timeMilliSeconds), format, delimiter);
	}

	private static String formatDate(Date date, int format, String delimiter) {
		return formatDate(format, delimiter).format(date);
	}

    private static SimpleDateFormat formatDate(int format, String delimiter) {
        assert format >= 0 && format < SIMPLE_TIME_FORMAT.length;
        String formatString = SIMPLE_TIME_FORMAT[format];
        if (delimiter != null) {
            formatString = formatString.replace("-", delimiter);
        }
        return new SimpleDateFormat(formatString);
    }

    /**
     * 获取指定日期的下一个日期
     * @param date 指定日期
     * @return 下一个日期
     */
    public static Date nextDate(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate() + 1);
    }

    /**
     * 获取指定时间的下一个日期
     * @param timeMilliSeconds 指定时间
     * @return 下一个日期
     */
    public static Date nextDate(long timeMilliSeconds) {
        return nextDate(new Date(timeMilliSeconds));
    }

    /**
     *
     * format time 01:02
     * @param time  时间，毫秒
     * @return String
     */
    public static String formatTime(long time) {
        String strMinute;
        String strSecond;
        int minute = 0;
        int second = (int)(time / ConstantUtils.THOUSAND);
        if (second > ConstantUtils.SECONDS_PER_MINUTE) {
            minute = second / ConstantUtils.SECONDS_PER_MINUTE;
            second = second % ConstantUtils.SECONDS_PER_MINUTE;
        }

        if (minute / ConstantUtils.TEN == 0) {
            strMinute = ZERO_SINGLE_STRING + minute;
        } else {
            strMinute = "" + minute;
        }
        if (second / ConstantUtils.TEN == 0) {
            strSecond = ZERO_SINGLE_STRING + second;
        } else {
            if (second  == ConstantUtils.SECONDS_PER_MINUTE) {
                strMinute = "" +  minute + 1;
                strSecond = ZERO_DOUBLE_STRING;
            } else {
                strSecond = "" + second;
            }
        }
        return strMinute + ":" + strSecond;
    }

    /**
     * generate time 01:02:03 02:03
     * @param time 时间，毫秒
     * @param delimiter 分割符
     * @return formatted time string
     */
    public static String formatTime(long time, String delimiter) {
        String curDelimiter = TextUtils.isEmpty(delimiter) ? ":" : delimiter;
        int totalSeconds = (int)(time / ConstantUtils.THOUSAND);
        int seconds = totalSeconds % ConstantUtils.SECONDS_PER_MINUTE;
        int minutes = totalSeconds / ConstantUtils.SECONDS_PER_MINUTE % ConstantUtils.SECONDS_PER_MINUTE;
        int hours = totalSeconds / ConstantUtils.SECONDS_PER_MINUTE / ConstantUtils.THOUSAND;

        return hours > 0 ? String.format("%02d" + curDelimiter + "%02d" + curDelimiter + "%02d", hours, minutes , seconds)
                : String.format("%02d" + curDelimiter + "%02d", minutes, seconds);
    }
}
