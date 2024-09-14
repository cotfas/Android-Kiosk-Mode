package com.cashdivider.deviceadmin.other;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by work on 3/29/17.
 */

/*
    http://stackoverflow.com/questions/7457730/preventing-status-bar-expansion
 */
public class StatusBar {

    private static CustomViewGroup view;

    public static void preventStatusBarExpansion(Context context) {
        if (view != null) {
            return;
        }

        WindowManager manager = ((WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|

                // this is to enable the notification to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        //http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int result = 0;
        if (resId > 0) {
            result = context.getResources().getDimensionPixelSize(resId);
        }

        localLayoutParams.height = result;

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        view = new CustomViewGroup(context);

        manager.addView(view, localLayoutParams);


        //hideNotificationBar(view);
    }

    public static void destroy(Context context) {
        if (view == null) {
            return;
        }

        try {
            WindowManager manager = ((WindowManager) context.getApplicationContext()
                    .getSystemService(Context.WINDOW_SERVICE));
            manager.removeViewImmediate(view);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class CustomViewGroup extends ViewGroup {

        public CustomViewGroup(Context context) {
            super(context);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.v("customViewGroup", "STATUS_BAR**********Intercepted");
            return true;
        }
    }

    /*
        http://stackoverflow.com/questions/2068084/kiosk-mode-in-android
     */
    public static void hideNotificationBar(final View view) {
        //final View view = (View) findViewById(android.R.id.content);
        if (view != null) {
            //"hides" back, home and return button on screen.
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN);
            view.setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            // Note that system bars will only be "visible" if none of the
                            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE |
                                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                        View.SYSTEM_UI_FLAG_IMMERSIVE |
                                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                        View.SYSTEM_UI_FLAG_FULLSCREEN);
                            }
                        }
                    });
        }
    }
}
