package com.cashdivider.deviceadmin;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.cashdivider.deviceadmin.application.AppApplication;
import com.cashdivider.deviceadmin.policy.Policy;
import com.cashdivider.deviceadmin.powerbutton.PowerButtonService;

/**
 * Created by cotfas on 3/21/2016.
 */
public class WindowChangeDetectingService extends AccessibilityService {

    // SLEEP DEFINITION
    public static String sleepPackageName = "com.urbandroid.sleep";
    public static String sleepActivityPath = ".alarmclock.AlarmAlertFullScreen";

    public static String captchapack = ".captchapack";
    public static String captcha = ".captcha";


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        //Configure these here for compatibility with API 13 and below.
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        try {
            onEvents(event);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onEvents(AccessibilityEvent event) {
        if (event == null) {
            return;
        }

        if (!NotificationListener.isIsNotificationRunning()) {
            return;
        }

        // Hide power menu when alarm running
        // From now on it works only from here
        if ((event.getClassName().toString().contains("globalactions"))) {
            PowerButtonService.closePowerDialog(getApplicationContext());
        }

        // Hide status bar
        if (event.getClassName().toString().contains("android.widget.FrameLayout")) {
            startSleep();
        }

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() == null) {
                Log.i("onEvents", " getPackageName = null");
                return;
            }

            ComponentName componentName = new ComponentName(
                    event.getPackageName().toString(),
                    event.getClassName().toString()
            );

            ActivityInfo activityInfo = tryGetActivity(componentName);
            boolean isActivity = activityInfo != null;
            if (isActivity)
            {
                Log.i("CurrentActivity", componentName.flattenToShortString());

                String currentActivity = componentName.flattenToShortString();


                // FORCE STARTING SLEEP ACTIVITY
                if (NotificationListener.isIsNotificationRunning()) {
                    Log.i("startSleep", " isNotificationRunning = true");

                    if (!(currentActivity.contains(sleepActivityPath)
                            || currentActivity.contains(captchapack)
                            || currentActivity.contains(captcha))) {


                        Log.i("startSleep", "true");

                        startSleep();
                    }
                }
                // FORCE STARTING SLEEP ACTIVITY

                //deviceAdminForcer(currentActivity);
            }
        }
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }

    private void deviceAdminForcer(String currentActivity) {
        // Device admin force
        String deviceAdmin = "com.android.settings";
        String deviceAdminAdd = ".DeviceAdminAdd";
        if (!AppApplication.getPolicy().isAdminActive()) {
            if (!currentActivity.contains(deviceAdminAdd)) {
                Policy.startPolicy(AppApplication.getContext());
            }
        }
        // Device admin force
    }

    public boolean isOnRingtone(Context context){
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(manager.getMode()==AudioManager.MODE_RINGTONE){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isOnAudio(Context context) {
        AudioManager manager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        if(manager.isMusicActive())
        {
            // do something - or do it not
            return true;
        }
        return false;
    }

    public static void startSleepDelayed() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startSleep();
            }
        }, 10);
    }
    public static void startSleep() {
        Context context = AppApplication.getContext();

        startSleep(context, sleepPackageName, sleepActivityPath);
    }
    public static void startSleep(Context context, String packageName, String activityPath) {
        if (NotificationListener.isIsNotificationRunning()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName(packageName, packageName + activityPath));

            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            context.startActivity(intent);

            Log.i("startActivity", "startSleep");
        } else {
            // Disable if no alarm is running
            NotificationListener.executeAlarmStarted(context, false);
        }
    }

    /**
     * Based on {@link com.android.settingslib.accessibility.AccessibilityUtils#getEnabledServicesFromSettings(Context,int)}
     * @see <a href="https://github.com/android/platform_frameworks_base/blob/d48e0d44f6676de6fd54fd8a017332edd6a9f096/packages/SettingsLib/src/com/android/settingslib/accessibility/AccessibilityUtils.java#L55">AccessibilityUtils</a>
     */
    @SuppressWarnings("JavadocReference")
    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(),  Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }

    public static boolean gotoAccessibilitySettings(Context context) {
        Intent settingsIntent = new Intent(
                Settings.ACTION_ACCESSIBILITY_SETTINGS);
        if (!(context instanceof Activity)) {
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        boolean isOk = true;
        try {
            context.startActivity(settingsIntent);
        } catch (ActivityNotFoundException e) {
            isOk = false;
        }
        return isOk;
    }

    public static void startAccessibilityIfNeeded(Context context) {
        if (!isAccessibilityServiceEnabled(context, WindowChangeDetectingService.class)) {
            gotoAccessibilitySettings(context);
        }
    }
}