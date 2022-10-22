package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.application.GroupLayerAdapter;
import com.kits.company.model.GoodGroup;
import com.kits.company.model.GroupLayerOne;
import com.kits.company.model.GroupLayerTwo;
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
    LottieAnimationView prog;
    ArrayList<GroupLayerOne> companies=new ArrayList<>();
    RecyclerView rc;
    GroupLayerOne cm ;
int counter=0;

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

        Call<RetrofitResponse> call1 = apiInterface.info("kowsar_info", "AppBroker_DefaultGroupCode");
        call1.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    parent(response.body().getText());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
            }
        });



    }

    private void callrec (){
        GroupLayerAdapter adapter = new GroupLayerAdapter(companies, App.getContext());
        rc.setAdapter(adapter);
        prog.setVisibility(View.GONE);
    }

    private void parent (String GoodGroupCode){

        Call<RetrofitResponse> call = apiInterface.Getgrp("GoodGroupInfo",GoodGroupCode);
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<GoodGroup> res=response.body().getGroups();
                    child(response.body().getGroups());
                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                GetShared.ErrorLog(t.getMessage());
            }
        });
    }

    private void child (ArrayList<GoodGroup> GoodGroups){

        ArrayList<GroupLayerTwo> Product_child = new ArrayList<>();
        Call<RetrofitResponse> call5 = apiInterface.Getgrp(
                "GoodGroupInfo",
                GoodGroups.get(counter).getGoodGroupFieldValue("groupcode"));
        call5.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                assert response.body() != null;
                ArrayList<GoodGroup> res = response.body().getGroups();
                for (GoodGroup goodGroups_parent1 : res) {
                    Product_child.add(new GroupLayerTwo(
                            goodGroups_parent1.getGoodGroupFieldValue("Name"),
                            Integer.parseInt(goodGroups_parent1.getGoodGroupFieldValue("groupcode")),
                            Integer.parseInt(goodGroups_parent1.getGoodGroupFieldValue("ChildNo"))));
                }
                cm = new GroupLayerOne(
                        GoodGroups.get(counter).getGoodGroupFieldValue("Name"),
                        Product_child,
                        Integer.parseInt(GoodGroups.get(counter).getGoodGroupFieldValue("groupcode")),
                        Integer.parseInt(GoodGroups.get(counter).getGoodGroupFieldValue("ChildNo")));
                companies.add(cm);
                counter++;

                if (counter>(GoodGroups.size()-1)){
                    callrec();
                }else {
                    child(GoodGroups);
                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                cm = new GroupLayerOne(
                        GoodGroups.get(counter).getGoodGroupFieldValue("Name"),
                        Product_child,
                        Integer.parseInt(GoodGroups.get(counter).getGoodGroupFieldValue("groupcode")),
                        Integer.parseInt(GoodGroups.get(counter).getGoodGroupFieldValue("ChildNo")));
                companies.add(cm);
                counter++;
                if (counter>(GoodGroups.size()-1)){
                    callrec();

                }else {
                    child(GoodGroups);
                }
            }
        });



    }

}

