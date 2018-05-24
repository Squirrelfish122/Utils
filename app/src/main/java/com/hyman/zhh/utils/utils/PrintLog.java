package com.hyman.zhh.utils.utils;

import android.util.Log;

/**
 * log
 * Created by zhanghao on 2016/11/29.
 */

public class PrintLog {

    private static boolean printLog = true;

    public static void setPringLog(boolean print){
        printLog = print;
    }

    public static void d(String tag, String msg){
        if (printLog){
            Log.d(tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if (printLog){
            Log.e(tag,msg);
        }
    }

    public static void i(String tag, String msg){
        if (printLog){
            Log.i(tag,msg);
        }
    }

    public static void v(String tag, String msg){
        if (printLog){
            Log.v(tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if (printLog){
            Log.w(tag,msg);
        }
    }
}
