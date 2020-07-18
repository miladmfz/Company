package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.kits.company.R;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.Prefactor_Adapter;
import com.kits.company.adapter.Prefactor_Detail_Adapter;
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

public class BuyhistoryDetialActivity extends AppCompatActivity {

    private DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private SharedPreferences shPref;
    private ArrayList<PreFactor> preFactors;
    private APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    ProgressBar prog;
    Integer code=0;
    Integer ReservedRows=2;
    Prefactor_Detail_Adapter adapter;
    Intent intent;
    Spinner spinner;
    RecyclerView re;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyhistory_detial);



        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            intent();

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


    public void intent() {
        Bundle data = getIntent().getExtras();
        code = Integer.parseInt(data.getString("id"));
        ReservedRows = Integer.parseInt(data.getString("ReservedRows"));
    }

    public void init() {

        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        re = findViewById(R.id.BuyhistorydetailActivity_R1);
        prog = findViewById(R.id.BuyhistorydetailActivity_prog);




        final String[] paths = {"قطعی-غیر قطعی", "قطعی", "غیر قطعی"};
        spinner = findViewById(R.id.BuyhistorydetailActivity_spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(BuyhistoryDetialActivity.this,
                android.R.layout.simple_spinner_item,paths);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        if(ReservedRows==2){
            spinner.setSelection(0);
        }else{
            spinner.setSelection(2);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ReservedRows=2;
                        call();
                        break;
                    case 1:
                        ReservedRows=1;
                        call();
                        break;
                    case 2:
                        ReservedRows=0;
                        call();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void call() {
        Call<PreFactorRespons> call = apiInterface.BasketPreFactor("BasketHistory",shPref.getString("mobile", null),code.toString(),ReservedRows.toString());
        call.enqueue(new Callback<PreFactorRespons>() {
            @Override
            public void onResponse(Call<PreFactorRespons> call, Response<PreFactorRespons> response) {
                if (response.isSuccessful()) {
                    preFactors = response.body().getPreFactors();
                    adapter = new Prefactor_Detail_Adapter( preFactors, BuyhistoryDetialActivity.this);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(BuyhistoryDetialActivity.this,1);
                    re.setLayoutManager(gridLayoutManager);
                    re.setAdapter(adapter);
                    re.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<PreFactorRespons> call, Throwable t) {
                Toast.makeText(BuyhistoryDetialActivity.this, "تاریخچه ای یافت نشد", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
