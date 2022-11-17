package com.printbistro;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import java.util.Map;
import java.util.HashMap;

import com.printbistro.NativePrintBistroSpec;

import androidx.annotation.NonNull;


public class PrintBistroModule extends NativePrintBistroSpec {

    public static String NAME = "RTNPrintBistro";
    private static ReactApplicationContext reactContext;
   


    PrintBistroModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void isAppInstalled(Promise promise) {
        promise.resolve(true);   
    }

    @Override
    public void startAppByPackageName(String packageName, final Promise promise) {
        promise.resolve(false);  
    }

    @Override
    public void getPrinters(final Promise promise){
        promise.resolve(this.NAME);  
    }

    @Override
    public void print(String printerCode, String jsonInput, final Promise promise){
        promise.resolve(jsonInput);  
    }
}