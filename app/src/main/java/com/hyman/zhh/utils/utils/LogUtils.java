package com.hyman.zhh.utils.utils;

import android.util.Log;

public class LogUtils {

    private static boolean isDebugable = true;

    public static void setDebug(boolean isDebug) {
        isDebugable = isDebug;
    }

    public static void v(String tag, String msg) {
        if (isDebugable) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebugable) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebugable) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebugable) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebugable) {
            Log.e(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (isDebugable) {
            Log.wtf(tag, msg);
        }
    }

    public static void printStackTrace(String tag, Throwable throwable) {
        if (isDebugable) {
            Log.i(tag, "", throwable);
        }
    }

}
