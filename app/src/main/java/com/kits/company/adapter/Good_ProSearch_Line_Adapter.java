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
import android.widget.Toast;


import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kits.company.R;
import com.kits.company.activity.DetailActivity;
import com.kits.company.activity.GrpActivity;
import com.kits.company.activity.SearchActivity;
import com.kits.company.activity.Search_date_detailActivity;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Good_ProSearch_Line_Adapter extends RecyclerView.Adapter<Good_ProSearch_Line_Adapter.GoodViewHolder>{
    private final Context mContext;
    DecimalFormat decimalFormat= new DecimalFormat("0,000");
    private  List<Good> goods;
    private final List<Good> available_goods = new ArrayList<>();
    private final List<Good> all_goods;
    public APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public Call<String> call2;
    byte[] imageByteArray ;
    public boolean multi_select;
    private  Intent intent;

    private String SERVER_IP_ADDRESS,price,name;
    Integer code=0 ;
    String UnitName;
    SharedPreferences shPref ;



    public Good_ProSearch_Line_Adapter(List<Good> goods, Context context)
    {
        this.mContext = context;
        shPref = mContext.getSharedPreferences("profile", Context.MODE_PRIVATE);
        this.all_goods = goods;
        for (Good g : all_goods) {
            if (Integer.parseInt(g.getGoodFieldValue("HasStackAmount"))>0) {
                this.available_goods.add(g);
            }
        }
    }
    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_prosearch_line, parent, false);
        return new GoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, final int position)
    {
        if(shPref.getBoolean("available_good", true)){
            goods = available_goods;
        }else{
            goods = all_goods;
        }

        final Good goodView = goods.get(position);


        holder.img.setVisibility(View.INVISIBLE);
        holder.rltv.setVisibility(View.VISIBLE);


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
            holder.rltv.setCheckable(true);

        }else {
            holder.btnadd.setVisibility(View.INVISIBLE);
            holder.maxsellpriceTextView.setVisibility(View.GONE);
            holder.sellpercent.setText("ناموجود");
            holder.sellpercent.setTextColor(mContext.getResources().getColor(R.color.red_300));
            holder.rltv.setCheckable(true);
            goods.get(position).setCheck(false);
            holder.rltv.setCheckable(false);
        }



        holder.goodnameTextView.setText(Farsi_number.PerisanNumber(goodView.getGoodFieldValue("GoodName")));




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

            call2 = apiInterface_image.GetImage("getImage",goodView.getGoodFieldValue("GoodCode"),0,100);
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

                if(multi_select){
                    if(Integer.parseInt(goodView.getGoodFieldValue("HasStackAmount"))>0) {
                        holder.rltv.setChecked(!holder.rltv.isChecked());
                        goods.get(position).setCheck(!goods.get(position).isCheck());
                        if (goods.get(position).isCheck()) {
                            if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                                SearchActivity activity = (SearchActivity) mContext;
                                activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 1);
                            }
                            if (mContext.getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                                Search_date_detailActivity activity = (Search_date_detailActivity) mContext;
                                activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 1);
                            }
                            if (mContext.getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                                GrpActivity activity = (GrpActivity) mContext;
                                activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 1);
                            }
                        } else {
                            if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                                SearchActivity activity = (SearchActivity) mContext;
                                activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 0);
                            }
                            if (mContext.getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                                Search_date_detailActivity activity = (Search_date_detailActivity) mContext;
                                activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 0);
                            }
                            if (mContext.getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                                GrpActivity activity = (GrpActivity) mContext;
                                activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 0);
                            }

                        }

                    }
                }else {
                    Good goodView = goods.get(position);
                    intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("id",Integer.parseInt(goodView.getGoodFieldValue("GoodCode")));
                    mContext.startActivity(intent);
                }
            }

        });





        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Buy_box buy_box = new Buy_box(mContext);
                buy_box.buydialog(goodView.getGoodFieldValue("GoodName"),goodView.getGoodFieldValue("SellPrice"),Integer.parseInt(goodView.getGoodFieldValue("GoodCode")));

            }
        });

        if(goods.get(position).isCheck()){
            holder.rltv.setChecked(true);
        }else {
            holder.rltv.setChecked(false);
        }
        holder.rltv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                multi_select=true;

                if(Integer.parseInt(goodView.getGoodFieldValue("HasStackAmount"))>0) {
                    holder.rltv.setChecked(!holder.rltv.isChecked());
                    goods.get(position).setCheck(!goods.get(position).isCheck());

                    if (goods.get(position).isCheck()) {
                        if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                            SearchActivity activity = (SearchActivity) mContext;
                            activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 1);
                        }
                        if (mContext.getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                            Search_date_detailActivity activity = (Search_date_detailActivity) mContext;
                            activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 1);
                        }
                        if (mContext.getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                            GrpActivity activity = (GrpActivity) mContext;
                            activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 1);
                        }
                    } else {
                        if (mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")) {
                            SearchActivity activity = (SearchActivity) mContext;
                            activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 0);
                        }
                        if (mContext.getClass().getName().equals("com.kits.company.activity.Search_date_detailActivity")) {
                            Search_date_detailActivity activity = (Search_date_detailActivity) mContext;
                            activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 0);
                        }
                        if (mContext.getClass().getName().equals("com.kits.company.activity.GrpActivity")) {
                            GrpActivity activity = (GrpActivity) mContext;
                            activity.good_select_function(goodView.getGoodFieldValue("SellPrice"), Integer.parseInt(goodView.getGoodFieldValue("GoodCode")), goodView.getGoodFieldValue("GoodName"), 0);
                        }

                    }

                }
                return true;
            }


        });



    }

    @Override
    public int getItemCount()
    {
        if(shPref.getBoolean("available_good", true)){
            return available_goods.size();
        }else{
            return all_goods.size();
        }
    }

    class GoodViewHolder extends RecyclerView.ViewHolder
    {
        private TextView goodnameTextView;
        private TextView maxsellpriceTextView;
        private TextView sellpercent;
        private Button btnadd;
        private ImageView img ;
        private LinearLayoutCompat ggg ;
        MaterialCardView rltv;

        GoodViewHolder(View itemView)
        {
            super(itemView);
            goodnameTextView = itemView.findViewById(R.id.good_prosearch_name);
            maxsellpriceTextView = itemView.findViewById(R.id.good_prosearch_price);
            sellpercent = itemView.findViewById(R.id.good_prosearch_percent);
            img =  itemView.findViewById(R.id.good_prosearch_img) ;
            rltv =  itemView.findViewById(R.id.good_prosearch);
            btnadd = itemView.findViewById(R.id.good_prosearch_btn);
            ggg = itemView.findViewById(R.id.proserch_ggg);
        }
    }
    public void onDestroy() {


    }

}
