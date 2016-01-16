package com.imaniac.myo.moduleReceiver.services;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.imaniac.myo.ReadSmsActivity;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

/**
 * Created by yogeshmadaan on 12/10/15.
 */
public class NotificationService extends NotificationListenerService {

    Context context;
    private AudioManager myAudioManager;
    @Override

    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

    }
    @Override

    public void onNotificationPosted(StatusBarNotification sbn) {


//        String pack = sbn.getPackageName();
//        String ticker = sbn.getNotification().tickerText.toString();
//        Bundle extras = sbn.getNotification().extras;
//        String title = extras.getString("android.title");
//        String text = extras.getCharSequence("android.text").toString();
//
//        Log.i("Package",pack);
//        Log.i("Ticker", ticker);
//        Log.i("Title", title);
//        Log.i("Text", text);
//
//        Intent msgrcv = new Intent("Msg");
//        msgrcv.putExtra("package", pack);
//        msgrcv.putExtra("ticker", ticker);
//        msgrcv.putExtra("title", title);
//        msgrcv.putExtra("text", text);
//        Log.e("notificatoin", "received");
//        Intent intent1 = new Intent(context, ReadSmsActivity.class);
//        String temp[] = ticker.split(":");
//        intent1.putExtra("message",ticker.split(":")[temp.length-1]);
//        intent1.putExtra("sender",ticker.split(":")[0]);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent1);
//        if(Hub.getInstance().getConnectedDevices().size()>0)
//        {
//            Hub.getInstance().getConnectedDevices().get(0).vibrate(Myo.VibrationType.LONG);
//            Log.e("vibration","sent");
//
//        }
//        else
//            Log.e("Hub Size","0");


    }

    @Override

    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg", "Notification Removed");

    }
}
