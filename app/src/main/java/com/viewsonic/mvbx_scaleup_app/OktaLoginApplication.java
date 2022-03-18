package com.viewsonic.mvbx_scaleup_app;

import android.app.Application;

public class OktaLoginApplication extends Application {

    public static OktaManager oktaManager;

    @Override
    public void onCreate() {
        super.onCreate();
        oktaManager = new OktaManager(this);
    }
}
