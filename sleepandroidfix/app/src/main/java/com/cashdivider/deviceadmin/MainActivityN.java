package com.cashdivider.deviceadmin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;

import com.cashdivider.deviceadmin.application.AppApplication;
import com.cashdivider.deviceadmin.policy.Policy;
import com.cashdivider.deviceadmin.powerbutton.PowerButtonService;

public class MainActivityN extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void onNotificationAccess(View view) {
		Intent intent = new Intent(
				"android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		startActivity(intent);

	}

	public void onAccessibilityAccess(View view) {
		WindowChangeDetectingService.gotoAccessibilitySettings(this);

	}

	public void onDeviceAccess(View view) {
		Policy.startPolicy(AppApplication.getContext());
	}


	/*POWER BUTTON*/
	public final static int REQUEST_CODE = 10101;

	public boolean checkDrawOverlayPermission() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if (!Settings.canDrawOverlays(this)) {
			/** if not construct intent to request permission */
			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
					Uri.parse("package:" + getPackageName()));
			/** request permission via start activity for result */
			startActivityForResult(intent, REQUEST_CODE);
			return false;
		} else {
			return true;
		}
	}

	@Override
	@TargetApi(Build.VERSION_CODES.M)
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE) {
			PowerButtonService.startPowerButtonService(this);
		}
	}

	public void onStartHeadsUpDisplay(View view) {
		/*POWER BUTTON*/
		if (checkDrawOverlayPermission()) {
			PowerButtonService.startPowerButtonService(this);
		}
		/*POWER BUTTON*/
	}
	/*POWER BUTTON*/

	public void onBatteryOptimisation(View view) {
		enableOptimization(this);
	}

	@TargetApi(Build.VERSION_CODES.M)
	public void enableOptimization(Context context) {
		Intent intent = new Intent();
		String packageName = context.getPackageName();
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		if (pm.isIgnoringBatteryOptimizations(packageName))
			intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
		else {
			intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
			intent.setData(Uri.parse("package:" + packageName));
		}
		context.startActivity(intent);
	}
}
