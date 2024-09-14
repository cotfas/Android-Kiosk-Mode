package com.cashdivider.deviceadmin.powerbutton;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.cashdivider.deviceadmin.R;
import com.cashdivider.deviceadmin.WindowChangeDetectingService;

/**
 * Created by work on 3/28/17.
 */

/*
    http://stackoverflow.com/questions/39064523/detect-power-button-long-press/39177810
*/

public class PowerButtonService extends Service {

    private View mView;

    public PowerButtonService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (mView != null) {
            return;
        }

        LinearLayout mLinear = new LinearLayout(getApplicationContext()) {

            //home or recent button
            public void onCloseSystemDialogs(String reason) {
                if ("globalactions".equals(reason)) {
                    Log.i("Key", "Long press on power button");

                    closePowerDialog(getContext());


                    WindowChangeDetectingService.startSleep();


                } else if ("homekey".equals(reason)) {
                    //home key pressed
                    Log.i("Key", "home key pressed");


                    WindowChangeDetectingService.startSleepDelayed();


                } else if ("recentapps".equals(reason)) {
                    // recent apps button clicked
                    Log.i("Key", "recent apps button clicked");


                    WindowChangeDetectingService.startSleep();
                }

                Log.i("Key", "reason = " + reason);

            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                if (event.getKeyCode() == KeyEvent.KEYCODE_POWER) {

                    // when pressing power will also reopen screen

                    closePowerDialog(getContext());
                    WindowChangeDetectingService.startSleep();

                    return true;
                }

                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                        || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
                        || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
                        || event.getKeyCode() == KeyEvent.KEYCODE_CAMERA
                        || event.getKeyCode() == KeyEvent.KEYCODE_POWER) {

                }

                Log.i("Key", "keycode " + event.getKeyCode());

                WindowChangeDetectingService.startSleep();

                return super.dispatchKeyEvent(event);
            }
        };

        mLinear.setFocusable(true);

        mView = LayoutInflater.from(this).inflate(R.layout.service_layout, mLinear);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        //params
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                100,
                100,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        wm.addView(mView, params);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {

        if (mView != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeViewImmediate(mView);
        }

        super.onDestroy();
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static void startPowerButtonService(Context context) {
        if (isMyServiceRunning(context, PowerButtonService.class)) {
            return;
        }

        if (Settings.canDrawOverlays(context)) {
            context.startService(new Intent(context, PowerButtonService.class));

            // Trying to close power dialog if needed
            closePowerDialog(context);
        }
    }
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public static void stopPowerButtonService(Context context) {

        if (isMyServiceRunning(context, PowerButtonService.class)) {
            context.stopService(new Intent(context, PowerButtonService.class));

            //System.exit(0); /* NOT GOOD FOR AccessibilityService */
        }
    }

    public static void closePowerDialog(Context context) {
        Intent closeDialog = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(closeDialog);
    }
}