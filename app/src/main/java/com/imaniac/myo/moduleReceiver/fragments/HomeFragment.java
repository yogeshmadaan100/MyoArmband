package com.imaniac.myo.moduleReceiver.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;
import com.imaniac.HomeAutomationActivity;
import com.imaniac.myo.R;
import com.imaniac.myo.ReadSmsActivity;
import com.imaniac.myo.moduleReceiver.CameraActivity;
import com.imaniac.myo.moduleReceiver.services.BackgroundService;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import java.lang.reflect.Method;

import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;


public class HomeFragment extends Fragment {
    EventBus eventBus;
    @OnClick(R.id.layout_add_device)
    public void addDevice()
    {
        startActivity(new Intent(getActivity(), ScanActivity.class));
    }
    @OnClick(R.id.layout_tutorial)
    public void showTutorial()
    {
        startActivity(new Intent(getActivity(), HomeAutomationActivity.class));
    }
    @OnClick(R.id.layout_about)
    public void aboutUs()
    {
        acceptCall(getActivity());
        if(Hub.getInstance().getConnectedDevices().size()>0)
        {
            Hub.getInstance().getConnectedDevices().get(0).vibrate(Myo.VibrationType.LONG);
        }
    }
    @OnClick(R.id.img_header)
    public void headerClick()
    {
        startActivity(new Intent(getActivity(), CameraActivity.class));
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,rootView);
        getActivity().startService(new Intent(getActivity(), BackgroundService.class));
        return rootView;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        eventBus = EventBus.getDefault();
        eventBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    public void onEvent(Pose pose)
    {
        Log.e("event received", "" + pose);
//        if(pose== Pose.FINGERS_SPREAD || pose == Pose.FIST)
//        {
//            startActivity(new Intent(getActivity(), CameraActivity.class));
//        }
    }
    private void rejectCall(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ITelephony telephonyService;
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(tm);

                telephonyService.endCall();
                Log.d("HANG UP", "harmeet");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void acceptCall(Context context){
        Log.e("accepting","call");
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        ITelephony telephonyService;
        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            telephonyService = (ITelephony) m.invoke(tm);

            telephonyService.answerRingingCall();
            Log.d("HANG UP", "harmeet");


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error cal",""+e);
        }
    }

}
