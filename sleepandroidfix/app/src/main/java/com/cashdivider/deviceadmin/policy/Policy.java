package com.cashdivider.deviceadmin.policy;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.cashdivider.deviceadmin.application.AppApplication;

import java.io.Serializable;

public class Policy implements Serializable {
	private static final long serialVersionUID = 1L;

	private Context mContext;
	private DevicePolicyManager mDPM;
	private ComponentName mPolicyAdmin;
	private static boolean adminMode = false;
	
	public Policy(Context context) {
		this.mContext = context;
		mDPM = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mPolicyAdmin = new ComponentName(context, PolicyAdmin.class);
	}

	/**
	 * Getter for the policy administrator ComponentName object.
	 *
	 * @return
	 */
	public ComponentName getPolicyAdmin() { 
		return mPolicyAdmin; 
	}

	/**
	 * Indicates whether the device administrator is currently active.
	 *
	 * @return
	 */
	public boolean isAdminActive() {
		return mDPM.isAdminActive(mPolicyAdmin);
	}

	public boolean isActivePasswordSufficient() {
		return mDPM.isActivePasswordSufficient();
	}

	public boolean isDeviceSecured() {
		return isAdminActive() && isActivePasswordSufficient();
	}
	
	
	public static boolean isAdminMode() {
		return adminMode;
	}
	public static void setAdminMode(boolean adminMode) {
		Policy.adminMode = adminMode;
	}
	
	public static void disablePolicy(Context context) {
		setAdminMode(true);
		DevicePolicyManager mDPM = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDPM.removeActiveAdmin(AppApplication.getPolicy().getPolicyAdmin());
	}

	/**
	 * Through the PolicyAdmin receiver, the app can use this to trap various device
	 * administration events, such as password change, incorrect password entry, etc.
	 *
	 */
	public static class PolicyAdmin extends DeviceAdminReceiver {
		@Override
		public void onEnabled(Context context, Intent intent) {

		}

		@Override
		public void onDisabled(Context context, Intent intent) {
			// Called when the app is about to be deactivated as a device administrator.
			// Deletes previously stored password policy.
			super.onDisabled(context, intent);
			// SharedPreferences prefs = context.getSharedPreferences(APP_PREF, Activity.MODE_PRIVATE);
			// prefs.edit().clear().commit();
			
			if (!isAdminMode()){
				startPolicy(context);
			}
			setAdminMode(false);
		}

		@Override
		public CharSequence onDisableRequested(Context context, Intent intent) {
			//   SharedPreferences settings = context.getSharedPreferences(APP_PREF, 0);
			//   String DEVICE_ADMIN_CAN_DEACTIVATE = settings.getString("KEY_PASSWORD_LENGTH", null);
			//  if(DEVICE_ADMIN_CAN_DEACTIVATE.equals("ON")){
			/*                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMain);
                return "OOPS!";*/
			//   }
			//	return DEVICE_ADMIN_CAN_DEACTIVATE;
			//while (true);

	
			return null;
		}
	}

	public static void startPolicy(Context context) {
		Intent i = new Intent(context, PolicyActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	public static void openPolicyIfNotActive() {
		if (!AppApplication.getPolicy().isAdminActive()) {
			startPolicy(AppApplication.getContext());
		}
	}
}

