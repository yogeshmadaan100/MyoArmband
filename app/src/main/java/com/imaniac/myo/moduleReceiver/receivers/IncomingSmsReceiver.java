package com.imaniac.myo.moduleReceiver.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.imaniac.myo.ReadSmsActivity;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

/**
 * Created by yogeshmadaan on 12/10/15.
 */
public class IncomingSmsReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {
            SmsMessage[] msgs = null;

            String str = "";

            if (bundle != null) {
                // Retrieve the SMS Messages received
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];


                // For every SMS message received
                for (int i = 0; i < msgs.length; i++) {
                    // Convert Object array
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[0]);
                    // Sender's phone number
                    str += "SMS from " + msgs[i].getOriginatingAddress() + " : ";
                    // Fetch the text message
                    str += msgs[i].getMessageBody().toString();
                    // Newline <img src="http://codetheory.in/wp-includes/images/smilies/simple-smile.png" alt=":-)" class="wp-smiley" style="height: 1em; max-height: 1em;">
                    str += "\n";
                    Intent intent1 = new Intent(context, ReadSmsActivity.class);
                    intent1.putExtra("message",msgs[i].getMessageBody().toString());
                    intent1.putExtra("sender",msgs[i].getOriginatingAddress());
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                    Log.e("calling","text to speech");
                }

                // Display the entire SMS Message
                Log.d("message", str);

            }
//            if (bundle != null) {
//
//                final Object[] pdusObj = (Object[]) bundle.get("pdus");
//
//                for (int i = 0; i < pdusObj.length; i++) {
//
//                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
//                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
//
//                    String senderNum = phoneNumber;
//                    String message = currentMessage.getDisplayMessageBody();
//
//                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);
//
//
//                    // Show Alert
//                    int duration = Toast.LENGTH_LONG;
//                    Toast toast = Toast.makeText(context,
//                            "senderNum: " + senderNum + ", message: " + message, duration);
//                    toast.show();
//                    if(Hub.getInstance().getConnectedDevices().size()>0)
//                    {
//                        Hub.getInstance().getConnectedDevices().get(0).vibrate(Myo.VibrationType.SHORT);
//                    }
//                    Intent intent1 = new Intent(context, ReadSmsActivity.class);
//                    intent1.putExtra("message",message);
//                    intent1.putExtra("sender",senderNum);
//                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent1);
//                    Log.e("calling","text to speech");
//
//                } // end for loop
//            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}