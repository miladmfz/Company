package com.kits.company.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
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
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

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
    public Call<RetrofitResponse> call2;
    public boolean multi_select;
    public boolean permission_multi=false;
    private Intent intent;
    BuyBox buyBox;
    private final Context mContext;


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

        if(mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")){
            permission_multi=true;
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


        if (goods.get(position).getGoodFieldValue("GoodName").length()>20){
            holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(goods.get(position).getGoodFieldValue("GoodName").substring(0,20)+"..."));
        }else {
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


        } else
        {
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
                        notifyDataSetChanged();
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


        holder.btnadd.setOnClickListener(view -> buyBox.TestDialog(goods.get(position), 0, "0"));


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
        private final LinearLayoutCompat ggg;
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
            ggg = itemView.findViewById(R.id.proserch_ggg);
        }
    }


}
