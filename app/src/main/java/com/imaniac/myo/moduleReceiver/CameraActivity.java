package com.imaniac.myo.moduleReceiver;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.imaniac.myo.R;
import com.thalmic.myo.Pose;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.ShutterCallback, Camera.PictureCallback {

    Camera mCamera;
    SurfaceView mPreview;
    EventBus eventBus;
    Button button;
    ImageButton switchCamera;
    public int currentCameraId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Log.e("camera", "started");
        mPreview = (SurfaceView) findViewById(R.id.preview);
        mPreview.getHolder().addCallback(this);
        mPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSnapClick();
            }
        });
        mCamera = Camera.open();
        currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        Log.e("camera", "opened");
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        switchCamera = (ImageButton) findViewById(R.id.switch_camera);
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera!=null) {
                    mCamera.stopPreview();
                }
                //NB: if you don't release the current camera before switching, you app will crash
                mCamera.release();

                //swap the id of the camera to be used
                if(currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    Log.e("opening","front camera");
                }
                else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    Log.e("opening","back camera");
                }
                mCamera = Camera.open(currentCameraId);
                //Code snippet for this method from somewhere on android developers, i forget where
                try {
                    //this step is critical or preview on new camera will no know where to render to
                    mCamera.setPreviewDisplay(mPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Camera.Parameters params = mCamera.getParameters();
                List<Camera.Size> sizes = params.getSupportedPreviewSizes();
                Camera.Size selected = sizes.get(currentCameraId);
                params.setPreviewSize(selected.width, selected.height);
                mCamera.setParameters(params);

                mCamera.setDisplayOrientation(90);
                mCamera.startPreview();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.stopPreview();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCamera.release();
        eventBus.unregister(this);
        Log.d("CAMERA", "Destroy");
    }

    public void onCancelClick(View v) {
        finish();
    }

    public void onSnapClick() {
        mCamera.takePicture(this, null, null, this);
    }

    @Override
    public void onShutter() {
        Toast.makeText(this, "Click!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //Here, we chose internal storage
        try {
            FileOutputStream out = openFileOutput("picture.jpg", Activity.MODE_PRIVATE);
            out.write(data);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File pictureFile = getOutputMediaFile();
        Log.e("path file 11111111", ""+pictureFile);
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();

        } catch (FileNotFoundException e) {

        } catch (Exception e) {

        }
        Log.e("file saved", "" + pictureFile.toString());
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(pictureFile)));
        Toast.makeText(getApplicationContext(),"Picture Captured",Toast.LENGTH_SHORT).show();
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Camera.Parameters params = mCamera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPreviewSizes();
        Camera.Size selected = sizes.get(currentCameraId);
        params.setPreviewSize(selected.width, selected.height);
        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(90);
        mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(mPreview.getHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("PREVIEW", "surfaceDestroyed");
    }
    public void onEvent(Pose pose)
    {
        Log.e("event received", "" + pose);

        if (pose==Pose.FINGERS_SPREAD || pose == Pose.FIST)
        {
            button.performClick();
            button.invalidate();
        }
        if(pose == Pose.WAVE_OUT)
        {
            switchCamera.performClick();
            switchCamera.invalidate();
        }
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Myo Camera");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }


        File mediaFile = new File(String.format(mediaStorageDir+File.separator+"%d.jpg", System.currentTimeMillis()));

        return mediaFile;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}