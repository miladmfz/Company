package com.kits.company.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kits.company.R;
import com.kits.company.activity.BasketActivity;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BuyBox {

    private final APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    float last_amount = 0;
    Spinner spinner;
    long iPrice = 0;
    float iAmount = 0;
    ArrayList<Good> Goods;
    private final Context mContext;

    String DefaultUnitValue = "1";
    String GoodUnitRef = "1";
    String UnitName = "";

    ArrayList<String> GoodUnitCode_array = new ArrayList<>();
    ArrayList<String> GoodUnitRatio_array = new ArrayList<>();
    ArrayList<String> GoodUnitName_array = new ArrayList<>();

    int GoodUnitCode_index = 0;
    int GoodUnitRatio_index = 0;
    int GoodUnitName_index = 0;
    Call<RetrofitResponse> call;

    public BuyBox(Context mContext) {
        this.mContext = mContext;
    }

    public void basketdsolo(@NonNull Good good, String facamount, final int position) {
        last_amount = 0;

        Call<RetrofitResponse> call = apiInterface.InsertBasket("Insertbasket",
                "DeviceCode",
                good.getGoodFieldValue("GoodCode"),
                String.valueOf(facamount),
                good.getGoodFieldValue("sellprice"),
                good.getGoodFieldValue("bUnitRef"),
                good.getGoodFieldValue("bRatio"),
                "test",
                GetShared.ReadString("mobile")
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                assert response.body() != null;

                Goods = response.body().getGoods();
                if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode")) > 0) {
                    App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                } else {
                    BasketActivity activity = (BasketActivity) mContext;
                    activity.buyrefresh(position, String.valueOf(facamount));
                }

            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
            }
        });
    }


    public void BuyDialog(@NonNull Good good, final int position, String BasketFlag) {
        last_amount = 0;
        GoodUnitRef = good.getGoodFieldValue("GoodUnitRef");
        DefaultUnitValue = good.getGoodFieldValue("DefaultUnitValue");
        UnitName = good.getGoodFieldValue("UnitName");
        int sellprizeCounter;
        for (sellprizeCounter = 1; sellprizeCounter < 6; sellprizeCounter++) {
            if (Integer.parseInt(good.getGoodFieldValue("UnitRef" + sellprizeCounter)) > 0) {
                GoodUnitCode_array.add(good.getGoodFieldValue("UnitRef" + sellprizeCounter));
                if (sellprizeCounter > 1) {
                    if (Integer.parseInt(good.getGoodFieldValue("Ratio" + sellprizeCounter)) > 0) {
                        GoodUnitRatio_array.add(good.getGoodFieldValue("Ratio" + sellprizeCounter));
                    }
                } else {
                    GoodUnitRatio_array.add("1");
                }
                GoodUnitName_array.add(good.getGoodFieldValue("UnitName" + sellprizeCounter));

            }
        }


        int TempCounter = 0;
        for (String string : GoodUnitCode_array) {
            if (string.equals(GoodUnitRef)) {
                GoodUnitCode_index = TempCounter;
            }
            TempCounter++;
        }
        TempCounter = 0;
        for (String string : GoodUnitRatio_array) {
            if (string.equals(DefaultUnitValue)) {
                GoodUnitRatio_index = TempCounter;
            }
            TempCounter++;
        }
        TempCounter = 0;
        for (String string : GoodUnitName_array) {
            if (string.equals(UnitName)) {
                GoodUnitName_index = TempCounter;
            }
            TempCounter++;
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


        tv_goodname.setText(good.getGoodFieldValue("GoodName"));
        price.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue("SellPrice")));
        amount.setHint(good.getGoodFieldValue("BasketAmount"));

        last_amount = last_amount + Float.parseFloat(good.getGoodFieldValue("BasketAmount"));

        ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, GoodUnitName_array);

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_unitname.setAdapter(spinner_adapter);
        sp_unitname.setSelection(GoodUnitName_index);


        sp_unitname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GoodUnitRef = GoodUnitCode_array.get(position);
                DefaultUnitValue = GoodUnitRatio_array.get(position);
                UnitName = GoodUnitName_array.get(position);
                try {
                    sumprice.setText(NumberFunctions.PerisanNumber("" + iPrice * iAmount * Float.parseFloat(DefaultUnitValue)));
                } catch (Exception e) {
                    sumprice.setText("");
                }
                if (good.getGoodFieldValue("MustInteger").equals("0")) {
                    amount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (good.getGoodFieldValue("MustInteger").equals("0")) {
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
                try {

                    iPrice = Integer.parseInt(NumberFunctions.EnglishNumber(price.getText().toString()));
                    iAmount = Float.parseFloat(NumberFunctions.EnglishNumber(amount.getText().toString()));
                    long totalprice = (long) (iPrice * iAmount * Integer.parseInt(DefaultUnitValue));
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

            String amo = NumberFunctions.EnglishNumber(amount.getText().toString());
            String pr = NumberFunctions.EnglishNumber(price.getText().toString());

            try {
                if (!amo.equals("")) {
                    if (Float.parseFloat(amo) > 0) {

                        if (BasketFlag.equals("0")) {
                            call = apiInterface.InsertBasket(
                                    "Insertbasket",
                                    "DeviceCode",
                                    good.getGoodFieldValue("GoodCode"),
                                    String.valueOf(Float.parseFloat(amo) + last_amount),
                                    pr,
                                    GoodUnitRef,
                                    DefaultUnitValue,
                                    "test",
                                    GetShared.ReadString("mobile"));

                        } else {
                            call = apiInterface.InsertBasket(
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
                        }

                        call.enqueue(new Callback<RetrofitResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                assert response.body() != null;

                                Goods = response.body().getGoods();

                                if (BasketFlag.equals("0")) {
                                    if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode")) > 0) {
                                        App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                                    } else {

                                        App.showToast("کالای مورد نظر به سبد خرید اضافه شد");
                                        dialog.dismiss();
                                    }
                                } else {
                                    if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode")) > 0) {
                                        App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                                    } else {
                                        BasketActivity activity = (BasketActivity) mContext;
                                        activity.buyrefresh(position, amo);
                                        dialog.dismiss();
                                    }


                                }


                            }

                            @Override
                            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                            }
                        });
                    } else {
                        App.showToast("لطفا تعداد صحیح را وارد کنید");

                    }
                } else {
                    App.showToast("لطفا تعداد مورد نظر را وارد کنید");
                }
            } catch (Exception e) {
                App.showToast("مقادیر وارد شده را اصلاح نمایید");
            }


        });
    }


    public void deletegoodfrombasket(String goodcode) {
        last_amount = 0;
        Call<RetrofitResponse> call = apiInterface.Basketdelete(
                "deletebasket",
                "DeviceCode",
                goodcode,
                GetShared.ReadString("mobile")
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                assert response.body() != null;
                if (response.body().getText().equals("done")) {
                    App.showToast("کالای مورد نظر حذف گردید");
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
            }
        });

    }


}
