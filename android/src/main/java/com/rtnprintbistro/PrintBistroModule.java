package com.rtnprintbistro;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import com.rtnprintbistro.NativePrintBistroSpec;

import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.content.pm.PackageManager;
import android.content.IntentFilter;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import android.app.Activity;

public class PrintBistroModule extends NativePrintBistroSpec {

    public static String NAME = "RTNPrintBistro";
    private static ReactApplicationContext reactContext;
    public static final int SYSTEM_ALERT_WINDOW = 1000;
    public static final int OVERLAY_PERMISSION_REQ_CODE = 2000;
    String[] permissions = {"android.permission.SYSTEM_ALERT_WINDOW",};


    PrintBistroModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        checkPermissions();
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void checkPermissions() {
        try{
            Activity  mActivity = reactContext.getCurrentActivity();
            // Checking if device version > 22 and we need to use new permission model
            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP_MR1) {
                // Checking if we can draw window overlay
                if (!Settings.canDrawOverlays(mActivity)) {
                    // Requesting permission for window overlay(needed for all react-native apps)
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + mActivity.getPackageName()));
                    mActivity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
                for(String perm : permissions){
                    // Checking each persmission and if denied then requesting permissions
                    if(mActivity.checkSelfPermission(perm) == PackageManager.PERMISSION_DENIED){
                        mActivity.requestPermissions(permissions, SYSTEM_ALERT_WINDOW);
                        break;
                    }
                }
            }
        }catch(Exception e){
            // delay 2000 ms to allow the app to start
            new Thread(new Runnable() {
                @Override
                public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                checkPermissions();
                }
            }).start(); 
        }
    }

    @Override
    public void closeApp() {
        try{
            Activity  mActivity =  reactContext.getCurrentActivity();
        
            mActivity.stopLockTask();
            mActivity.finish();
         }catch(Exception e){
            Toast.makeText( reactContext, "Error closing app", Toast.LENGTH_LONG).show();
        }   
    }

    @Override
    public void startKioskMode() {
        try{
            Activity  mActivity =  reactContext.getCurrentActivity();
        
            mActivity.startLockTask();
        }catch(Exception e){
            Toast.makeText( reactContext, "Error starting lock task", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void stopKioskMode() {
        try{
            Activity  mActivity =  reactContext.getCurrentActivity();

            mActivity.stopLockTask();
        }catch(Exception e){
            Toast.makeText( reactContext, "Error stopping lock task", Toast.LENGTH_LONG).show();
        }
    }
}