package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.application.Category;
import com.kits.company.application.Product;
import com.kits.company.application.ProductAdapter;
import com.kits.company.model.GoodGroup;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllviewActivity extends AppCompatActivity {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    Intent intent;
    ProgressBar prog;
    ArrayList<Category> companies=new ArrayList<>();
    RecyclerView rc;
    Category cm ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allview);

        InternetConnection ic =new  InternetConnection(this);
        if(ic.has()){
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

    //***********************************************************************



    private void init (){

        rc = findViewById(R.id.allview_rc);
        prog = findViewById(R.id.allview_prog);
        rc.setLayoutManager(new LinearLayoutManager(this));

        companies = new ArrayList<>();


        Call<RetrofitResponse> call = apiInterface.Getgrp("GoodGroupInfo","0");
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<GoodGroup> res = response.body().getGroups();
                    for (GoodGroup goodGroups_parent : res) {
                        ArrayList<Product> Product_child = new ArrayList<>();
                        Call<RetrofitResponse> call5 = apiInterface.Getgrp(
                                "GoodGroupInfo",
                                goodGroups_parent.getGoodGroupFieldValue("groupcode"));
                        call5.enqueue(new Callback<RetrofitResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                assert response.body() != null;
                                ArrayList<GoodGroup> res = response.body().getGroups();
                                for (GoodGroup goodGroups_parent1 : res) {
                                    Product_child.add(new Product(
                                            goodGroups_parent1.getGoodGroupFieldValue("Name"),
                                            Integer.parseInt(goodGroups_parent1.getGoodGroupFieldValue("groupcode")),
                                            Integer.parseInt(goodGroups_parent1.getGoodGroupFieldValue("ChildNo"))));
                                }
                                cm = new Category(
                                        goodGroups_parent.getGoodGroupFieldValue("Name"),
                                        Product_child,
                                        Integer.parseInt(goodGroups_parent.getGoodGroupFieldValue("groupcode")),
                                        Integer.parseInt(goodGroups_parent.getGoodGroupFieldValue("ChildNo")));
                                companies.add(cm);
                            }
                            @Override
                            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                                cm = new Category(
                                        goodGroups_parent.getGoodGroupFieldValue("Name"),
                                        Product_child,
                                        Integer.parseInt(goodGroups_parent.getGoodGroupFieldValue("groupcode")),
                                        Integer.parseInt(goodGroups_parent.getGoodGroupFieldValue("ChildNo")));
                                companies.add(cm);
                            }
                        });

                    }

                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                GetShared.ErrorLog(t.getMessage());
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            ProductAdapter adapter = new ProductAdapter(companies, App.getContext());
            rc.setAdapter(adapter);
            prog.setVisibility(View.GONE);
        }, 2000);

    }



}
