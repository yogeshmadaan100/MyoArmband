package com.android.internal.telephony;

/**
 * Created by yogeshmadaan on 31/10/15.
 */
public interface ITelephony {
    boolean endCall();
    void answerRingingCall();
    void silenceRinger();
}
