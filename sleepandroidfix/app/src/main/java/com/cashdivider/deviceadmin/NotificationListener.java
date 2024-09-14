package com.cashdivider.deviceadmin;

import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.cashdivider.deviceadmin.other.AlarmReceiver;
import com.cashdivider.deviceadmin.other.StatusBar;
import com.cashdivider.deviceadmin.powerbutton.PowerButtonService;
import com.cashdivider.deviceadmin.utils.SharedPrefUtils;

/**
 * Created by cotfas on 3/21/2016.
 */

public class NotificationListener extends NotificationListenerService {

    private static final String NOTIFICATION_RUNNING_KEY = "NOTIFICATION_RUNNING_KEY";

    private static final int sleepNotificationFlag = 99;
    private static final int sleepNotificationFlagOther = 227;

    private static boolean isIsNotificationRunning = false;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if (sbn.getPackageName().contains(WindowChangeDetectingService.sleepPackageName)) {

            Log.i("onNotificationPosted", sbn.getNotification().toString());

            if (isNotificationValid(sbn)) {

                executeAnyAlarmEnabled(this, true);

                executeAlarmStarted(this, true);
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if (sbn.getPackageName().contains(WindowChangeDetectingService.sleepPackageName)) {

            Log.i("onNotificationRemoved", sbn.getNotification().toString());

            if (isNotificationValid(sbn)) {

                executeAlarmStarted(this, false);

                executeAnyAlarmEnabled(this, false);
            }
        }
    }

    public static void executeAlarmStarted(Context context, boolean isNotificationRunning) {
        NotificationListener.setIsNotificationRunning(isNotificationRunning);

        if (isNotificationRunning) {
            PowerButtonService.startPowerButtonService(context);
            StatusBar.preventStatusBarExpansion(context);
        } else {
            StatusBar.destroy(context);
            PowerButtonService.stopPowerButtonService(context);
        }
    }

    public static void executeAnyAlarmEnabled(Context context, boolean hasAnyAlarmEnabled) {
        if (hasAnyAlarmEnabled) {
            NotificationUtils.create(context);
            WindowChangeDetectingService.startAccessibilityIfNeeded(context);

        } else {
            NotificationUtils.cancel(context);
        }
    }

    public static boolean isIsNotificationRunning() {
        return isIsNotificationRunning;
        //return SharedPrefUtils.getBoolean(NOTIFICATION_RUNNING_KEY);
    }

    public static void setIsNotificationRunning(boolean isNotificationRunning) {
        NotificationListener.isIsNotificationRunning = isNotificationRunning;
        //SharedPrefUtils.saveBoolean(NOTIFICATION_RUNNING_KEY, isNotificationRunning);
    }

    private boolean isNotificationValid(StatusBarNotification sbn) {
        if (sbn.getNotification().flags == sleepNotificationFlag) {
            return true;
        } else if (sbn.getNotification().flags == sleepNotificationFlagOther) {
            return true;
        }
        return false;
    }
}