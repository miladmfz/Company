package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kits.company.R;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FavoriteActivity extends AppCompatActivity {
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    Good_ProSearch_Adapter adapter;
    Intent intent;
    private SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
    RecyclerView rc_good;
    ArrayList<Good> goods;
    TextView textCartItemCount;
    ArrayList<GoodBuy> goodbuys;
    ProgressBar prog;
    GridLayoutManager gridLayoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount,PageNo=0;
    Call<GoodRespons> call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }


    public void init() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);

        prog = findViewById(R.id.favoriteactivity_prog);
        rc_good =  findViewById(R.id.favoriteactivity_recycler);


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
                            Log.e("rc_...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
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
                ("goodinfo", "" , "",0,PageNo,shPref.getString("mobile", null),1);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    adapter = new Good_ProSearch_Adapter( goods, FavoriteActivity.this);
                    gridLayoutManager = new GridLayoutManager(FavoriteActivity.this,2);
                    rc_good.setLayoutManager(gridLayoutManager);
                    rc_good.setAdapter(adapter);
                    rc_good.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {

                finish();
                Toast.makeText(FavoriteActivity.this, "کالایی در لیست وجود ندارد", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void allgood_more() {
        prog.setVisibility(View.VISIBLE);
         call = apiInterface.GetAllGood
                ("goodinfo", "", "",0,PageNo,shPref.getString("mobile", null),1);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> good_page = response.body().getGoods();
                    goods.addAll(good_page);
                    adapter = new Good_ProSearch_Adapter( goods, FavoriteActivity.this);
                    gridLayoutManager = new GridLayoutManager(FavoriteActivity.this,2);
                    gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                    rc_good.setLayoutManager(gridLayoutManager);
                    rc_good.setAdapter(adapter);
                    rc_good.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {

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
            intent = new Intent(FavoriteActivity.this, BuyActivity.class);
            sEdit = shPref.edit();
            sEdit.putString("basket_position", "0");
            sEdit.apply();
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
            Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
            call2.enqueue(new Callback<GoodBuyRespons>() {
                @Override
                public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        goodbuys = response.body().getGoodsbuy();

                        textCartItemCount.setText(Farsi_number.PerisanNumber(goodbuys.get(0).getSumFacAmount()));
                        if(Integer.parseInt(goodbuys.get(0).getSumFacAmount())>0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());


                    Log.e("retrofit_fail",t.getMessage());

                }
            });
        }
    }



    @Override
    public void onResume(){
        super.onResume();
        setupBadge();
    }



}
