package com.cashdivider.deviceadmin.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Cotfas Vlad on 6/07/17.
 */
public class LogService {

    private static final String TAG = "LogService";

    private static final boolean LOG_ENABLED = true;
    private static final boolean ERROR_ENABLED = true;

    public static void log(Object clazz, String message) {
        if (LOG_ENABLED) {
            if (TextUtils.isEmpty(message)) {
                message = "null";
            }
            Log.i(getTag(clazz.toString()), "---->" + message);
        }
    }

    public static void log(Class<?> clazz, String message) {
        if (LOG_ENABLED) {
            if (TextUtils.isEmpty(message)) {
                message = "null";
            }
            Log.i(getTag(clazz), "---->" + message);
        }
    }

    public static void log(String tag, String message) {
        if (LOG_ENABLED) {
            if (TextUtils.isEmpty(message)) {
                message = "null";
            }
            Log.i(getTag(tag), "---->" + message);
        }
    }

    public static void log(String message) {
        if (LOG_ENABLED) {
            if (TextUtils.isEmpty(message)) {
                message = "null";
            }
            Log.i(TAG, "---->" + message);
        }
    }

    public static void error(Object clazz, Exception exception) {
        if (ERROR_ENABLED) {
            if (exception == null) {
                return;
            }
            Log.e(getTag(clazz.toString()), "---->" + exception.getMessage(), exception.getCause());
        }
    }

    public static void error(Class<?> clazz, Exception exception) {
        if (ERROR_ENABLED) {
            if (exception == null) {
                return;
            }
            Log.e(getTag(clazz), "---->" + exception.getMessage(), exception.getCause());
        }
    }

    public static void error(String tag, Exception exception) {
        if (ERROR_ENABLED) {
            if (exception == null) {
                return;
            }
            Log.e(getTag(tag), "---->" + exception.getMessage(), exception.getCause());
        }
    }

    public static void error(Exception exception) {
        if (ERROR_ENABLED) {
            if (exception == null) {
                return;
            }
            Log.e(TAG, "---->" + exception.getMessage(), exception.getCause());
        }
    }

    private static String getTag(final String tag) {
        if (TextUtils.isEmpty(tag)) {
            return TAG;
        }
        return tag;
    }

    private static String getTag(final Class<?> clazz) {
        String tag = "";
        if (clazz != null) {
            tag = clazz.getSimpleName();
        }
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        return tag;
    }

    public static boolean isLogEnabled() {
        return LOG_ENABLED;
    }

    public static boolean isErrorEnabled() {
        return ERROR_ENABLED;
    }
}