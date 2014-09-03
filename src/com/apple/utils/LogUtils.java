/**
 * @(#)LogUtils.java     2011-8-15
 * Copyright (c) 2007-2011 Shanghai ShuiDuShi Co.Ltd. All right reserved.
 */
package com.apple.utils;
import android.util.Log;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class description here
 * @author ke.di
 * @version 2.0.0
 * @since Aug 12, 2011
 */
public final class LogUtils {
	private static final String PREFIX = "TTPOD:";
    private static boolean sLogEnable = true;
    private static boolean sWriteToFile = false;

    private static final SimpleDateFormat LOG_DATE_TIME_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");

    private static String sLogFileName;

    /**
     * @param folder folder path
     */
    public static void setLogFileFolder(String folder) {
        sLogFileName = folder + "/TTPOD_" + DateUtils.formatcurrentDate(DateUtils.FORMAT_TO_SECOND) + ".log";
    }

    /**
     * 设置Log开关
     *
     * @param enable 开关项(默认为开).
     */
    public static void setEnable(boolean enable) {
        sLogEnable = enable;
    }

    /**
     * 设置写入log到文件
     * @param writeToFile 开关项(默认开)
     */
    public static void setWriteToFile(boolean writeToFile) {
        sWriteToFile = writeToFile;
    }

    /**
	 * log for debug
     *
	 * @see android.util.Log#d(String, String)
	 * @param message log message
	 * @param tag tag
	 */
	public static void d(String tag, String message) {
        if (sLogEnable) {
            String msg = PREFIX + message;
            Log.d(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for debug
	 * @see android.util.Log#d(String, String, Throwable)
	 * @param message log message
	 * @param throwable throwable
	 * @param tag tag
	 */
	public static void d(String tag, String message, Throwable throwable) {
		if (sLogEnable) {
            String msg = PREFIX + message;
            Log.d(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
		}
	}

	/**
	 * log for debug
	 * @see android.util.Log#d(String, String)
     * @param tag tag
	 * @param format message format, such as "%d ..."
     * @param params message content params
	 */
	public static void d(String tag, String format, Object... params) {
		if (sLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.d(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for warning
	 * @see android.util.Log#w(String, String)
	 * @param message log message
	 * @param tag tag
	 */
	public static void w(String tag, String message) {
		if (sLogEnable) {
            String msg = PREFIX + message;
            Log.w(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for warning
	 * @see android.util.Log#w(String, Throwable)
	 * @param tag tag
	 * @param throwable throwable
	 */
	public static void w(String tag, Throwable throwable) {
		if (sLogEnable) {
			Log.w(tag, throwable);
            writeToFile(tag, Log.getStackTraceString(throwable));
		}
	}

	/**
	 * log for warning
	 * @see android.util.Log#w(String, String, Throwable)
	 * @param message log message
	 * @param throwable throwable
	 * @param tag tag
	 */
	public static void w(String tag, String message, Throwable throwable) {
		if (sLogEnable) {
            String msg = PREFIX + message;
            Log.w(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
		}
	}

    /**
	 * log for warning
	 * @see android.util.Log#w(String, String)
     * @param tag tag
	 * @param format message format, such as "%d ..."
     * @param params message content params
	 */
	public static void w(String tag, String format, Object... params) {
		if (sLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.w(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for error
	 * @see android.util.Log#i(String, String)
	 * @param message message
	 * @param tag tag
	 */
	public static void e(String tag, String message) {
        String msg = PREFIX + message;
        Log.e(tag, msg);
        writeToFile(tag, msg);
	}

	/**
	 * log for error
	 * @see android.util.Log#i(String, String, Throwable)
	 * @param message log message
	 * @param throwable throwable
	 * @param tag tag
	 */
	public static void e(String tag, String message, Throwable throwable) {
        String msg = PREFIX + message;
        Log.e(tag, msg, throwable);
        writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
	}

    /**
	 * log for error
	 * @see android.util.Log#e(String, String)
     * @param tag tag
	 * @param format message format, such as "%d ..."
     * @param params message content params
	 */
	public static void e(String tag, String format, Object... params) {
        String msg = String.format(PREFIX + format, params);
        Log.e(tag, msg);
        writeToFile(tag, msg);
	}

	/**
	 * log for information
	 * @see android.util.Log#i(String, String)
	 * @param message message
	 * @param tag tag
	 */
	public static void i(String tag, String message) {
		if (sLogEnable) {
            String msg = PREFIX + message;
            Log.i(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for information
	 * @see android.util.Log#i(String, String, Throwable)
	 * @param message log message
	 * @param throwable throwable
	 * @param tag tag
	 */
	public static void i(String tag, String message, Throwable throwable) {
		if (sLogEnable) {
            String msg = message;
            Log.i(tag, PREFIX + msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
		}
	}

    /**
	 * log for information
	 * @see android.util.Log#i(String, String)
     * @param tag tag
	 * @param format message format, such as "%d ..."
     * @param params message content params
	 */
	public static void i(String tag, String format, Object... params) {
		if (sLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.i(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for verbos
	 * @see android.util.Log#v(String, String)
	 * @param message log message
	 * @param tag tag
	 */
	public static void v(String tag, String message) {
		if (sLogEnable) {
            String msg = PREFIX + message;
            Log.v(tag, msg);
            writeToFile(tag, msg);
		}
	}

	/**
	 * log for verbose
	 * @see android.util.Log#v(String, String, Throwable)
	 * @param message log message
	 * @param throwable throwable
	 * @param tag tag
	 */
	public static void v(String tag, String message, Throwable throwable) {
		if (sLogEnable) {
            String msg = PREFIX + message;
            Log.v(tag, msg, throwable);
            writeToFile(tag, msg + "\n" + Log.getStackTraceString(throwable));
		}
	}

    /**
	 * log for verbose
	 * @see android.util.Log#v(String, String)
     * @param tag tag
	 * @param format message format, such as "%d ..."
     * @param params message content params
	 */
	public static void v(String tag, String format, Object... params) {
		if (sLogEnable) {
            String msg = String.format(PREFIX + format, params);
            Log.v(tag, msg);
            writeToFile(tag, msg);
		}
	}

    private static void writeToFile(String tag, String msg) {
        if (sWriteToFile) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(sLogFileName, true);
                fileWriter.write(String.format("%s pid=%d %s: %s\n", LOG_DATE_TIME_FORMAT.format(new Date()), android.os.Process.myPid(), tag, msg));
                fileWriter.flush();
                fileWriter.close();
            } catch (Throwable t) {
                t.printStackTrace();
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
