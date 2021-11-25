package com.kits.company.adapter;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.kits.company.application.App;

public class GetShared extends Application {
    private static SharedPreferences shPref;
    private static SharedPreferences.Editor sEdit;


    public static void EditString(String Key ,String Value){
        shPref = App.getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        sEdit.putString(Key, Value);
        sEdit.apply();
    }

    public static String ReadString(String Key){

        shPref = App.getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        return shPref.getString(Key, "");
    }

    public static boolean ReadBoolan(String Key){
        shPref = App.getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        return shPref.getBoolean(Key, true);
    }

    public static void EditBoolan(String Key ,boolean Value){
        shPref = App.getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        sEdit.putBoolean(Key, Value);
        sEdit.apply();
    }

    public static boolean firstStart(){
        shPref = App.getContext().getSharedPreferences("profile", Context.MODE_PRIVATE);
        return shPref.getBoolean("firstStart", true);
    }


    public void Create(){
        sEdit.putBoolean("firstStart", false);
        EditString("ItemsShow","3");
        sEdit.apply();
    }


    
    
}
