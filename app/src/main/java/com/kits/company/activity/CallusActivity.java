package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kits.company.R;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallusActivity extends AppCompatActivity {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

TextView text1,text2,text3,text_tel;

Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callus);


        InternetConnection ic =new  InternetConnection(App.getContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(App.getContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

//********************************************************************************************

    public void setupBadge() {


    }


    public void init() {
        Button kowsarsamaneh;
        kowsarsamaneh = findViewById(R.id.callus_kits);
        kowsarsamaneh.setText(NumberFunctions.PerisanNumber("تمامی حقوق این نرم افزار متعلق به گروه نرم افزاری کوثر می باشد شماره تماس3–66569320"));

        text1 =findViewById(R.id.callus_text1);
        text2 =findViewById(R.id.callus_text2);
        text3 =findViewById(R.id.callus_text3);





        Call<RetrofitRespons> call1 = apiInterface.info("kowsar_info","CompanyName");
        call1.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    text1.setText(response.body().getText());
                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });

        Call<RetrofitRespons> call2 = apiInterface.info("kowsar_info","phone");
        call2.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    text3.setText(response.body().getText());
                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });
        Call<RetrofitRespons> call3 = apiInterface.info("kowsar_info","address");
        call3.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;

                    text2.setText(response.body().getText());
                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });

    }




}
