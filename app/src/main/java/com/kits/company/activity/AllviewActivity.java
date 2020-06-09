package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kits.company.R;

import com.kits.company.adapter.Grp_Vlist_detail_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.Company;
import com.kits.company.application.Product;
import com.kits.company.application.ProductAdapter;
import com.kits.company.model.Good;
import com.kits.company.model.GoodGroup;
import com.kits.company.model.GoodGroupRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AllviewActivity extends AppCompatActivity {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    Intent intent;
    ProgressBar prog;
    ArrayList<Company> companies=new ArrayList<>();
    RecyclerView rc;
    Company cm ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allview);


        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //***********************************************************************



    private void init (){

        rc = findViewById(R.id.allview_rc);
        prog = findViewById(R.id.allview_prog);
        rc.setLayoutManager(new LinearLayoutManager(this));

        companies = new ArrayList<>();






        final ArrayList<Product> Product_child2 = new ArrayList<>();




        Call<GoodGroupRespons> call = apiInterface.Getgrp("GoodGroupInfo",0);
        call.enqueue(new Callback<GoodGroupRespons>() {
            @Override
            public void onResponse(Call<GoodGroupRespons> call, Response<GoodGroupRespons> response) {
                if (response.isSuccessful()) {
                    ArrayList<GoodGroup> res = response.body().getGroups();
                    for (final GoodGroup goodGroups_parent : res) {
                        final ArrayList<Product> Product_child = new ArrayList<>();

                        Call<GoodGroupRespons> call5 = apiInterface.Getgrp("GoodGroupInfo", goodGroups_parent.getGroupCode());
                        call5.enqueue(new Callback<GoodGroupRespons>() {
                            @Override
                            public void onResponse(Call<GoodGroupRespons> call, Response<GoodGroupRespons> response) {
                                ArrayList<GoodGroup> res = response.body().getGroups();

                                for (final GoodGroup goodGroups_parent1 : res) {
                                    Product_child.add(new Product(goodGroups_parent1.getName(), goodGroups_parent1.getGroupCode(),goodGroups_parent1.getChildNo()));
                                }
                                cm = new Company(goodGroups_parent.getName(), Product_child,goodGroups_parent.getGroupCode(),goodGroups_parent.getChildNo());
                                companies.add(cm);

                            }

                            @Override
                            public void onFailure(Call<GoodGroupRespons> call, Throwable t) {
                                cm = new Company(goodGroups_parent.getName(), Product_child,goodGroups_parent.getGroupCode(),goodGroups_parent.getChildNo());
                                companies.add(cm);


                            }
                        });

                    }

                }
            }
            @Override
            public void onFailure(Call<GoodGroupRespons> call, Throwable t) {

            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ProductAdapter adapter = new ProductAdapter(companies,AllviewActivity.this);
                rc.setAdapter(adapter);
                prog.setVisibility(View.GONE);
            }
        }, 2000);


    }




}
