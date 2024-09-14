package com.cashdivider.deviceadmin.other;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.cashdivider.deviceadmin.NotificationListener;

/**
 * Created by cotfas on 3/21/2016.
 */
@Deprecated
public class AlarmReceiver extends BroadcastReceiver {


    public static final String ALARM_ALERT_CHANGES_ACTION = "android.app.action.NEXT_ALARM_CLOCK_CHANGED";

    public static final String SLEEP_ALARM_STARTED = "com.urbandroid.sleep.alarmclock.ALARM_ALERT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "intent = " + intent.toString());

        String action = intent.getAction();
        if (action.equals(ALARM_ALERT_CHANGES_ACTION)) {
            //execute(context);
        } else if (action.contains(SLEEP_ALARM_STARTED)) {
            // TODO does not longer works
            //NotificationListener.executeAlarmStarted(context, true);
        }
    }

    private static boolean isExecuting = false;
    private static void execute(final Context context) {
        if (isExecuting) {
            return;
        }
        isExecuting = true;

        Handler handler = new Handler(context.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                NotificationListener.executeAnyAlarmEnabled(context, hasAnyAlarms(context));

                isExecuting = false;
            }
        }, 1500);
    }

    public static synchronized boolean hasAnyAlarms(Context context) {

        // TODO it crashes on new device with permission > so we return true
        if (true) return true;


        boolean result = false;

        ContentProviderClient yourCR = null;
        Cursor yourCursor = null;
        try {
            Uri yourURI = Uri.parse("content://com.urbandroid.sleep.alarmclock/alarm");
            yourCR = context.getContentResolver().acquireContentProviderClient(yourURI);

            String whereClause = "enabled"
                    + "=?";

            String[] args = new String[]{"1"};
            yourCursor = yourCR.query(yourURI, null, whereClause, args, null);

            if (yourCursor != null && yourCursor.moveToFirst()) {
                //if the row exist then return the id
                int row = yourCursor.getInt(yourCursor.getColumnIndex("_id"));
                if (row != -1) {
                    result = true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (yourCursor != null) {
                yourCursor.close();
            }
            if (yourCR != null) {
                yourCR.release();
            }
        }

        Log.i("AlarmReceiver", "hasAnyAlarms = " + result);

        return result;
    }
}