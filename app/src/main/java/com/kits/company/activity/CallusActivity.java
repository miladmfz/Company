package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.kits.company.R;

import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
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


        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
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
        kowsarsamaneh.setText(Farsi_number.PerisanNumber("تمامی حقوق این نرم افزار متعلق به گروه نرم افزاری کوثر می باشد شماره تماس3–66569320"));

        text1 =findViewById(R.id.callus_text1);
        text2 =findViewById(R.id.callus_text2);





        Call<String> call1 = apiInterface.info("kowsar_info",66);//66 company
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    text1.setText(response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });


        Call<String> call2 = apiInterface.info("kowsar_info",438);//438 phone
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    text2.setText(response.body());
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("onFailure",t.toString());
            }
        });

    }




}
