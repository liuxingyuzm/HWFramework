package com.huawei.emui.hiexperience.hwperf.utils;

import android.util.Log;

public final class HwPerfLog {
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static final String TAG = "HwPerfLog";
    private static volatile boolean logEnabled = true;

    private HwPerfLog() {
    }

    public static void d(String tag, String msg) {
        log(3, tag, msg, null);
    }

    public static void d(String msg) {
        log(3, TAG, msg, null);
    }

    public static void i(String tag, String msg) {
        log(4, tag, msg, null);
    }

    public static void i(String msg) {
        log(4, TAG, msg, null);
    }

    public static void w(String tag, String msg) {
        log(5, tag, msg, null);
    }

    public static void w(String msg) {
        log(5, TAG, msg, null);
    }

    public static void e(Throwable ex) {
        log(6, null, null, ex);
    }

    public static void e(String tag, String msg) {
        log(6, tag, msg, null);
    }

    public static void e(String msg) {
        log(6, TAG, msg, null);
    }

    public static void e(String tag, String msg, Throwable ex) {
        log(6, tag, msg, ex);
    }

    private static void log(int priority, String tag, String msg, Throwable ex) {
        String logMessage;
        if (logEnabled) {
            if (ex == null) {
                logMessage = msg;
            } else {
                logMessage = String.format(LOG_FORMAT, new Object[]{msg == null ? ex.getMessage() : msg, Log.getStackTraceString(ex)});
            }
            Log.println(priority, tag, logMessage);
        }
    }
}
