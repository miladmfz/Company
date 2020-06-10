package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;


import com.kits.company.R;
import com.kits.company.adapter.InternetConnection;

import java.util.Objects;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        config();
        InternetConnection ic = new  InternetConnection(SplashActivity.this);
        if(ic.has()){

            init();
        }
        else{
            final Dialog dialog1 ;
            dialog1 = new Dialog(this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.setContentView(R.layout.connection_fail);
            dialog1.show();
            Button to_setting = dialog1.findViewById(R.id.to_setting);
            to_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });
            Toast.makeText(getApplicationContext(), "عدم اتصال به اینترنت", Toast.LENGTH_LONG).show();
        }
        Button splash_refresh= findViewById(R.id.splash_refresh);
        splash_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
    }



    public void config() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        boolean firstStart = shPref.getBoolean("firstStart", true);
        sEdit = shPref.edit();
        if (firstStart) {
            sEdit.putBoolean("firstStart", false);
            sEdit.putString("Active", "-1");
            sEdit.putString("fname", " ");
            sEdit.putString("lname", " ");
            sEdit.putString("mobile", " ");
            sEdit.putString("email", " ");
            sEdit.putString("address", "معرفی نشده");
            sEdit.putString("PostalCode", "معرفی نشده");
            sEdit.putString("img", " ");
            sEdit.putString("basket_position", " ");
            sEdit.putString("view", "grid");
            sEdit.apply();
        }
    }

    public void init() {
        if (Integer.parseInt(Objects.requireNonNull(shPref.getString("Active", null)))== 1)
        {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            intent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void setupBadge() {


    }

}
