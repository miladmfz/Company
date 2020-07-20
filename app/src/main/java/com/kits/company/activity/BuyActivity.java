package com.kits.company.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kits.company.R;
import com.kits.company.adapter.Good_buy_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity {

    private DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
    RecyclerView re;
    ArrayList<GoodBuy> goodbuys;
    private APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    TextView Buy_row,Buy_price,Buy_amount;
    ProgressBar prog;
    GridLayoutManager gridLayoutManager;
    int id=0;
    Intent intent;
    ArrayList<GoodBuy> goodbuys_sum;
    Good_buy_Adapter adapter;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);



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


    public void init() {

        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        Buy_row = findViewById(R.id.BuyActivity_total_row_buy);
        Buy_price = findViewById(R.id.BuyActivity_total_price_buy);
        Buy_amount = findViewById(R.id.BuyActivity_total_amount_buy);
        Button total_delete = findViewById(R.id.BuyActivity_total_delete);
        Button final_buy_test = findViewById(R.id.BuyActivity_test);
        re = findViewById(R.id.BuyActivity_R1);
        prog = findViewById(R.id.BuyActivity_prog);

        Toolbar toolbar = findViewById(R.id.BuyActivity_toolbar);
        setSupportActionBar(toolbar);




        Call<GoodBuyRespons> call = apiInterface.Getbasket("BasketGet",shPref.getString("mobile", null));
        call.enqueue(new Callback<GoodBuyRespons>() {
            @Override
            public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                if (response.isSuccessful()) {
                    goodbuys = response.body().getGoodsbuy();
                    if ( goodbuys.size()< 1) {
                        Toast.makeText(BuyActivity.this, "کالایی در سبد خرید موجود نمی باشد", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    adapter = new Good_buy_Adapter( goodbuys, BuyActivity.this);
                    gridLayoutManager = new GridLayoutManager(BuyActivity.this,1);
                    gridLayoutManager.scrollToPosition(Integer.parseInt(shPref.getString("basket_position", null)));
                    re.setLayoutManager(gridLayoutManager);
                    re.setAdapter(adapter);
                    re.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<GoodBuyRespons> call, Throwable t) {

                    Log.e("retrofit_fail",t.getMessage());


            }
        });




        Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
        call2.enqueue(new Callback<GoodBuyRespons>() {
            @Override
            public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                     goodbuys_sum = response.body().getGoodsbuy();

                    if(Integer.parseInt(goodbuys_sum.get(0).getGoodBuyFieldValue("SumFacAmount"))>0) {
                        Buy_amount.setText(Farsi_number.PerisanNumber(goodbuys_sum.get(0).getGoodBuyFieldValue("SumFacAmount")));
                        Buy_price.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuys_sum.get(0).getGoodBuyFieldValue("SumPrice")))));
                        Buy_row.setText(Farsi_number.PerisanNumber(goodbuys_sum.get(0).getGoodBuyFieldValue("CountGood")));
                    }else{
                        Toast.makeText(BuyActivity.this, "سبد خرید خالی می باشد", Toast.LENGTH_SHORT).show();
                        finish();;
                    }
                }
            }

            @Override
            public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());


            }
        });




        re.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down

                    id =   gridLayoutManager.findFirstVisibleItemPosition();
                    sEdit = shPref.edit();
                    sEdit.putString("basket_position", String.valueOf(id));
                    sEdit.apply();
                }

                if (dy < 0) { //check for scroll down

                    id =   gridLayoutManager.findFirstVisibleItemPosition();
                    sEdit = shPref.edit();
                    sEdit.putString("basket_position", String.valueOf(id));
                    sEdit.apply();
                }
            }
        });




        final_buy_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(getApplicationContext(), FinalbuyActivity.class);
                finish();
                startActivity(intent);
            }
        });


        total_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(BuyActivity.this)
                        .setTitle("توجه")
                        .setMessage("آیا مایل به خالی کردن سبد خرید می باشید؟")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<String> call = apiInterface.Basketdeleteall("Basketdeleteall",shPref.getString("mobile", null));
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        if (response.isSuccessful()) {
                                            if (response.body().equals("done")) {
                                                Toast.makeText(BuyActivity.this, "سبد خرید حذف گردید", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        Log.e("retrofit_fail",t.getMessage());
                                    }
                                });
                            }
                        })
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();
            }
        });


    }

    public void buyrefresh(int position,int amount){

        goodbuys.get(position).setFacAmount(amount);
        adapter.notifyDataSetChanged();

        Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
        call2.enqueue(new Callback<GoodBuyRespons>() {
            @Override
            public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                     goodbuys_sum= response.body().getGoodsbuy();

                    if(Integer.parseInt(goodbuys_sum.get(0).getGoodBuyFieldValue("SumFacAmount"))>0) {
                        Buy_amount.setText(Farsi_number.PerisanNumber(goodbuys_sum.get(0).getGoodBuyFieldValue("SumFacAmount")));
                        Buy_price.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuys_sum.get(0).getGoodBuyFieldValue("SumPrice")))));
                        Buy_row.setText(Farsi_number.PerisanNumber(goodbuys_sum.get(0).getGoodBuyFieldValue("CountGood")));
                    }else{
                        Toast.makeText(BuyActivity.this, "سبد خرید خالی می باشد", Toast.LENGTH_SHORT).show();
                        finish();;
                    }
                }
            }

            @Override
            public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                Log.e("retrofit_fail",t.getMessage());


            }
        });


    }



}
