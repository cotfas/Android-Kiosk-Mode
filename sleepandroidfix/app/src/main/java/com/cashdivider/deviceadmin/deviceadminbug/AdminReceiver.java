package com.cashdivider.deviceadmin.deviceadminbug;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AdminReceiver extends DeviceAdminReceiver
{
	public static final String TAG = "DeviceAdmin";
	
	@Override
	public void onEnabled(Context context, Intent intent)
	{
		Log.v(TAG, "AdminReceiver.onEnabled()");
	}
	
	@Override
	public void onDisabled(Context context, Intent intent)
	{
		Log.v(TAG, "AdminReceiver.onDisabled()");
	}
	
	@Override
	public void onPasswordChanged(Context context, Intent intent)
	{
		Log.v(TAG, "AdminReceiver.onPasswordChanged()");
	}
}
