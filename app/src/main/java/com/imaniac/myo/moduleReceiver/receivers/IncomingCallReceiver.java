package com.imaniac.myo.moduleReceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.imaniac.AcceptCallActivity;
import com.imaniac.myo.app.MyApplication;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yogeshmadaan on 12/10/15.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    Context context;
    private int mInterval = 4000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    private AudioManager myAudioManager;
    public int mod;
    public static boolean isRinging = false;
    public static Intent intent ;

    @Override
    public void onReceive(final Context context, Intent intent){
        this.context = context;
        try{
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            mHandler =new Handler();
            myAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            mod=myAudioManager.getRingerMode();
            this.intent = intent;
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context, "Phone Is Ringing", Toast.LENGTH_LONG).show();
                startRepeatingTask();
                Intent intent1 = new Intent(context, AcceptCallActivity.class);
                intent1.putExtra("sender",intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
                if(Hub.getInstance().getConnectedDevices().size()>0) {
                    silence(context);

                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                Vibrator vib = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                                vib.cancel();
                                Log.e("cancelling","cancel");
                                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                                myAudioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
                            } catch (Exception e) {
                                Log.e("error", "" + e);
                            }
                            ;
                        }
                    }, 2000);

                }
                isRinging = true;
                Log.e("ringing", "true");
            }

            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                Toast.makeText(context, "Call Recieved", Toast.LENGTH_LONG).show();
                stopRepeatingTask();
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                isRinging = false;
                Log.e("ringin","false");
        }

            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context, "Phone Is Idle", Toast.LENGTH_LONG).show();
                stopRepeatingTask();
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            isRinging = false;
                Log.e("ringin","false");

            }
        }
        catch(Exception e){e.printStackTrace();}
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            if(Hub.getInstance().getConnectedDevices().size()>0)
            {
               if(isRinging){
                Hub.getInstance().getConnectedDevices().get(0).vibrate(Myo.VibrationType.LONG);
                Log.e("vibration","sent");}

            }
                mHandler.postDelayed(mStatusChecker, mInterval);
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }
    private void silence(Context context){
        Log.e("trying","silence");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ITelephony telephonyService;
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(tm);

            telephonyService.silenceRinger();
            Log.d("HANG UP", "harmeet");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}