package com.kits.company.adapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.kits.company.R;
import com.kits.company.activity.BuyActivity;
import com.kits.company.activity.DetailActivity;
import com.kits.company.activity.FavoriteActivity;
import com.kits.company.activity.FinalbuyActivity;
import com.kits.company.activity.GrpActivity;
import com.kits.company.activity.MainActivity;
import com.kits.company.activity.SearchActivity;
import com.kits.company.activity.Search_date_detailActivity;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Buy_box {

    private final Context mContext;
    DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private Integer code ;
    private APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    private SharedPreferences shPref;
    int last_amount=0;
    ArrayList<GoodBuy> goodbuys;

    public Buy_box(Context mContext)
    {
        this.mContext = mContext;
        shPref = mContext.getSharedPreferences("profile", Context.MODE_PRIVATE);


    }



    public void buydialog( String goodname, String sellprice, int goodcode) {


        code=goodcode;
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
        dialog.setContentView(R.layout.box_buy);
        Button boxbuy = dialog.findViewById(R.id.box_buy_btn);

        final TextView tv_goodname = dialog.findViewById(R.id.box_buy_name);
        final EditText amount = dialog.findViewById(R.id.box_buy_amount);
        final TextView price = dialog.findViewById(R.id.box_buy_price);
        final TextView sumprice = dialog.findViewById(R.id.box_buy_sumprice);


        tv_goodname.setText(goodname);
        price.setText(Farsi_number.PerisanNumber(sellprice));
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo","","goodcode="+goodcode,0,0,shPref.getString("mobile", null),0);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> goods = response.body().getGoods();
                    Good good= goods.get(0);
                    amount.setHint(""+good.getBasketAmount());
                    last_amount=last_amount+good.getBasketAmount();
                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {

                Log.e("retrofit_fail",t.getMessage());


            }
        });


        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try
                {
                    Integer iPrice = Integer.parseInt(price.getText().toString());
                    Integer iAmount = Integer.parseInt(arabicToenglish(amount.getText().toString()));
                    sumprice.setText(Farsi_number.PerisanNumber(""+iPrice*iAmount));
                } catch (Exception e) {
                    sumprice.setText("");
                }
            }
        });



        dialog.show();
        amount.requestFocus();
        amount.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

        boxbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String amo =amount.getText().toString();
                 String pr = price.getText().toString();

                 if(!amo.equals("")) {
                     if(Integer.parseInt(amo)>0) {
                         Call<GoodBuyRespons> call = apiInterface.InsertBasket("Insertbasket", "DeviceCode", code, Integer.parseInt(amo)+last_amount, Integer.parseInt(pr), "test", shPref.getString("mobile", null));
                         call.enqueue(new Callback<GoodBuyRespons>() {
                             @Override
                             public void onResponse(Call<GoodBuyRespons> call, retrofit2.Response<GoodBuyRespons> response) {
                                 Log.e("onResponse", "" + response.body());
                                 assert response.body() != null;

                                 goodbuys = response.body().getGoodsbuy();

                                 if (goodbuys.get(0).getErrCode() > 0){
                                     Toast.makeText(mContext, goodbuys.get(0).getErrDesc(), Toast.LENGTH_SHORT).show();

                                 }else{
                                         Toast toast = Toast.makeText(mContext, "کالای مورد نظر به سبد خرید اضافه شد", Toast.LENGTH_SHORT);
                                         if(mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")){
                                             SearchActivity activity= (SearchActivity) mContext;
                                             activity.setupBadge();
                                         }

                                         if(mContext.getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")){
                                             Search_date_detailActivity activity= (Search_date_detailActivity) mContext;
                                             activity.setupBadge();
                                         }

                                         if(mContext.getClass().getName().equals("com.kits.company.activity.GrpActivity")){
                                             GrpActivity activity= (GrpActivity) mContext;
                                             activity.setupBadge();
                                         }

                                         if(mContext.getClass().getName().equals("com.kits.company.activity.MainActivity")){
                                             MainActivity activity= (MainActivity) mContext;
                                             activity.setupBadge();
                                         }

                                         if(mContext.getClass().getName().equals("com.kits.company.activity.FavoriteActivity")){
                                             FavoriteActivity activity= (FavoriteActivity) mContext;
                                             activity.setupBadge();
                                         }

                                         if(mContext.getClass().getName().equals("com.kits.company.activity.DetailActivity")){
                                             DetailActivity activity= (DetailActivity) mContext;
                                             activity.setupBadge();
                                         }

                                         toast.setGravity(Gravity.CENTER, 10, 10);
                                         toast.show();
                                         dialog.dismiss();

                                 }
                             }

                             @Override
                             public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                                 Log.e("onFailure", "" + t.toString());
                             }
                         });
                     }else {
                         Toast.makeText(mContext, "لطفا تعداد صحیح را وارد کنید", Toast.LENGTH_SHORT).show();

                     }
                 }else {
                     Toast.makeText(mContext, "لطفا تعداد مورد نظر را وارد کنید", Toast.LENGTH_SHORT).show();
                 }

            }
        });
    }


    public void deletegoodfrombasket (int goodcode) {


                     Call<String> call = apiInterface.Basketdelete("deletebasket", "DeviceCode", goodcode, shPref.getString("mobile", null));
                     call.enqueue(new Callback<String>() {
                         @Override
                         public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                             Log.e("onResponse", "" + response.body());
                             assert response.body() != null;
                             if (response.body().equals("done")) {
                                 Log.e("onResponse2", "" + response.body());
                                 Toast toast = Toast.makeText(mContext, "کالای مورد نظر حذف گردید", Toast.LENGTH_SHORT);
                                 toast.setGravity(Gravity.CENTER, 10, 10);
                                 toast.show();
                             }
                         }

                         @Override
                         public void onFailure(Call<String> call, Throwable t) {
                             Log.e("onFailure", "" + t.toString());
                         }
                     });

    }



    public void basketdialog(String goodname, String sellprice, int goodcode, final int facamount,final int position) {

        code=goodcode;
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
        dialog.setContentView(R.layout.box_buy);
        Button boxbuy = dialog.findViewById(R.id.box_buy_btn);

        final TextView tv_goodname = dialog.findViewById(R.id.box_buy_name);
        final EditText amount = dialog.findViewById(R.id.box_buy_amount);
        final TextView price = dialog.findViewById(R.id.box_buy_price);
        final TextView sumprice = dialog.findViewById(R.id.box_buy_sumprice);


        tv_goodname.setText(Farsi_number.PerisanNumber(goodname));
        price.setText(Farsi_number.PerisanNumber(sellprice));
        amount.setHint(Farsi_number.PerisanNumber(String.valueOf(facamount)));

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try
                {
                    Integer iPrice = Integer.parseInt(price.getText().toString());
                    Integer iAmount = Integer.parseInt(arabicToenglish(amount.getText().toString()));
                    sumprice.setText(Farsi_number.PerisanNumber(""+iPrice*iAmount));
                } catch (Exception e) {
                    sumprice.setText("");
                }
            }
        });



        dialog.show();
        amount.requestFocus();
        amount.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

        boxbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 final String amo =amount.getText().toString();
                 String pr = price.getText().toString();
                if(!amo.equals("")) {
                    if(Integer.parseInt(amo)>0) {

                        Call<GoodBuyRespons> call = apiInterface.InsertBasket("Insertbasket", "DeviceCode", code, Integer.parseInt(amo)+facamount, Integer.parseInt(pr), "test", shPref.getString("mobile", null));
                        call.enqueue(new Callback<GoodBuyRespons>() {
                            @Override
                            public void onResponse(Call<GoodBuyRespons> call, retrofit2.Response<GoodBuyRespons> response) {
                                Log.e("onResponse", "" + response.body());
                                assert response.body() != null;

                                goodbuys = response.body().getGoodsbuy();

                                if (goodbuys.get(0).getErrCode() > 0){
                                    Toast.makeText(mContext, goodbuys.get(0).getErrDesc(), Toast.LENGTH_SHORT).show();
                                }else{
                                    BuyActivity activity = (BuyActivity) mContext;
                                    activity.buyrefresh(position, Integer.parseInt(amo));
                                    dialog.dismiss();
                                }

                            }
                            @Override
                            public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                                Log.e("onFailure", "" + t.toString());
                            }
                        });
                    }else {
                        Toast.makeText(mContext, "لطفا تعداد صحیح را وارد کنید", Toast.LENGTH_SHORT).show();

                    }
                }else {

                    Toast.makeText(mContext, "لطفا تعداد مورد نظر را وارد کنید", Toast.LENGTH_SHORT).show();
            }


        }
        });
    }


    public void basketdsolo( String sellprice ,int goodcode, final int facamount,final int position) {

        Call<GoodBuyRespons> call = apiInterface.InsertBasket("Insertbasket", "DeviceCode", goodcode,facamount, Integer.valueOf(sellprice), "test", shPref.getString("mobile", null));
        call.enqueue(new Callback<GoodBuyRespons>() {
            @Override
            public void onResponse(Call<GoodBuyRespons> call, retrofit2.Response<GoodBuyRespons> response) {
                Log.e("onResponse", "" + response.body());
                assert response.body() != null;

                goodbuys = response.body().getGoodsbuy();
                if (goodbuys.get(0).getErrCode() > 0){
                    Toast.makeText(mContext, goodbuys.get(0).getErrDesc(), Toast.LENGTH_SHORT).show();
                }else{
                    BuyActivity activity = (BuyActivity) mContext;
                    activity.buyrefresh(position,facamount);
                }

            }

            @Override
            public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                Log.e("onFailure", "" + t.toString());
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
