package com.kits.company.adapter;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Base64;
import android.util.Log;
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
import com.kits.company.activity.GrpActivity;
import com.kits.company.activity.SearchActivity;
import com.kits.company.activity.Search_date_detailActivity;
import com.kits.company.application.App;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.Good;
import com.kits.company.model.RetrofitRespons;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Good_ProSearch_Adapter extends RecyclerView.Adapter<Good_ProSearch_Adapter.GoodViewHolder>{
    DecimalFormat decimalFormat= new DecimalFormat("0,000");
    private List<Good> goods= new ArrayList<>();
    private final List<Good> available_goods = new ArrayList<>();
    private List<Good> all_goods= new ArrayList<>();
    public APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public Call<RetrofitRespons> call2;
    public boolean multi_select;
    private  Intent intent;
    BuyBox buyBox;
    private final Context mContext;


    public Good_ProSearch_Adapter(List<Good> goods, Context context)
    {
        this.mContext=context;
        this.all_goods = goods;
        for (Good g : all_goods) {
            if (Float.parseFloat(g.getGoodFieldValue("TotalAmount"))>0) {
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
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_prosearch, parent, false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, final int position)
    {
        buyBox=new BuyBox(mContext);

        available_goods.clear();

        for (Good g : all_goods) {
            if (Float.parseFloat(g.getGoodFieldValue("TotalAmount"))>0) {
                if (!App.getContext().getString(R.string.app_name).equals("ATA kala")) {
                    if (Float.parseFloat(g.getGoodFieldValue("active")) > 0) {
                        this.available_goods.add(g);
                    }
                }
            }
        }

        if(GetShared.ReadBoolan("available_good")){
            goods= available_goods;
        }else{
            goods = all_goods;
        }
        final Good goodView = goods.get(position);


        holder.img.setVisibility(View.INVISIBLE);
        holder.rltv.setVisibility(View.VISIBLE);

        holder.rltv.setChecked(goods.get(position).isCheck());

        if(Integer.parseInt(goodView.getGoodFieldValue("HasStackAmount"))>0) {
            if (goodView.getGoodFieldValue("MaxSellPrice").equals(goodView.getGoodFieldValue("SellPrice"))) {
                holder.maxsellpriceTextView.setVisibility(View.GONE);
            } else {
                holder.maxsellpriceTextView.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(goodView.getGoodFieldValue("MaxSellPrice")))));
                spannableString.setSpan(new StrikethroughSpan(), 0, goodView.getGoodFieldValue("MaxSellPrice").length(), Spanned.SPAN_MARK_MARK);
                holder.maxsellpriceTextView.setText(spannableString);
            }
            holder.sellpercent.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(goodView.getGoodFieldValue("SellPrice")))));
            holder.btnadd.setVisibility(View.VISIBLE);
            holder.sellpercent.setTextColor(App.getContext().getResources().getColor(R.color.green_900));
            holder.rltv.setCheckable(true);

        }else {

                holder.btnadd.setVisibility(View.INVISIBLE);
                holder.maxsellpriceTextView.setVisibility(View.GONE);
                holder.sellpercent.setText("ناموجود");
                holder.sellpercent.setTextColor(App.getContext().getResources().getColor(R.color.red_300));
                holder.rltv.setCheckable(true);
                goods.get(position).setCheck(false);
                holder.rltv.setCheckable(false);

        }

        if(Float.parseFloat(goodView.getGoodFieldValue("totalamount"))>0){
            holder.totalstate.setVisibility(View.GONE);
        }else {
            holder.totalstate.setVisibility(View.VISIBLE);
        }

        holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(goodView.getGoodFieldValue("GoodName")));

        if(!goods.get(position).getGoodFieldValue("GoodImageName").equals("")){
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



        }else{
            Glide.with(holder.img)
                    .asBitmap()
                    .load(R.drawable.white)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(holder.img);
            holder.img.setVisibility(View.VISIBLE);

            call2 = apiInterface_image.GetImage(
                    "getImage",
                    goodView.getGoodFieldValue("GoodCode"),
                    "0",
                    "150");
            call2.enqueue(new Callback<RetrofitRespons>() {
                @Override
                public void onResponse(Call<RetrofitRespons> call2, Response<RetrofitRespons> response) {
                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        try {
                            if(!response.body().getText().equals("no_photo")) {
                                goods.get(position).setGoodImageName(response.body().getText());
                                Glide.with(holder.img)
                                        .asBitmap()
                                        .load(Base64.decode(response.body().getText(), Base64.DEFAULT))
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .into(holder.img);

                            } else {
                                Glide.with(holder.img)
                                        .asBitmap()
                                        .load(R.drawable.no_photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .fitCenter()
                                        .into(holder.img);

                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }


                    }
                }
                @Override
                public void onFailure(Call<RetrofitRespons> call2, Throwable t) {
                    Log.e("onFailure",""+t.toString());
                }
            });
        }










        holder.rltv.setOnClickListener(v -> {

            if(multi_select){
                if(Integer.parseInt(goodView.getGoodFieldValue("HasStackAmount"))>0) {
                    holder.rltv.setChecked(!holder.rltv.isChecked());
                    goods.get(position).setCheck(!goods.get(position).isCheck());
                    if (goods.get(position).isCheck()) {
                        if (App.getContext().getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                            SearchActivity activity = (SearchActivity) App.getContext() ;
                            activity.good_select_function(goodView, 1);
                        }
                        if (App.getContext().getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                            Search_date_detailActivity activity = (Search_date_detailActivity) App.getContext() ;
                            activity.good_select_function(goodView, 1);
                        }
                        if (App.getContext().getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                            GrpActivity activity = (GrpActivity) App.getContext() ;
                            activity.good_select_function(goodView, 1);
                        }
                    } else {
                        if (App.getContext().getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                            SearchActivity activity = (SearchActivity) App.getContext() ;
                            activity.good_select_function(goodView, 0);
                        }
                        if (App.getContext().getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                            Search_date_detailActivity activity = (Search_date_detailActivity) App.getContext() ;
                            activity.good_select_function(goodView, 0);
                        }
                        if (App.getContext().getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                            GrpActivity activity = (GrpActivity) App.getContext() ;
                            activity.good_select_function(goodView, 0);
                        }

                    }

                }
            }else {
                Good goodView1 = goods.get(position);
                intent = new Intent(App.getContext(), DetailActivity.class);
                intent.putExtra("id",Integer.parseInt(goodView1.getGoodFieldValue("GoodCode")));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                App.getContext().startActivity(intent);
            }

        });





        holder.btnadd.setOnClickListener(view -> buyBox.buydialog(goodView));


        holder.rltv.setOnLongClickListener(view -> {
            multi_select=true;

            if(Integer.parseInt(goodView.getGoodFieldValue("HasStackAmount"))>0) {
                holder.rltv.setChecked(!holder.rltv.isChecked());
                goods.get(position).setCheck(!goods.get(position).isCheck());

                if (goods.get(position).isCheck()) {
                    if (App.getContext().getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                        SearchActivity activity = (SearchActivity) App.getContext() ;
                        activity.good_select_function(goodView, 1);
                    }
                    if (App.getContext().getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                        Search_date_detailActivity activity = (Search_date_detailActivity) App.getContext() ;
                        activity.good_select_function(goodView, 1);
                    }
                    if (App.getContext().getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                        GrpActivity activity = (GrpActivity) App.getContext() ;
                        activity.good_select_function(goodView, 1);
                    }
                } else {
                    if (App.getContext().getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                        SearchActivity activity = (SearchActivity) App.getContext() ;
                        activity.good_select_function(goodView, 0);
                    }
                    if (App.getContext().getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                        Search_date_detailActivity activity = (Search_date_detailActivity) App.getContext() ;
                        activity.good_select_function(goodView, 0);
                    }
                    if (App.getContext().getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                        GrpActivity activity = (GrpActivity) App.getContext() ;
                        activity.good_select_function(goodView, 0);
                    }

                }

            }
            return true;
        });


    }

    @Override
    public int getItemCount()
    {
        if(GetShared.ReadBoolan("available_good")){
            return available_goods.size();
        }else{
            return all_goods.size();
        }
    }




    class GoodViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView goodnameTextView;
        private final TextView maxsellpriceTextView;
        private final TextView sellpercent;
        private final TextView totalstate;
        private final Button btnadd;
        private final ImageView img ;
        private final LinearLayoutCompat ggg ;
        MaterialCardView rltv;

        GoodViewHolder(View itemView)
        {
            super(itemView);
            goodnameTextView = itemView.findViewById(R.id.good_prosearch_name);
            maxsellpriceTextView = itemView.findViewById(R.id.good_prosearch_price);
            sellpercent = itemView.findViewById(R.id.good_prosearch_percent);
            totalstate = itemView.findViewById(R.id.good_prosearch_totalstate);
            img =  itemView.findViewById(R.id.good_prosearch_img) ;
            rltv =  itemView.findViewById(R.id.good_prosearch);
            btnadd = itemView.findViewById(R.id.good_prosearch_btn);
            ggg = itemView.findViewById(R.id.proserch_ggg);
        }
    }



}
