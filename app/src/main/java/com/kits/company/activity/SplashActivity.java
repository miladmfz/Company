package com.kits.company.activity;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    Intent intent;
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        config();
        Button splash_refresh= findViewById(R.id.splash_refresh);
        InternetConnection ic = new  InternetConnection(this);
        if(ic.has()){

            try {
                init();
            }catch (Exception e){
                GetShared.ErrorLog(e.getMessage());
            }

        }
        else{
            final Dialog dialog1 ;
            dialog1 = new Dialog(this);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog1.setContentView(R.layout.connection_fail);
            dialog1.show();
            Button to_setting = dialog1.findViewById(R.id.to_setting);
            to_setting.setOnClickListener(view -> startActivity(new Intent(Settings.ACTION_SETTINGS)));
            splash_refresh.setVisibility(View.VISIBLE);

            App.showToast("عدم اتصال به اینترنت");
        }

        splash_refresh.setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });
    }



    public void config() {


        GetShared.EditBoolan("available_good", true);
        GetShared.EditString("AppBasketItem", "one");

        if (GetShared.ReadBoolan("firstStart")) {

            GetShared.EditBoolan("firstStart", false);

            GetShared.EditString("Active", "-1");
            GetShared.EditString("fname", " ");
            GetShared.EditString("lname", " ");
            GetShared.EditString("mobile", " ");
            GetShared.EditString("email", " ");
            GetShared.EditString("address", "معرفی نشده");
            GetShared.EditString("PostalCode", "معرفی نشده");
            GetShared.EditString("CustomerName", "معرفی نشده");
            GetShared.EditString("img", " ");
            GetShared.EditString("basket_position", " ");
            GetShared.EditString("AppBasketItem", "one");
            GetShared.EditString("view", "grid");

        }
    }

    public void init() {


        Call<RetrofitResponse> call1 = apiInterface.info("check_server","0");
        call1.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if(response.body().getText().equals("false")){
                        if (GetShared.ReadString("Active").equals("1")) {
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                        }else {

                            GetShared.EditString("Active", "-1");
                            GetShared.EditString("fname", " ");
                            GetShared.EditString("lname", " ");
                            GetShared.EditString("mobile", " ");
                            GetShared.EditString("email", " ");
                            GetShared.EditString("address", "معرفی نشده");
                            GetShared.EditString("PostalCode", "معرفی نشده");
                            GetShared.EditString("CustomerName", "معرفی نشده");
                            GetShared.EditString("img", " ");
                            GetShared.EditString("basket_position", " ");
                            GetShared.EditString("view", "grid");


                            intent = new Intent(SplashActivity.this, RegisterActivity.class);
                        }
                        startActivity(intent);
                        finish();
                    }else {
                        GetShared.ErrorLog("server disconnect");
                        App.showToast( "ارتباط با سرور میسر نمی باشد.");
                    }
                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                App.showToast(t.getMessage());
                App.showToast("ارتباط با سرور میسر نمی باشد.");
                init();
            }
        });
    }


}
