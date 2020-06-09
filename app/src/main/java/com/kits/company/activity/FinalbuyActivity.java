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
        EditText editText1 =findViewById(R.id.finalbuyActivity_6);
        Button sendfactor =findViewById(R.id.finalbuyActivity_sendbasket);


        Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
        call2.enqueue(new Callback<GoodBuyRespons>() {
            @Override
            public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    goodbuys = response.body().getGoodsbuy();

                    if(Integer.parseInt(goodbuys.get(0).getSumFacAmount())>0) {
                        textView3.setText(Farsi_number.PerisanNumber(goodbuys.get(0).getSumFacAmount()));
                        textView4.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuys.get(0).getSumPrice()))));
                        textView2.setText(Farsi_number.PerisanNumber(goodbuys.get(0).getCountGood()));
                    }else{
                        Toast.makeText(FinalbuyActivity.this, "سبد خرید خالی می باشد", Toast.LENGTH_SHORT).show();
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
        textView5.setText(Farsi_number.PerisanNumber(shPref.getString("address", null)));

        editText1.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(final Editable editable) {
                        basket_explain = arabicToenglish(editable.toString());

                    }
                });

        sendfactor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new android.app.AlertDialog.Builder(FinalbuyActivity.this)
                        .setTitle("توجه")
                        .setMessage("آیا فاکتور ارسال گردد؟")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketToPreFactor",shPref.getString("mobile", null),basket_explain);
                                call2.enqueue(new Callback<GoodBuyRespons>() {
                                    @Override
                                    public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                                        if (response.isSuccessful()) {
                                            assert response.body() != null;
                                            goodbuys = response.body().getGoodsbuy();

                                            if(goodbuys.get(0).getPreFactorCode()>0) {
                                                Toast.makeText(FinalbuyActivity.this, "پیش فاکتور با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else{
                                                Toast toast =Toast.makeText(FinalbuyActivity.this, "لطفا مقادیر کالا هارا بررسی کنید", Toast.LENGTH_SHORT);
                                                toast.setGravity(Gravity.CENTER, 10, 10);
                                                toast.show();
                                                finish();
                                                startActivity(getIntent());
                                            }
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                                        Toast.makeText(FinalbuyActivity.this, "لصصصصد", Toast.LENGTH_SHORT).show();

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
