package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.kits.company.BuildConfig;
import com.kits.company.application.App;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.APIVerification;
import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

import retrofit2.Call;
import retrofit2.Callback;

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

    public static void ErrorLog(String ErrorStr) {

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(App.getContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID);


        PersianCalendar calendar1 = new PersianCalendar();
        //String version= BuildConfig.VERSION_NAME;
        String appname= App.getContext().getPackageName();



        APIInterface apiInterface = APIVerification.getCleint().create(APIInterface.class);
        Call<RetrofitResponse> cl = apiInterface.Errorlog("Errorlog"
                , ErrorStr
                , ReadString("mobile")
                , android_id
                , appname
                , calendar1.getPersianShortDateTime()
                , "version");
        cl.enqueue(new Callback<RetrofitResponse>() {@Override
        public void onResponse(@NonNull Call<RetrofitResponse> call, @NonNull retrofit2.Response<RetrofitResponse> response) {
            assert response.body() != null; }



            @Override
            public void onFailure(@NonNull Call<RetrofitResponse> call, @NonNull Throwable t) {
            }
        });

    }




}
