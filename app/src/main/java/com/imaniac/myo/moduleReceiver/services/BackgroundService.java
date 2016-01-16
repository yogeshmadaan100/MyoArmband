/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Distributed under the Myo SDK license agreement. See LICENSE.txt for details.
 */

package com.imaniac.myo.moduleReceiver.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.imaniac.myo.MainActivity;
import com.imaniac.myo.moduleReceiver.receivers.IncomingCallReceiver;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.XDirection;

import java.lang.reflect.Method;

import de.greenrobot.event.EventBus;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";

    private Toast mToast;
    private static BackgroundService _instance;
    EventBus eventBus = EventBus.getDefault();
    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onConnect(Myo myo, long timestamp) {
            Log.e("myo connected",""+Hub.getInstance().getConnectedDevices().get(0).getName());
            eventBus.post(new String("Connected"));
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            eventBus.post(new String("Disconnected"));

        }

        // onPose() is called whenever the Myo detects that the person wearing it has changed their pose, for example,
        // making a fist, or not making a fist anymore.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Show the name of the pose in a toast.
            switch (pose) {
                case UNKNOWN:
                    break;
                case REST:
                case DOUBLE_TAP:
                    switch (myo.getArm()) {
                        case LEFT:
                            break;
                        case RIGHT:
                            break;
                    }
                    break;
                case FIST:
                    break;
                case WAVE_IN:
                    Log.e("cancelling","call");
                   // cancelCall();
                    break;
                case WAVE_OUT:
                    Log.e("accepting","Call");
                    break;
                case FINGERS_SPREAD:
                    break;
            }

            if (pose != Pose.UNKNOWN && pose != Pose.REST) {
                // Tell the Myo to stay unlocked until told otherwise. We do that here so you can
                // hold the poses without the Myo becoming locked.
                myo.unlock(Myo.UnlockType.HOLD);

                // Notify the Myo that the pose has resulted in an action, in this case changing
                // the text on the screen. The Myo will vibrate.
                myo.notifyUserAction();
            } else {
                // Tell the Myo to stay unlocked only for a short period. This allows the Myo to
                // stay unlocked while poses are being performed, but lock after inactivity.
                myo.unlock(Myo.UnlockType.TIMED);
            }
            EventBus eventBus = EventBus.getDefault();
            eventBus.post(pose);
            Log.e("posting event",""+pose);
        }

        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            super.onArmSync(myo, timestamp, arm, xDirection);
            eventBus.post(new String("Synced"));

        }

        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            super.onArmUnsync(myo, timestamp);
            eventBus.post(new String("Unsynced"));

        }

        @Override
        public void onUnlock(Myo myo, long timestamp) {
            super.onUnlock(myo, timestamp);
            eventBus.post(new String("Unlocked"));

        }

        @Override
        public void onLock(Myo myo, long timestamp) {
            super.onLock(myo, timestamp);
            eventBus.post(new String("Locked"));

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            showToast("Couldn't initialize Hub");
            stopSelf();
            return;
        }

        // Disable standard Myo locking policy. All poses will be delivered.
        hub.setLockingPolicy(Hub.LockingPolicy.NONE);

        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);

        // Finally, scan for Myo devices and connect to the first one found that is very near.
        hub.attachToAdjacentMyo();
        _instance = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Service is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);

        Hub.getInstance().shutdown();
    }

    private void showToast(String text) {
        Log.w(TAG, text);
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public DeviceListener getListener() {
        return mListener;
    }

    public static BackgroundService getInstance() {
        return _instance;
    }
//    public void cancelCall()
//    {
//        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        try {
//            Class c = Class.forName(tm.getClass().getName());
//            Method m = c.getDeclaredMethod("getITelephony");
//            m.setAccessible(true);
//            ITelephony telephonyService = (ITelephony) m.invoke(tm);
//            Bundle bundle = IncomingCallReceiver.intent.getExtras();
//            String phoneNumber = bundle.getString("incoming_number");
//            Log.d("INCOMING", phoneNumber);
//            if ((phoneNumber != null)) {
//                telephonyService.endCall();
//                Log.d("HANG UP", phoneNumber);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void acceptCall(){
//        TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
//        try {
//            Class c = Class.forName(tm.getClass().getName());
//            Method m = c.getDeclaredMethod("getITelephony");
//            m.setAccessible(true);
//            ITelephony telephonyService = (ITelephony) m.invoke(tm);
//            Bundle bundle = IncomingCallReceiver.intent.getExtras();
//            String phoneNumber = bundle.getString("incoming_number");
//            Log.d("INCOMING", phoneNumber);
//            if ((phoneNumber != null)) {
//                telephonyService.answerRingingCall();
//                Log.d("HANG UP", phoneNumber);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


}
