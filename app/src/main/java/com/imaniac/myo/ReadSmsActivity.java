package com.imaniac.myo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

import java.util.Locale;
import java.util.Timer;

public class ReadSmsActivity extends AppCompatActivity {
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private Speaker speaker;
    TextToSpeech t1;
    private int mInterval = 2000; // 5 seconds by default, can be changed later
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_sms);
        mHandler = new Handler();
        //checkTTS();
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int result = t1.setLanguage(Locale.US);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    } else {
                        Bundle bundle = getIntent().getExtras();
                        if(bundle!=null){

                            String text = bundle.getString("message");
                            String sender = getContactName(bundle.getString("sender"));
//            if(speaker==null)
//                speaker = new Speaker(this);
//                speaker.pause(LONG_DURATION);
//                speaker.speak("You have a new message from" + sender + "!" + text);
//                speaker.pause(SHORT_DURATION);
//                speaker.speak(text);
                            t1.speak("You have a new message from" + sender + "!" + text, TextToSpeech.QUEUE_FLUSH, null);
                            Log.e("speaking",""+text);
                            startRepeatingTask();
                        }
                    }

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

    }
    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }
    private String getContactName(String phone){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(0);
        }else {
            return "unknown number";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        speaker.destroy();
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        stopRepeatingTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_sms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            if (!t1.isSpeaking()) {
                stopRepeatingTask();
                Log.e("handler", "finished");
            }
            else
                mHandler.postDelayed(mStatusChecker, mInterval);
        }


    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

}
