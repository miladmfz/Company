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
import androidx.appcompat.widget.LinearLayoutCompat;

import android.widget.TextView;


import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kits.company.R;
import com.kits.company.activity.DetailActivity;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;


import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Good_view_Adapter extends RecyclerView.Adapter<Good_view_Adapter.GoodViewHolder>{
    private final Context mContext;
    DecimalFormat decimalFormat= new DecimalFormat("0,000");
    private final List<Good> goods;
    private APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    byte[] imageByteArray ;
    public Call<String> call2;
    private  Intent intent;

    private String SERVER_IP_ADDRESS,price,name;
    Integer code=0 ;
    String UnitName;
    SharedPreferences shPref ;
    public Good_view_Adapter(List<Good> goods, Context mContext)
    {
        this.mContext = mContext;
        this.goods = goods;
        SERVER_IP_ADDRESS = mContext.getString(R.string.SERVERIP);
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
        code=0;
        final int myps= position;
        final Good goodView = goods.get(position);

        if(Integer.parseInt(goodView.getGoodFieldValue("HasStackAmount"))>0) {
            if (goodView.getGoodFieldValue("MaxSellPrice").equals(goodView.getGoodFieldValue("SellPrice"))) {
                holder.maxsellpriceTextView.setVisibility(View.GONE);
            } else {
                holder.maxsellpriceTextView.setVisibility(View.VISIBLE);
                SpannableString spannableString = new SpannableString(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodView.getGoodFieldValue("MaxSellPrice")))));
                spannableString.setSpan(new StrikethroughSpan(), 0, goodView.getGoodFieldValue("MaxSellPrice").length(), Spanned.SPAN_MARK_MARK);
                holder.maxsellpriceTextView.setText(spannableString);
            }
            holder.sellpercent.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodView.getGoodFieldValue("SellPrice")))));
            holder.btnadd.setVisibility(View.VISIBLE);
            holder.sellpercent.setTextColor(mContext.getResources().getColor(R.color.green_900));


        }else {
            holder.maxsellpriceTextView.setVisibility(View.GONE);
            holder.btnadd.setVisibility(View.INVISIBLE);
            holder.sellpercent.setText("ناموجود");
            holder.sellpercent.setTextColor(mContext.getResources().getColor(R.color.red_300));
        }


        holder.goodnameTextView.setText(Farsi_number.PerisanNumber(goodView.getGoodFieldValue("GoodName")));



        code=Integer.parseInt(goodView.getGoodFieldValue("GoodCode"));
        price=goodView.getGoodFieldValue("SellPrice");
        name=goodView.getGoodFieldValue("GoodName");
        String SERVER_IP_ADDRESS = mContext.getString(R.string.SERVERIP);

        if(!goods.get(position).getGoodImageName().equals("")){
            Glide.with(holder.img)
                    .asBitmap()
                    .load(R.drawable.white)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(holder.img);
            holder.img.setVisibility(View.VISIBLE);

            Glide.with(holder.img)
                    .asBitmap()
                    .load(Base64.decode(goods.get(position).getGoodImageName(), Base64.DEFAULT))
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

            call2 = apiInterface_image.GetImage("getImage",goodView.getGoodFieldValue("GoodCode"),0,150);
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call2, Response<String> response) {
                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        try {
                            if(!response.body().equals("no_photo")) {
                                goods.get(position).setGoodImageName(response.body());
                                Glide.with(holder.img)
                                        .asBitmap()
                                        .load(Base64.decode(response.body(), Base64.DEFAULT))
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
                public void onFailure(Call<String> call2, Throwable t) {
                    Log.e("onFailure",""+t.toString());
                }
            });
        }


        holder.rltv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                Good goodView = goods.get(myps);
                intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("id", Integer.parseInt(goodView.getGoodFieldValue("GoodCode")));
                mContext.startActivity(intent);
            }

        });





        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Buy_box buy_box = new Buy_box(mContext);
                buy_box.buydialog(goodView.getGoodFieldValue("GoodName"),goodView.getGoodFieldValue("SellPrice"),Integer.parseInt(goodView.getGoodFieldValue("GoodCode")));

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return goods.size();

    }

    class GoodViewHolder extends RecyclerView.ViewHolder
    {
        private TextView goodnameTextView;
        private TextView maxsellpriceTextView;
        private TextView sellpercent;
        private Button btnadd;
        private ImageView img ;
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
