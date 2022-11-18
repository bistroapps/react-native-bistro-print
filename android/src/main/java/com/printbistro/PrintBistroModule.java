package com.printbistro;

import androidx.annotation.NonNull;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.printbistro.NativePrintBistroSpec;

// print
import com.facebook.react.bridge.Promise;
import android.content.Intent;
import android.app.Activity;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.content.ActivityNotFoundException;



public class PrintBistroModule extends NativePrintBistroSpec {

    private static final int REQUEST_CODE = 2000;
    private static final int REQUEST_CODE_PRINT = 2001;

    public static String NAME = "RTNPrintBistro";
    private static ReactApplicationContext reactContext;
    private Promise promise;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            try{
                String result = "";
                //alert("requestCode: " + requestCode);

                if(intent == null) {
                    promise.resolve(false);
                    return;
                }

                if (requestCode == REQUEST_CODE) {
                    result = resultCode == Activity.RESULT_CANCELED ? intent.getStringExtra("status") : intent.getStringExtra("printes");
                    promise.resolve(result);
                }

                if (requestCode == REQUEST_CODE_PRINT) {
                    result = intent.getStringExtra("status");
                    promise.resolve(result);
                }
            } catch (Exception e) {
                if(e != null) {
                    promise.reject("Error: " + e.getMessage());
                   
                    e.printStackTrace();
                }else{
                    promise.reject("Erro na comunicação com o serviço.");
                }
            }
        }
    };

    PrintBistroModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;

        context.addActivityEventListener(mActivityEventListener);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void isAppInstalled(String packageName, Promise promise) {
        List<PackageInfo> packList = this.reactContext.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packList.size(); i++) {
            PackageInfo packInfo = packList.get(i);
            if (((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) && (packInfo.packageName.toString().compareTo(packageName) == 0)) {
                promise.resolve(true);
                return;
            }
        }

        promise.resolve(false);
    }

    @Override
    public void startAppByPackageName(String packageName, final Promise promise) {
        if (packageName != null) {
            Intent launchIntent = this.reactContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                getReactApplicationContext().startActivity(launchIntent);
                promise.resolve(true);
                return;
            } else {
                promise.reject("could not start app");
                return;
            }
        }
        promise.reject("package name missing"); 
    }

    @Override
    public void getPrinters(final Promise promise){
        this.promise = promise;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("service://retornoconfiguracao"));
            getCurrentActivity().startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            promise.reject("Erro ao buscar impressoras. O serviço está instalado?");
        } catch (Exception e) {
            if(e != null) {
              promise.reject("Error: " + e.getMessage());
                //alert("Error: " + e.getMessage());
                e.printStackTrace();
            }else{
                promise.reject("Erro ao buscar impressoras");
            }
        }
    }

    @Override
    public void print(String printerCode, String jsonInput, final Promise promise){
        try {
            this.promise = promise;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("service://serviceprinter"));
            intent.putExtra("cupom", jsonInput);
            intent.putExtra("codigoImpressora", printerCode);
            getCurrentActivity().startActivityForResult(intent, REQUEST_CODE_PRINT);
        }
        catch (ActivityNotFoundException e) {
            promise.reject("Erro ao imprimir. O serviço está instalado?");
        } catch (Exception e) {
            if(e != null) {
                promise.reject("Error: " + e.getMessage());
                //alert("Error: " + e.getMessage());
                e.printStackTrace();
            }else{
                promise.reject("Erro ao imprimir");
            }
        }  
    }
}