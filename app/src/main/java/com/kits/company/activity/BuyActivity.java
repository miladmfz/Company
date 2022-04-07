package com.kits.company.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.Good_buy_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyActivity extends AppCompatActivity {

    private final DecimalFormat decimalFormat = new DecimalFormat("0,000");
    RecyclerView re;
    ArrayList<Good> Goods;
    private final APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    TextView Buy_row,Buy_price,Buy_amount;
    ProgressBar prog;
    GridLayoutManager gridLayoutManager;
    int id=0;
    Intent intent;
    ArrayList<Good> Goods_sum;
    public ArrayList<String> Goods_shortage=new ArrayList<>();
    Good_buy_Adapter adapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);


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


    public void init() {

        Buy_row = findViewById(R.id.BuyActivity_total_row_buy);
        Buy_price = findViewById(R.id.BuyActivity_total_price_buy);
        Buy_amount = findViewById(R.id.BuyActivity_total_amount_buy);
        Button total_delete = findViewById(R.id.BuyActivity_total_delete);
        Button final_buy_test = findViewById(R.id.BuyActivity_test);
        re = findViewById(R.id.BuyActivity_R1);
        prog = findViewById(R.id.BuyActivity_prog);

        Toolbar toolbar = findViewById(R.id.BuyActivity_toolbar);
        setSupportActionBar(toolbar);


        Call<RetrofitResponse> call = apiInterface.Getbasket(
                "BasketGet",
                GetShared.ReadString("mobile")
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call,@NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Goods = response.body().getGoods();
                    if ( Goods.size()< 1) {

                        App.showToast("کالایی در سبد خرید موجود نمی باشد");
                        finish();
                    }
                    Log.e("123",GetShared.ReadString("AppBasketItem"));

                    adapter = new Good_buy_Adapter( Goods, BuyActivity.this);
                    gridLayoutManager = new GridLayoutManager(App.getContext(),1);
                    gridLayoutManager.scrollToPosition(Integer.parseInt(GetShared.ReadString("basket_position")));
                    re.setLayoutManager(gridLayoutManager);
                    re.setAdapter(adapter);
                    re.setItemAnimator(new FadeInUpAnimator());
                    prog.setVisibility(View.GONE);
                    shortage();

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());
            }
        });




        Call<RetrofitResponse> call2 = apiInterface.GetbasketSum(
                "BasketSum",
                GetShared.ReadString("mobile")
        );
        call2.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call,@NotNull  Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                     Goods_sum = response.body().getGoods();

                    if(Integer.parseInt(Goods_sum.get(0).getGoodFieldValue("SumFacAmount"))>0) {
                        Buy_amount.setText(NumberFunctions.PerisanNumber(Goods_sum.get(0).getGoodFieldValue("SumFacAmount")));
                        Buy_price.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(Goods_sum.get(0).getGoodFieldValue("SumPrice")))));
                        Buy_row.setText(NumberFunctions.PerisanNumber(Goods_sum.get(0).getGoodFieldValue("CountGood")));
                    }else{
                        App.showToast("سبد خرید خالی می باشد");
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call,@NotNull Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());
            }
        });




        re.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    id = gridLayoutManager.findFirstVisibleItemPosition();
                    GetShared.EditString("basket_position", String.valueOf(id));
                }
                if (dy < 0) { //check for scroll down
                    id = gridLayoutManager.findFirstVisibleItemPosition();
                    GetShared.EditString("basket_position", String.valueOf(id));
                }
            }
        });




        final_buy_test.setOnClickListener(view -> {
            intent = new Intent(this, FinalbuyActivity.class);
            finish();
            startActivity(intent);
        });


        total_delete.setOnClickListener(view -> new AlertDialog.Builder(this)
                .setTitle("توجه")
                .setMessage("آیا مایل به خالی کردن سبد خرید می باشید؟")
                .setPositiveButton("بله", (dialogInterface, i) -> {

                    Call<RetrofitResponse> call1 = apiInterface.Basketdeleteall(
                            "Basketdeleteall",
                            GetShared.ReadString("mobile")
                    );
                    call1.enqueue(new Callback<RetrofitResponse>() {
                        @Override
                        public void onResponse(@NotNull  Call<RetrofitResponse> call1, @NotNull  Response<RetrofitResponse> response) {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                if (response.body().getText().equals("done")) {
                                    App.showToast("سبد خرید حذف گردید");
                                    finish();
                                }
                            }
                        }
                        @Override
                        public void onFailure(@NotNull Call<RetrofitResponse> call1,@NotNull  Throwable t) {
                            Log.e("retrofit_fail",t.getMessage());
                        }
                    });
                })
                .setNegativeButton("خیر", (dialogInterface, i) -> {
                })
                .show());


    }
    public void shortage(){


        for(Good gb :Goods){
            if (gb.getGoodFieldValue("IsReserved").equals("1")) {
                if(Integer.parseInt(gb.getGoodFieldValue("NotReserved"))>0){
                    Goods_shortage.add("••"+gb.getGoodFieldValue("GoodName")+" موجود نمی باشد لطفا از سبد حذف کرده تا سفارش شما ثبت گردد");
                }
            }
        }

        ArrayAdapter adapter_lv = new ArrayAdapter<>(
                this,
                R.layout.listview_shortage,
                Goods_shortage);
        ListView listView = findViewById(R.id.BuyActivity_Listview);
        listView.setAdapter(adapter_lv);

    }
    public void buyrefresh(int position,String amount){

        Goods.get(position).setFacAmount(amount);
        adapter.notifyDataSetChanged();

        Call<RetrofitResponse> call2 = apiInterface.GetbasketSum("BasketSum",GetShared.ReadString("mobile"));
        call2.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call,@NotNull  Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                     Goods_sum= response.body().getGoods();

                    if(Integer.parseInt(Goods_sum.get(0).getGoodFieldValue("SumFacAmount"))>0) {
                        Buy_amount.setText(NumberFunctions.PerisanNumber(Goods_sum.get(0).getGoodFieldValue("SumFacAmount")));
                        Buy_price.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(Goods_sum.get(0).getGoodFieldValue("SumPrice")))));
                        Buy_row.setText(NumberFunctions.PerisanNumber(Goods_sum.get(0).getGoodFieldValue("CountGood")));
                    }else{
                        App.showToast("سبد خرید خالی می باشد");
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call,@NotNull  Throwable t) {
                Log.e("retrofit_fail",t.getMessage());
            }
        });
    }



}
