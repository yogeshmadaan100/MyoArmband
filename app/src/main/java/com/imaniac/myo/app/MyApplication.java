package com.imaniac.myo.app;

import android.app.Application;
import android.content.Intent;

import com.imaniac.myo.moduleReceiver.services.BackgroundService;
import com.imaniac.myo.moduleReceiver.services.NotificationService;

/**
 * Created by yogeshmadaan on 12/10/15.
 */
public class MyApplication extends Application {

    MyApplication _instance;
    BackgroundService backgroundService ;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        startService(new Intent(getApplicationContext(), BackgroundService.class));
        startService(new Intent(getApplicationContext(), NotificationService.class));
        backgroundService = BackgroundService.getInstance();
    }

    public MyApplication get_instance() {
        return _instance;
    }

    public BackgroundService getBackgroundService() {
        return backgroundService;
    }
}
