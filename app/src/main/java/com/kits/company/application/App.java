package com.kits.company.application;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.kits.company.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {
    private static App  instance;
    private static Toast toast;
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iransansmobile_medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public App()
    {
        instance = this;
    }
    public static Context getContext() {
        return instance;

    }

    public static void showToast(String string) {
        if (toast!=null)
            toast.cancel();
        toast = Toast.makeText(instance, string, Toast.LENGTH_LONG);
        toast.show();

    }


}
