package com.kits.company.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.kits.company.R;
import com.kits.company.activity.BuyActivity;
import com.kits.company.activity.DetailActivity;
import com.kits.company.activity.FavoriteActivity;
import com.kits.company.activity.GrpActivity;
import com.kits.company.activity.MainActivity;
import com.kits.company.activity.SearchActivity;
import com.kits.company.activity.Search_date_detailActivity;
import com.kits.company.application.App;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.Good;
import com.kits.company.model.RetrofitRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuyBox {

    private final APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    float last_amount=0;
    Spinner spinner;
    long iPrice=0;
    float iAmount=0;
    ArrayList<Good> Goods;
    Good good;
    private final Context mContext;

    String DefaultUnitValue ="1";
    String GoodUnitRef ="1";
    String UnitName ="";

    ArrayList<String> GoodUnitCode_array=new ArrayList<>();
    ArrayList<String> GoodUnitRatio_array=new ArrayList<>();
    ArrayList<String> GoodUnitName_array=new ArrayList<>();

    int GoodUnitCode_index=0;
    int GoodUnitRatio_index=0;
    int GoodUnitName_index=0;



    public BuyBox(Context mContext)
    {
        this.mContext = mContext;
    }
    public void buydialog(Good g) {
        this.good=g;
        GoodUnitRef=good.getGoodFieldValue("GoodUnitRef");
        GoodUnitRef=good.getGoodFieldValue("GoodUnitRef");
        DefaultUnitValue=good.getGoodFieldValue("DefaultUnitValue");
        UnitName=good.getGoodFieldValue("UnitName");
        int z;
        for(z=1;z<6;z++){
            if(Integer.parseInt(good.getGoodFieldValue("UnitRef"+z))>0){
                GoodUnitCode_array.add(good.getGoodFieldValue("UnitRef"+z));
                if(z>1) {
                    if (Integer.parseInt(good.getGoodFieldValue("Ratio" + z)) > 0) {
                        GoodUnitRatio_array.add(good.getGoodFieldValue("Ratio" + z));
                    }
                }else {
                    GoodUnitRatio_array.add("1");
                }
                GoodUnitName_array.add(good.getGoodFieldValue("UnitName"+z));

            }
        }


        int y=0;
        for ( String string : GoodUnitCode_array) {
            if(string.equals(GoodUnitRef)){
                GoodUnitCode_index=y;
            }
            y++;
        }
        y=0;
        for ( String string : GoodUnitRatio_array) {
            if(string.equals(DefaultUnitValue)){
                GoodUnitRatio_index=y;
            }
            y++;
        }
        y=0;
        for ( String string : GoodUnitName_array) {
            if(string.equals(UnitName)){
                GoodUnitName_index=y;
            }
            y++;
        }

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
        dialog.setContentView(R.layout.box_buy);
        Button boxbuy = dialog.findViewById(R.id.box_buy_btn);

        final TextView tv_goodname = dialog.findViewById(R.id.box_buy_name);
        final EditText amount = dialog.findViewById(R.id.box_buy_amount);
        final TextView price = dialog.findViewById(R.id.box_buy_price);
        final TextView sumprice = dialog.findViewById(R.id.box_buy_sumprice);
        spinner = dialog.findViewById(R.id.box_buy_unit);

        tv_goodname.setText(good.getGoodFieldValue("GoodName"));
        price.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue("SellPrice")));


        amount.setHint(good.getGoodFieldValue("BasketAmount"));
        last_amount=last_amount+ Float.parseFloat(good.getGoodFieldValue("BasketAmount"));

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_item,GoodUnitName_array);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinner_adapter);
        spinner.setSelection(GoodUnitName_index);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoodUnitRef=GoodUnitCode_array.get(position);
                DefaultUnitValue=GoodUnitRatio_array.get(position);
                UnitName=GoodUnitName_array.get(position);
                try
                {
                    sumprice.setText(NumberFunctions.PerisanNumber(""+iPrice*iAmount*Float.parseFloat(DefaultUnitValue)));
                } catch (Exception e) {
                    sumprice.setText("");
                }
                if(good.getGoodFieldValue("MustInteger").equals("0")){
                    amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        if(good.getGoodFieldValue("MustInteger").equals("0")){
            amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

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

                    iPrice = Integer.parseInt(NumberFunctions.EnglishNumber(price.getText().toString()));
                    iAmount = Float.parseFloat(NumberFunctions.EnglishNumber(amount.getText().toString()));
                    long totalprice = (long) (iPrice * iAmount* Integer.parseInt(DefaultUnitValue));
                    sumprice.setText(NumberFunctions.PerisanNumber(String.valueOf(totalprice)));

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
                InputMethodManager inputMethodManager = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

        boxbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amo =NumberFunctions.EnglishNumber(amount.getText().toString());
                String pr =NumberFunctions.EnglishNumber(price.getText().toString());

                try {
                    if(!amo.equals("")) {
                        if(Float.parseFloat(amo)>0) {
                            Call<RetrofitRespons> call = apiInterface.InsertBasket(
                                    "Insertbasket",
                                    "DeviceCode",
                                    good.getGoodFieldValue("GoodCode"),
                                    String.valueOf(Float.parseFloat(amo)+last_amount),
                                    pr,
                                    GoodUnitRef,
                                    DefaultUnitValue,
                                    "test",
                                    GetShared.ReadString("mobile"));
                            call.enqueue(new Callback<RetrofitRespons>() {
                                @Override
                                public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                                    Log.e("onResponse", "" + response.body());
                                    assert response.body() != null;

                                    Goods = response.body().getGoods();

                                    if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode"))> 0){

                                        App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));

                                    }else{
                                        App.showToast( "کالای مورد نظر به سبد خرید اضافه شد");
                                        if(mContext.getClass().getName().equals("com.kits.userberoz.activity.SearchActivity")){
                                            SearchActivity activity= (SearchActivity) mContext;
                                            activity.setupBadge();
                                        }

                                        if(mContext.getClass().getName().equals("com.kits.userberoz.activity.Search_date_detailActivity")){
                                            Search_date_detailActivity activity= (Search_date_detailActivity) mContext ;
                                            activity.setupBadge();
                                        }

                                        if(mContext.getClass().getName().equals("com.kits.userberoz.activity.GrpActivity")){
                                            GrpActivity activity= (GrpActivity) mContext ;
                                            activity.setupBadge();
                                        }

                                        if(mContext.getClass().getName().equals("com.kits.userberoz.activity.MainActivity")){
                                            MainActivity activity= (MainActivity) mContext ;
                                            activity.setupBadge();
                                        }

                                        if(mContext.getClass().getName().equals("com.kits.userberoz.activity.FavoriteActivity")){
                                            FavoriteActivity activity= (FavoriteActivity) mContext ;
                                            activity.setupBadge();
                                        }

                                        if(mContext.getClass().getName().equals("com.kits.userberoz.activity.DetailActivity")){
                                            DetailActivity activity= (DetailActivity) mContext ;
                                            activity.setupBadge();
                                        }

                                        dialog.dismiss();

                                    }
                                }

                                @Override
                                public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                                    Log.e("onFailure", "" + t.toString());
                                }
                            });
                        }else {
                            App.showToast("لطفا تعداد صحیح را وارد کنید");

                        }
                    }else {
                        App.showToast("لطفا تعداد مورد نظر را وارد کنید");
                    }
                }catch (Exception e){
                    App.showToast( "مقادیر وارد شده را اصلاح نمایید");
                }


            }
        });
    }


    public void deletegoodfrombasket (String goodcode) {

        Call<RetrofitRespons> call = apiInterface.Basketdelete(
                "deletebasket",
                "DeviceCode",
                goodcode,
                GetShared.ReadString("mobile")
        );
        call.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                Log.e("onResponse", "" + response.body());
                assert response.body() != null;
                if (response.body().getText().equals("done")) {
                    App.showToast("کالای مورد نظر حذف گردید");
                }
            }

            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("onFailure", "" + t.toString());
            }
        });

    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void basketdialog(Good g, final int position) {

        this.good=g;
        GoodUnitRef=good.getGoodFieldValue("GoodUnitRef");
        DefaultUnitValue=good.getGoodFieldValue("DefaultUnitValue");
        UnitName=good.getGoodFieldValue("UnitName");
        int z;
        for(z=1;z<6;z++){
            try {
                if(Integer.parseInt(good.getGoodFieldValue("UnitRef"+z))>0){
                    GoodUnitCode_array.add(good.getGoodFieldValue("UnitRef"+z));
                    if(z>1) {
                        if (Integer.parseInt(good.getGoodFieldValue("Ratio" + z)) > 0) {
                            GoodUnitRatio_array.add(good.getGoodFieldValue("Ratio" + z));
                        }
                    }else {
                        GoodUnitRatio_array.add("1");
                    }
                    GoodUnitName_array.add(good.getGoodFieldValue("UnitName"+z));

                }
            }catch (Exception e){
            }

        }


        int y=0;
        for ( String string : GoodUnitCode_array) {
            if(string.equals(GoodUnitRef)){
                GoodUnitCode_index=y;
            }
            y++;
        }
        y=0;
        for ( String string : GoodUnitRatio_array) {
            if(string.equals(DefaultUnitValue)){
                GoodUnitRatio_index=y;
            }
            y++;
        }
        y=0;
        for ( String string : GoodUnitName_array) {
            if(string.equals(UnitName)){
                GoodUnitName_index=y;
            }
            y++;
        }


        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
        dialog.setContentView(R.layout.box_buy);
        Button boxbuy = dialog.findViewById(R.id.box_buy_btn);

        final TextView tv_goodname = dialog.findViewById(R.id.box_buy_name);
        final EditText amount = dialog.findViewById(R.id.box_buy_amount);
        final TextView price = dialog.findViewById(R.id.box_buy_price);
        final TextView sumprice = dialog.findViewById(R.id.box_buy_sumprice);
        final Spinner sp_unitname = dialog.findViewById(R.id.box_buy_unit);
        final TextView tv_unitname = dialog.findViewById(R.id.box_buy_unit_tv);
        sp_unitname.setVisibility(View.GONE);
        tv_unitname.setVisibility(View.VISIBLE);
        tv_unitname.setText(UnitName);

        tv_goodname.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue("GoodName")));
        price.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue("sellprice")));
        amount.setHint(NumberFunctions.PerisanNumber(good.getGoodFieldValue("FacAmount")));

        if(good.getGoodFieldValue("MustInteger").equals("0")){
            amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
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
                    float iPrice = Integer.parseInt(NumberFunctions.EnglishNumber(price.getText().toString()));
                    float iAmount = Float.parseFloat(NumberFunctions.EnglishNumber(amount.getText().toString()));
                    long totalprice = (long) iPrice * (long) iAmount* (long) Integer.parseInt(DefaultUnitValue);
                    sumprice.setText(NumberFunctions.PerisanNumber(String.valueOf(totalprice)));
                } catch (Exception e) {
                    sumprice.setText("");
                }
            }
        });



        dialog.show();
        amount.requestFocus();
        amount.postDelayed(() -> {
            InputMethodManager inputMethodManager = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(amount, InputMethodManager.SHOW_IMPLICIT);
        }, 500);

        boxbuy.setOnClickListener(view -> {
            final String amo =NumberFunctions.EnglishNumber(amount.getText().toString());
            String pr = NumberFunctions.EnglishNumber(price.getText().toString());
            try {
                if (!amo.equals("")) {
                    if (Float.parseFloat(amo) > 0) {

                        Call<RetrofitRespons> call = apiInterface.InsertBasket(
                                "Insertbasket",
                                "DeviceCode",
                                good.getGoodFieldValue("GoodCode"),
                                String.valueOf(Float.parseFloat(amo)),
                                pr,
                                GoodUnitRef,
                                DefaultUnitValue,
                                "test",
                                GetShared.ReadString("mobile")
                        );
                        call.enqueue(new Callback<RetrofitRespons>() {
                            @Override
                            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                                Log.e("onResponse", "" + response.body());
                                assert response.body() != null;

                                Goods = response.body().getGoods();

                                if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode")) > 0) {

                                    App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                                } else {
                                    BuyActivity activity = (BuyActivity) mContext ;
                                    activity.buyrefresh(position, amo);
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                                Log.e("onFailure", "" + t.toString());
                            }
                        });
                    } else {
                        App.showToast("لطفا تعداد صحیح را وارد کنید");
                    }
                } else {
                    App.showToast("لطفا تعداد مورد نظر را وارد کنید");
                }
            }catch (Exception e){
                App.showToast("مقادیر وارد شده را اصلاح نمایید");
            }


        });
    }


    public void basketdsolo(Good g , String facamount, final int position) {

        this.good=g;
        Call<RetrofitRespons> call = apiInterface.InsertBasket("Insertbasket",
                "DeviceCode",
                good.getGoodFieldValue("GoodCode"),
                String.valueOf(facamount),
                good.getGoodFieldValue("sellprice"),
                good.getGoodFieldValue("bUnitRef"),
                good.getGoodFieldValue("bRatio"),
                "test",
                GetShared.ReadString("mobile")
        );
        call.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                Log.e("onResponse", "" + response.body());
                assert response.body() != null;

                Goods = response.body().getGoods();
                if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode"))> 0){
                    App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                }else{
                    BuyActivity activity = (BuyActivity) mContext ;
                    activity.buyrefresh(position,String.valueOf(facamount));
                }

            }

            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("onFailure", "" + t.toString());
            }
        });
    }




}
