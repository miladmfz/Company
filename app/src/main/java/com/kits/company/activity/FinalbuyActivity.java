package com.kits.company.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinalbuyActivity extends AppCompatActivity {

    private final DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private final APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    Intent intent;
    ArrayList<Good> Goods;

    String basket_explain="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalbuy);


        InternetConnection ic = new InternetConnection(this);
        if (ic.has()) {
            try {
                init();
            }catch (Exception e){
                GetShared.ErrorLog(e.getMessage());
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //**********************************************


    public void init() {


        final TextView textView1 =findViewById(R.id.finalbuyActivity_1);
        final TextView textView2 =findViewById(R.id.finalbuyActivity_2);
        final TextView textView3 =findViewById(R.id.finalbuyActivity_3);
        final TextView textView4 =findViewById(R.id.finalbuyActivity_4);
        final TextView textView5 =findViewById(R.id.finalbuyActivity_5);
        final EditText editText1 =findViewById(R.id.finalbuyActivity_6);
        Button sendfactor =findViewById(R.id.finalbuyActivity_sendbasket);


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

                    if(Integer.parseInt(Goods.get(0).getGoodFieldValue("SumFacAmount"))>0) {
                        textView3.setText(NumberFunctions.PerisanNumber(Goods.get(0).getGoodFieldValue("SumFacAmount")));
                        textView4.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(Goods.get(0).getGoodFieldValue("SumPrice")))));
                        textView2.setText(NumberFunctions.PerisanNumber(Goods.get(0).getGoodFieldValue("CountGood")));
                    }else{
                        App.showToast("سبد خرید خالی می باشد");
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

            }
        });

        textView1.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("fname")+"  "+GetShared.ReadString("lname")));
        textView5.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("address")));



        sendfactor.setOnClickListener(v -> {
            if(!editText1.getText().toString().equals(""))
            basket_explain = NumberFunctions.EnglishNumber(editText1.getText().toString());
            else
            basket_explain="_";

            new android.app.AlertDialog.Builder(this)
                    .setTitle("توجه")
                    .setMessage("آیا فاکتور ارسال گردد؟")
                    .setPositiveButton("بله", (dialogInterface, i) -> {
                        Call<RetrofitResponse> call21 = apiInterface.GetbasketSum(
                                "BasketToPreFactor",
                                GetShared.ReadString("mobile"),
                                basket_explain);
                        call21.enqueue(new Callback<RetrofitResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                if (response.isSuccessful()) {
                                    assert response.body() != null;
                                    Goods = response.body().getGoods();

                                    if(Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode"))>0)
                                    {
                                        App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                                        finish();
                                        intent = new Intent(FinalbuyActivity.this, BasketActivity.class);
                                        GetShared.EditString("basket_position", "0");
                                        startActivity(intent);
                                    }else{
                                        if(Integer.parseInt(Goods.get(0).getGoodFieldValue("PreFactorCode"))>0) {
                                            if(Integer.parseInt(Goods.get(0).getGoodFieldValue("NotReserved"))>0)
                                            {
                                                App.showToast("پیش فاکتور با موفقیت ثبت شد");
                                                finish();
                                                intent = new Intent(FinalbuyActivity.this, BasketHistoryDetialActivity.class);
                                                intent.putExtra("id", Goods.get(0).getGoodFieldValue("PreFactorCode"));
                                                intent.putExtra("ReservedRows", "1");
                                                startActivity(intent);
                                            }else {
                                                App.showToast("پیش فاکتور با موفقیت ثبت شد");
                                                finish();
                                            }

                                        }else{
                                            App.showToast("لطفا مقادیر کالا هارا بررسی کنید");
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    }

                                }
                            }
                            @Override
                            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                            }
                        });
                    })

                    .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    })
                    .show();

        });




    }




}
