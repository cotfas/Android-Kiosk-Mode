package com.cashdivider.deviceadmin;

import android.app.Notification;
import android.content.Context;

import br.com.goncalves.pugnotification.notification.PugNotification;

/**
 * Created by work on 3/28/17.
 */

public class NotificationUtils {

    private static final int IDENTIFIER = 1111;

    public static void create(Context context) {

        PugNotification.with(context)
                .load()
                .identifier(IDENTIFIER)
                .ongoing(true)
                .autoCancel(false)
                .onlyAlertOnce(true)
                .priority(Notification.PRIORITY_MAX)
                /*.title(R.string.app_name)*/
                .title(R.string.textNotification)
                /*.message(R.string.textNotification)
                .bigTextStyle(R.string.textNotification)*/
                .smallIcon(R.drawable.pugnotification_ic_launcher)
                /*.largeIcon(R.drawable.pugnotification_ic_launcher)*/
                .simple()
                .build();
    }

    public static void cancel(Context context) {
        PugNotification.with(context).cancel(IDENTIFIER);
    }
}
