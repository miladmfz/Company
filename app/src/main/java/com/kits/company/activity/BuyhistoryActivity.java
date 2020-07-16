package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kits.company.R;
import com.kits.company.adapter.Good_buy_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.Prefactor_Adapter;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.PreFactor;
import com.kits.company.model.PreFactorRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyhistoryActivity extends AppCompatActivity {


    private DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private SharedPreferences shPref;
    ArrayList<PreFactor> preFactors;
    private APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    ProgressBar prog;
    Prefactor_Adapter adapter;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyhistory);


        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }
    //*****************************************


    public void setupBadge() {


    }


    public void init() {

        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        final RecyclerView re = findViewById(R.id.BuyhistoryActivity_R1);
        prog = findViewById(R.id.BuyhistoryActivity_prog);



        Call<PreFactorRespons> call = apiInterface.BasketPreFactor("BasketHistory",shPref.getString("mobile", null),"0","0");
        call.enqueue(new Callback<PreFactorRespons>() {
            @Override
            public void onResponse(Call<PreFactorRespons> call, Response<PreFactorRespons> response) {
                if (response.isSuccessful()) {
                    preFactors = response.body().getPreFactors();
                     adapter = new Prefactor_Adapter( preFactors, BuyhistoryActivity.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(BuyhistoryActivity.this,1);
                    re.setLayoutManager(gridLayoutManager);
                    re.setAdapter(adapter);
                    re.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);



                }

            }

            @Override
            public void onFailure(Call<PreFactorRespons> call, Throwable t) {

                finish();
                Toast.makeText(BuyhistoryActivity.this, "تاریخچه ای یافت نشد", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
