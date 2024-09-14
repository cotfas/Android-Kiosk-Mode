package com.cashdivider.deviceadmin.other;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cashdivider.deviceadmin.WindowChangeDetectingService;

/**
 * Created by work on 3/29/17.
 */

public class SleepStarter extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        WindowChangeDetectingService.startSleep();

        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
