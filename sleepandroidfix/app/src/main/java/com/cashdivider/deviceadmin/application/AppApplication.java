package com.cashdivider.deviceadmin.application;

import android.app.Application;
import android.content.Context;

import com.cashdivider.deviceadmin.policy.Policy;

/**
 * Created by cotfas on 3/21/2016.
 */
public class AppApplication extends Application {

    private static Context context;

    private Policy policy;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        context = this;
    }

    private Context getContextAPI(){
        return context;
    }
    public static Context getContext(){
        return ((AppApplication)context.getApplicationContext()).getContextAPI();
    }


    private Policy getPolicyAPI() {
        if (policy == null) {
            policy = new Policy(context);
        }
        return this.policy;
    }
    private void setPolicyAPI(Policy policy) {
        this.policy = policy;
    }

    public static Policy getPolicy() {
        return ((AppApplication)context.getApplicationContext()).getPolicyAPI();
    }

    public static void setPolicy(Policy policy) {
        ((AppApplication)context.getApplicationContext()).setPolicyAPI(policy);
    }
}
