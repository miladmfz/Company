package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteActivity extends AppCompatActivity {
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    Good_ProSearch_Adapter adapter;
    Intent intent;
    RecyclerView rc_good;
    ArrayList<Good> goods;
    TextView textCartItemCount;
    ArrayList<Good> Goods;
    ProgressBar prog;
    GridLayoutManager gridLayoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount,PageNo=0;
    Call<RetrofitResponse> call;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

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


    public void init() {

        toolbar =  findViewById(R.id.favoriteactivity_toolbar);

        prog = findViewById(R.id.favoriteactivity_prog);
        rc_good =  findViewById(R.id.favoriteactivity_recycler);

        setSupportActionBar(toolbar);

        final SwitchMaterial mySwitch_activestack = findViewById(R.id.favoriteactivity_switch);
        mySwitch_activestack.setChecked(GetShared.ReadBoolan("available_good"));
        mySwitch_activestack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetShared.EditBoolan("available_good",!GetShared.ReadBoolan("available_good"));
                adapter.notifyDataSetChanged();
                gridLayoutManager = new GridLayoutManager(App.getContext(),2);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter);

            }
        });

        allgood();


        rc_good.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount =   gridLayoutManager.getChildCount();
                    totalItemCount =   gridLayoutManager.getItemCount();
                    pastVisiblesItems =   gridLayoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-2) {
                            loading = false;
                            PageNo++;
                            allgood_more();
                        }
                    }
                }
            }
        });


    }

    private void allgood() {

        call = apiInterface.GetAllGood
                ("goodinfo",
                        "0",
                        "" ,
                        "",
                        "0",
                        String.valueOf(PageNo),
                        GetShared.ReadString("mobile"),
                        "1");
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    adapter = new Good_ProSearch_Adapter( goods, App.getContext());
                    gridLayoutManager = new GridLayoutManager(App.getContext(),2);
                    rc_good.setLayoutManager(gridLayoutManager);
                    rc_good.setAdapter(adapter);
                    rc_good.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                App.showToast("کالایی در لیست وجود ندارد");
                finish();
            }
        });

    }


    private void allgood_more() {
        prog.setVisibility(View.VISIBLE);
         call = apiInterface.GetAllGood
                ("goodinfo",
                        "0",
                        "",
                        "",
                        "0",
                        String.valueOf(PageNo),
                        GetShared.ReadString("mobile"),
                        "1");
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> good_page = response.body().getGoods();
                    goods.addAll(good_page);
                    adapter = new Good_ProSearch_Adapter( goods, App.getContext());
                    gridLayoutManager = new GridLayoutManager(App.getContext(),2);
                    gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                    rc_good.setLayoutManager(gridLayoutManager);
                    rc_good.setAdapter(adapter);
                    rc_good.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

                prog.setVisibility(View.GONE);
                Log.e("retrofit_fail",t.getMessage());

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.basket_menu);
        View actionView = menuItem.getActionView();
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.basket_menu) {
            intent = new Intent(this, BuyActivity.class);
            GetShared.EditString("basket_position", "0");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupBadge() {

        if (textCartItemCount != null) {

            if (textCartItemCount.getVisibility() != View.GONE) {
                textCartItemCount.setVisibility(View.GONE);
            }
            Call<RetrofitResponse> call2 = apiInterface.GetbasketSum(
                    "BasketSum",
                    GetShared.ReadString("mobile")
            );
            call2.enqueue(new Callback<RetrofitResponse>() {
                @Override
                public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Goods = response.body().getGoods();
                        textCartItemCount.setText(NumberFunctions.PerisanNumber(Goods.get(0).getGoodFieldValue("SumFacAmount")));
                        if(Integer.parseInt(Goods.get(0).getGoodFieldValue("SumFacAmount"))>0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());

                }
            });
        }
    }



}
