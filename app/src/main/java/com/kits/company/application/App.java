package com.kits.company.application;

import android.app.Application;

import com.kits.company.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iransansmobile_medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
