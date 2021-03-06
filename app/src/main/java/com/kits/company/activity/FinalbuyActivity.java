package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kits.company.R;
import com.kits.company.adapter.Good_buy_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.Prefactor_Adapter;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.PreFactor;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FinalbuyActivity extends AppCompatActivity {
    private DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private SharedPreferences shPref;
    ArrayList<PreFactor> preFactors;
    private APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    ProgressBar prog;
    Prefactor_Adapter adapter;
    Intent intent;
    SharedPreferences.Editor sEdit;
    ArrayList<GoodBuy> goodbuys;
    GridLayoutManager gridLayoutManager;
    int id=0;
    String basket_explain="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalbuy);


        InternetConnection ic = new InternetConnection(getApplicationContext());
        if (ic.has()) {
            init();
        } else {
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //**********************************************


    public void init() {

        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);

        final TextView textView1 =findViewById(R.id.finalbuyActivity_1);
        final TextView textView2 =findViewById(R.id.finalbuyActivity_2);
        final TextView textView3 =findViewById(R.id.finalbuyActivity_3);
        final TextView textView4 =findViewById(R.id.finalbuyActivity_4);
        final TextView textView5 =findViewById(R.id.finalbuyActivity_5);
        final EditText editText1 =findViewById(R.id.finalbuyActivity_6);
        Button sendfactor =findViewById(R.id.finalbuyActivity_sendbasket);


        Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
        call2.enqueue(new Callback<GoodBuyRespons>() {
            @Override
            public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    goodbuys = response.body().getGoodsbuy();

                    if(Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("SumFacAmount"))>0) {
                        textView3.setText(Farsi_number.PerisanNumber(goodbuys.get(0).getGoodBuyFieldValue("SumFacAmount")));
                        textView4.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("SumPrice")))));
                        textView2.setText(Farsi_number.PerisanNumber(goodbuys.get(0).getGoodBuyFieldValue("CountGood")));
                    }else{
                        Toast.makeText(FinalbuyActivity.this, "?????? ???????? ???????? ???? ????????", Toast.LENGTH_SHORT).show();
                        finish();;
                    }
                }
            }

            @Override
            public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                Log.e("retrofit_fail",t.getMessage());


            }
        });

        textView1.setText(Farsi_number.PerisanNumber(shPref.getString("fname", null)+"  "+shPref.getString("lname", null)));
        textView5.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("address", null))));



        sendfactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText1.getText().toString().equals(""))
                basket_explain = arabicToenglish(editText1.getText().toString());
                else
                basket_explain="_";
                new android.app.AlertDialog.Builder(FinalbuyActivity.this)
                        .setTitle("????????")
                        .setMessage("?????? ???????????? ?????????? ??????????")
                        .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketToPreFactor",shPref.getString("mobile", null),basket_explain);
                                call2.enqueue(new Callback<GoodBuyRespons>() {
                                    @Override
                                    public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                                        if (response.isSuccessful()) {
                                            assert response.body() != null;
                                            goodbuys = response.body().getGoodsbuy();

                                            if(Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("ErrCode"))>0)
                                            {
                                                Toast toast =Toast.makeText(FinalbuyActivity.this, goodbuys.get(0).getGoodBuyFieldValue("ErrDesc"), Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 10, 10);
                                                toast.show();
                                                finish();
                                                startActivity(getIntent());
                                            }else{
                                                if(Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("PreFactorCode"))>0) {
                                                    if(Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("NotReserved"))>0)
                                                    {
                                                        Toast.makeText(FinalbuyActivity.this, "?????? ???????????? ???? ???????????? ?????? ????", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        intent = new Intent(FinalbuyActivity.this, BuyhistoryDetialActivity.class);
                                                        intent.putExtra("id", goodbuys.get(0).getGoodBuyFieldValue("PreFactorCode"));
                                                        intent.putExtra("ReservedRows", "1");
                                                        startActivity(intent);
                                                    }else {
                                                        Toast.makeText(FinalbuyActivity.this, "?????? ???????????? ???? ???????????? ?????? ????", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }

                                                }else{
                                                    Toast toast =Toast.makeText(FinalbuyActivity.this, "???????? ???????????? ???????? ???????? ?????????? ????????", Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 10, 10);
                                                    toast.show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }

                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                                        Toast.makeText(FinalbuyActivity.this, "????????????", Toast.LENGTH_SHORT).show();

                                        Log.e("retrofit_fail",t.getMessage());
                                    }
                                });
                            }
                        })

                        .setNegativeButton("??????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .show();

            }
        });




    }

    private static String arabicToenglish(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }


}
