package com.cashdivider.deviceadmin.policy;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.os.Bundle;

import com.cashdivider.deviceadmin.R;
import com.cashdivider.deviceadmin.application.AppApplication;

public class PolicyActivity extends Activity {
	private static final int REQ_ACTIVATE_DEVICE_ADMIN = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startPolicy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_ACTIVATE_DEVICE_ADMIN) {
			if (resultCode != RESULT_OK) {
				startPolicy();
			} else {
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startPolicy() {
		Intent activateDeviceAdminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		activateDeviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
				AppApplication.getPolicy().getPolicyAdmin());
		
		// It is good practice to include the optional explanation text to explain to
		// user why the application is requesting to be a device administrator.  The system
		// will display this message on the activation screen.
		activateDeviceAdminIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, 
				this.getResources().getString(R.string.device_admin_activation_message));

		//activateDeviceAdminIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		this.startActivityForResult(activateDeviceAdminIntent, REQ_ACTIVATE_DEVICE_ADMIN);

	}
}
