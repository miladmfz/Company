package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.Prefactor_Detail_Adapter;
import com.kits.company.application.App;
import com.kits.company.model.PreFactor;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketHistoryDetialActivity extends AppCompatActivity {

    private ArrayList<PreFactor> preFactors;
    private final APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    LottieAnimationView prog;
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

        InternetConnection ic =new  InternetConnection(this);
        if(ic.has()){
            intent();

            try {
                init();
            }catch (Exception e){
                GetShared.ErrorLog(e.getMessage());
            }
        } else{
            intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }
    //*****************************************



    public void intent() {
        Bundle data = getIntent().getExtras();
        code = Integer.parseInt(data.getString("id"));
        ReservedRows = Integer.parseInt(data.getString("ReservedRows"));
    }

    public void init() {

        re = findViewById(R.id.BuyhistorydetailActivity_R1);
        prog = findViewById(R.id.BuyhistorydetailActivity_prog);




        final String[] paths = {"قطعی-غیر قطعی", "قطعی", "غیر قطعی"};
        spinner = findViewById(R.id.BuyhistorydetailActivity_spinner);
        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(App.getContext(),
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

        Call<RetrofitResponse> call = apiInterface.BasketPreFactor(
                "BasketHistory",
                GetShared.ReadString("mobile"),
                code.toString(),
                ReservedRows.toString()
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    preFactors = response.body().getPreFactors();
                    adapter = new Prefactor_Detail_Adapter( preFactors, App.getContext());
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getContext(),1);
                    re.setLayoutManager(gridLayoutManager);
                    re.setAdapter(adapter);
                    re.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                App.showToast("تاریخچه ای یافت نشد");
                finish();
            }
        });
    }

}
