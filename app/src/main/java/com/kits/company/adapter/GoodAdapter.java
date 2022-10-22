package com.kits.company.adapter;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.kits.company.R;
import com.kits.company.activity.DetailActivity;
import com.kits.company.activity.SearchActivity;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.GoodViewHolder> {
    DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private List<Good> goods = new ArrayList<>();
    private final List<Good> available_goods = new ArrayList<>();
    private List<Good> all_goods = new ArrayList<>();
    public APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    public Call<RetrofitResponse> call2;
    public boolean multi_select;
    public boolean permission_multi = false;
    private Intent intent;
    BuyBox buyBox;
    private final Context mContext;


    float last_amount = 0;
    Spinner spinner;
    long iPrice = 0;
    float iAmount = 0;
    ArrayList<Good> Goods;
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

    public GoodAdapter(List<Good> goods, Context context) {
        this.mContext = context;
        this.all_goods = goods;

        this.buyBox = new BuyBox(mContext);

        for (Good g : all_goods) {
            if (Float.parseFloat(g.getGoodFieldValue("TotalAmount")) > 0) {
                if (!App.getContext().getString(R.string.app_name).equals("ATA kala")) {
                    if (Float.parseFloat(g.getGoodFieldValue("active")) > 0) {
                        this.available_goods.add(g);
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (GetShared.ReadString("view").equals("grid")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_prosearch, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_prosearch_line, parent, false);
        }
        if (mContext.getClass().getName().equals("com.kits.company.activity.MainActivity")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_mainview, parent, false);
        }
        if (mContext.getClass().getName().equals("com.kits.company.activity.DetailActivity")) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_mainview, parent, false);
        }
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
            permission_multi = true;
        }

        available_goods.clear();


        for (Good g : all_goods) {
            if (Float.parseFloat(g.getGoodFieldValue("TotalAmount")) > 0) {
                if (!App.getContext().getString(R.string.app_name).equals("ATA kala")) {
                    if (Float.parseFloat(g.getGoodFieldValue("active")) > 0) {
                        this.available_goods.add(g);
                    }
                }
            }
        }

        if (GetShared.ReadBoolan("available_good")) {
            goods = available_goods;
        } else {
            goods = all_goods;
        }


        holder.img.setVisibility(View.INVISIBLE);
        holder.rltv.setVisibility(View.VISIBLE);

        holder.rltv.setChecked(goods.get(position).isCheck());

        if (Integer.parseInt(goods.get(position).getGoodFieldValue("HasStackAmount")) > 0) {
            if (goods.get(position).getGoodFieldValue("MaxSellPrice").equals(goods.get(position).getGoodFieldValue("SellPrice"))) {
                holder.maxsellpriceTextView.setVisibility(View.GONE);
            } else {
                holder.maxsellpriceTextView.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(goods.get(position).getGoodFieldValue("MaxSellPrice")))));
                spannableString.setSpan(new StrikethroughSpan(), 0, goods.get(position).getGoodFieldValue("MaxSellPrice").length(), Spanned.SPAN_MARK_MARK);
                holder.maxsellpriceTextView.setText(spannableString);
            }
            holder.sellpercent.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(goods.get(position).getGoodFieldValue("SellPrice")))));
            holder.btnadd.setVisibility(View.VISIBLE);
            holder.sellpercent.setTextColor(App.getContext().getResources().getColor(R.color.green_900));
            holder.rltv.setCheckable(true);

        } else {

            holder.btnadd.setVisibility(View.INVISIBLE);
            holder.maxsellpriceTextView.setVisibility(View.GONE);
            holder.sellpercent.setText("ناموجود");
            holder.sellpercent.setTextColor(App.getContext().getResources().getColor(R.color.red_300));
            holder.rltv.setCheckable(true);
            goods.get(position).setCheck(false);
            holder.rltv.setCheckable(false);

        }

        if (Float.parseFloat(goods.get(position).getGoodFieldValue("totalamount")) > 0) {
            holder.totalstate.setVisibility(View.GONE);
        } else {
            holder.totalstate.setVisibility(View.VISIBLE);
        }


        if (goods.get(position).getGoodFieldValue("GoodName").length() > 20) {
            holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(goods.get(position).getGoodFieldValue("GoodName").substring(0, 20) + "..."));
        } else {
            holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(goods.get(position).getGoodFieldValue("GoodName")));
        }


        if (!goods.get(position).getGoodFieldValue("GoodImageName").equals("")) {
            Glide.with(holder.img)
                    .asBitmap()
                    .load(R.drawable.white)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(holder.img);
            holder.img.setVisibility(View.VISIBLE);

            Glide.with(holder.img)
                    .asBitmap()
                    .load(Base64.decode(goods.get(position).getGoodFieldValue("GoodImageName"), Base64.DEFAULT))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(holder.img);


        } else {
            Glide.with(holder.img)
                    .asBitmap()
                    .load(R.drawable.white)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(holder.img);
            holder.img.setVisibility(View.VISIBLE);
            call2 = apiInterface_image.GetImage(
                    "getImage",
                    goods.get(position).getGoodFieldValue("GoodCode"),
                    "0",
                    "150"
            );
            call2.enqueue(new Callback<RetrofitResponse>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(@NonNull Call<RetrofitResponse> call2, @NonNull Response<RetrofitResponse> response) {
                    if (response.isSuccessful()) {

                        assert response.body() != null;

                        if (!response.body().getText().equals("no_photo")) {
                            goods.get(position).setGoodImageName(response.body().getText());
                        } else {
                            goods.get(position).setGoodImageName(String.valueOf(R.string.no_photo));

                        }
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RetrofitResponse> call2, @NonNull Throwable t) {
                }
            });
        }


        holder.rltv.setOnClickListener(v -> {

            if (multi_select) {
                if (Integer.parseInt(goods.get(position).getGoodFieldValue("HasStackAmount")) > 0) {
                    holder.rltv.setChecked(!holder.rltv.isChecked());
                    goods.get(position).setCheck(!goods.get(position).isCheck());
                    if (goods.get(position).isCheck()) {
                        if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                            SearchActivity activity = (SearchActivity) mContext;
                            activity.good_select_function(goods.get(position));
                        }
                    }

                }
            } else {
                Good goodView1 = goods.get(position);
                intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", Integer.parseInt(goodView1.getGoodFieldValue("GoodCode")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                App.getContext().startActivity(intent);
            }

        });


        holder.btnadd.setOnClickListener(v -> {

            BuyDialog(goods.get(position));
        });


        holder.rltv.setOnLongClickListener(view -> {
            if (permission_multi) {
                multi_select = true;
                if (Integer.parseInt(goods.get(position).getGoodFieldValue("HasStackAmount")) > 0) {
                    holder.rltv.setChecked(!holder.rltv.isChecked());
                    goods.get(position).setCheck(!goods.get(position).isCheck());
                    if (goods.get(position).isCheck()) {
                        if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                            SearchActivity activity = (SearchActivity) mContext;
                            activity.good_select_function(goods.get(position));
                        }
                    }

                }
            }
            return true;
        });


    }

    @Override
    public int getItemCount() {
        if (GetShared.ReadBoolan("available_good")) {
            return available_goods.size();
        } else {
            return all_goods.size();
        }
    }


    class GoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView goodnameTextView;
        private final TextView maxsellpriceTextView;
        private final TextView sellpercent;
        private final TextView totalstate;
        private final Button btnadd;
        private final ImageView img;
        MaterialCardView rltv;

        GoodViewHolder(View itemView) {
            super(itemView);
            goodnameTextView = itemView.findViewById(R.id.good_prosearch_name);
            maxsellpriceTextView = itemView.findViewById(R.id.good_prosearch_price);
            sellpercent = itemView.findViewById(R.id.good_prosearch_percent);
            totalstate = itemView.findViewById(R.id.good_prosearch_totalstate);
            img = itemView.findViewById(R.id.good_prosearch_img);
            rltv = itemView.findViewById(R.id.good_prosearch);
            btnadd = itemView.findViewById(R.id.good_prosearch_btn);
        }
    }


    public void BuyDialog(Good good) {
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
        amount.setHint(NumberFunctions.PerisanNumber(good.getGoodFieldValue("BasketAmount")));

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


                        call.enqueue(new Callback<RetrofitResponse>() {
                            @Override
                            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                assert response.body() != null;

                                Goods = response.body().getGoods();

                                if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode")) > 0) {
                                    App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc"));
                                } else {
                                    goods.get(goods.indexOf(good)).setBasketAmount( String.valueOf(Float.parseFloat(amo) + last_amount));
                                    notifyItemChanged(goods.indexOf(good));
                                    App.showToast("کالای مورد نظر به سبد خرید اضافه شد");
                                    dialog.dismiss();
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


}
