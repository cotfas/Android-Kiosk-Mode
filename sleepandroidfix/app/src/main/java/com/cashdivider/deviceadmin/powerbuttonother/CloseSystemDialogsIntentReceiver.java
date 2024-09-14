package com.cashdivider.deviceadmin.powerbuttonother;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cashdivider.deviceadmin.NotificationListener;
import com.cashdivider.deviceadmin.powerbutton.PowerButtonService;

/**
 * Created by cotfas on 5/13/16.
 */

/*
    http://stackoverflow.com/questions/10077905/override-power-button-just-like-home-button
 */

public class CloseSystemDialogsIntentReceiver extends BroadcastReceiver {
    public void onReceive(final Context context, final Intent intent) {
        Log.i("HERE", "------------------------------------HERE");
        //Toast.makeText(context, "POWER_BUTTON", Toast.LENGTH_LONG).show();

        if (NotificationListener.isIsNotificationRunning()) {
            PowerButtonService.closePowerDialog(context);
        }
    }
}

