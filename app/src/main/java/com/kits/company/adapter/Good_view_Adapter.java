package com.kits.company.adapter;


import android.app.Activity;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.kits.company.R;
import com.kits.company.activity.DetailActivity;
import com.kits.company.application.App;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.Good;
import com.kits.company.model.RetrofitRespons;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Good_view_Adapter extends RecyclerView.Adapter<Good_view_Adapter.GoodViewHolder>{
    DecimalFormat decimalFormat= new DecimalFormat("0,000");
    private final List<Good> goods;
    private final APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public Call<RetrofitRespons> call2;
    private  Intent intent;


    BuyBox buyBox;
    private final Context mContext;




    public Good_view_Adapter(List<Good> goods, Context context)
    {
        this.mContext=context;
        this.goods = goods;
    }
    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_view, parent, false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder,final int position)
    {
        buyBox=new BuyBox(mContext);



        final Good goodView = goods.get(position);




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

        }else {
            holder.maxsellpriceTextView.setVisibility(View.GONE);
            holder.btnadd.setVisibility(View.INVISIBLE);
            holder.sellpercent.setText("ناموجود");
            holder.sellpercent.setTextColor(App.getContext().getResources().getColor(R.color.red_300));
            holder.rltv.setVisibility(View.GONE);

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
                    "150"
            );
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


            Good goodView1 = goods.get(position);
            intent = new Intent(App.getContext(), DetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("id", Integer.parseInt(goodView1.getGoodFieldValue("GoodCode")));

            App.getContext().startActivity(intent);
        });





        holder.btnadd.setOnClickListener(view -> {

            buyBox.buydialog(goodView);

        });
    }

    @Override
    public int getItemCount()
    {
        return goods.size();

    }

    class GoodViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView goodnameTextView;
        private final TextView maxsellpriceTextView;
        private final TextView sellpercent;
        private final Button btnadd;
        private final ImageView img ;
        MaterialCardView rltv;

        GoodViewHolder(View itemView)
        {
            super(itemView);
            goodnameTextView = itemView.findViewById(R.id.good_prosearch_view_name);
            maxsellpriceTextView = itemView.findViewById(R.id.good_prosearch_view_price);
            sellpercent = itemView.findViewById(R.id.good_prosearch_view_percent);
            img =  itemView.findViewById(R.id.good_prosearch_view_img) ;
            rltv =  itemView.findViewById(R.id.good_prosearch_view);
            btnadd = itemView.findViewById(R.id.good_prosearch_view_btn);
        }
    }

}
