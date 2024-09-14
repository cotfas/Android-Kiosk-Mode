package com.cashdivider.deviceadmin.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.cashdivider.deviceadmin.R;
import com.cashdivider.deviceadmin.application.AppApplication;


/**
 * Created by Cotfas Vlad on 6/19/17.
 */

@SuppressWarnings("unused")
@SuppressLint("ApplySharedPref")
public class SharedPrefUtils {

    private static final String APP_SHARED_PREF = AppApplication.getContext().getString(R.string.app_name);


    /**
     * Getting string from SharedPref
     * @param sharedKey
     * @return
     */
    public static String getStringValue(String sharedKey) {
        LogService.log("sharedKey = " + sharedKey);

        Context context = AppApplication.getContext();
        if (context == null) {
            return null;
        }
        try {
            SharedPreferences settings = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            String result = settings.getString(sharedKey, null);
            return result;
        } catch (Exception e) {
            LogService.error(e);
        }
        return null;
    }

    /**
     * Getting string from SharedPref
     * @param sharedKey
     * @param defaultValues
     * @return
     */
    public static String getStringValue(String sharedKey, String defaultValues) {
        LogService.log("sharedKey = " + sharedKey);

        Context context = AppApplication.getContext();
        if (context == null) {
            return defaultValues;
        }
        try {
            SharedPreferences settings = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            String result = settings.getString(sharedKey, defaultValues);
            return result;
        } catch (Exception e) {
            LogService.error(e);
        }
        return defaultValues;
    }

    /**
     * Saving string to SharedPref
     * @param sharedKey
     * @param sharedValue
     */
    public static void saveString(String sharedKey, String sharedValue) {
        LogService.log("sharedKey = " + sharedKey);

        try {
            Context context = AppApplication.getContext();
            if (context == null) {
                return;
            }

            SharedPreferences prefs = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(sharedKey, sharedValue);
            editor.commit();
        } catch (Exception e) {
            LogService.error(e);
        }
    }

    /**
     * Getting boolean from SharedPref
     * @param sharedKey
     * @return
     */
    public static boolean getBoolean(String sharedKey) {
        LogService.log("sharedKey = " + sharedKey);

        Context context = AppApplication.getContext();
        if (context == null) {
            return false;
        }
        try {
            SharedPreferences settings = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            boolean result = settings.getBoolean(sharedKey, false);
            return result;
        } catch (Exception e) {
            LogService.error(e);
        }
        return false;
    }

    /**
     * Getting boolean from SharedPref
     * @param sharedKey
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(String sharedKey, boolean defaultValue) {
        LogService.log("sharedKey = " + sharedKey);

        Context context = AppApplication.getContext();
        if (context == null) {
            return defaultValue;
        }
        try {
            SharedPreferences settings = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            boolean result = settings.getBoolean(sharedKey, defaultValue);
            return result;
        } catch (Exception e) {
            LogService.error(e);
        }
        return defaultValue;
    }

    /**
     * Saving boolean to SharedPref
     * @param sharedKey
     * @param sharedValue
     */
    public static void saveBoolean(String sharedKey, boolean sharedValue) {
        LogService.log("sharedKey = " + sharedKey);

        try {
            Context context = AppApplication.getContext();
            if (context == null) {
                return;
            }

            SharedPreferences prefs = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(sharedKey, sharedValue);
            editor.commit();
        } catch (Exception e) {
            LogService.error(e);
        }
    }

    /**
     * Getting float from SharedPref
     * @param sharedKey
     * @param defaultValue
     * @return
     */
    public static float getFloat(String sharedKey, float defaultValue) {
        LogService.log("sharedKey = " + sharedKey);

        Context context = AppApplication.getContext();
        if (context == null) {
            return defaultValue;
        }
        try {
            SharedPreferences settings = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            float result = settings.getFloat(sharedKey, defaultValue);
            return result;
        } catch (Exception e) {
            LogService.error(e);
        }
        return defaultValue;
    }

    /**
     * Saving float to SharedPref
     * @param sharedKey
     * @param sharedValue
     */
    public static void saveFloat(String sharedKey, float sharedValue) {
        LogService.log("sharedKey = " + sharedKey);

        try {
            Context context = AppApplication.getContext();
            if (context == null) {
                return;
            }

            SharedPreferences prefs = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat(sharedKey, sharedValue);
            editor.commit();
        } catch (Exception e) {
            LogService.error(e);
        }
    }

    /**
     * Getting integer from SharedPref
     * @param sharedKey
     * @param defaultValue
     * @return
     */
    public static int getInteger(String sharedKey, int defaultValue) {
        LogService.log("sharedKey = " + sharedKey);

        Context context = AppApplication.getContext();
        if (context == null) {
            return defaultValue;
        }
        try {
            SharedPreferences settings = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            int result = settings.getInt(sharedKey, defaultValue);
            return result;
        } catch (Exception e) {
            LogService.error(e);
        }
        return defaultValue;
    }

    /**
     * Saving integer to SharedPref
     * @param sharedKey
     * @param sharedValue
     */
    public static void saveInteger(String sharedKey, int sharedValue) {
        LogService.log("sharedKey = " + sharedKey);

        try {
            Context context = AppApplication.getContext();
            if (context == null) {
                return;
            }

            SharedPreferences prefs = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(sharedKey, sharedValue);
            editor.commit();
        } catch (Exception e) {
            LogService.error(e);
        }
    }
}