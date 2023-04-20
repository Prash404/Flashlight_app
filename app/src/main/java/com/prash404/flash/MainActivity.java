package com.prash404.flash;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageButton toggleButton;
    boolean hasCameraFlash = false;
    boolean partyFlashOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        toggleButton = findViewById(R.id.toggleButton);
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        toggleButton.setOnClickListener(view -> {
            if(hasCameraFlash){
                if(partyFlashOn){
                    partyFlashOn = false;
                    toggleButton.setImageResource(R.drawable.power_off);
                    try {
                        flashOff();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    partyFlashOn = true;
                    toggleButton.setImageResource(R.drawable.power_on);
                    try {
                        flashOn();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                Toast.makeText(MainActivity.this,"This device does not have a camera flash", Toast.LENGTH_LONG);
            }
        });
    }

    private void flashOn() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId,true);
    }
    private void flashOff() throws CameraAccessException {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = cameraManager.getCameraIdList()[0];
        cameraManager.setTorchMode(cameraId,false);
    }

    private void flickerFlash() throws CameraAccessException, InterruptedException {
        while(partyFlashOn) {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId,true);
            Thread.sleep((500));
            cameraManager.setTorchMode(cameraId,false);
            Thread.sleep((500));
        }
    }
}