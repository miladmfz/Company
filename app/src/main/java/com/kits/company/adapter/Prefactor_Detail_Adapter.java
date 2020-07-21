package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.internal.$Gson$Preconditions;
import com.kits.company.R;
import com.kits.company.activity.BuyActivity;
import com.kits.company.activity.BuyhistoryActivity;
import com.kits.company.activity.BuyhistoryDetialActivity;
import com.kits.company.activity.DetailActivity;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.PreFactor;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Prefactor_Detail_Adapter extends RecyclerView.Adapter<Prefactor_Detail_Adapter.GoodViewHolder> {

    private APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    private Context mContext;
    private ArrayList<PreFactor> preFactors;
    private String SERVER_IP_ADDRESS;
    public Call<String> call2;
    public Prefactor_Detail_Adapter(ArrayList<PreFactor> preFactors, Context mContext) {
        this.mContext = mContext;
        this.preFactors = preFactors;
        SERVER_IP_ADDRESS = mContext.getString(R.string.SERVERIP);
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_buy_history, parent, false);
        return new GoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, int position) {
        final PreFactor preFactorview = preFactors.get(position);

        holder.goodnameTextView.setText(Farsi_number.PerisanNumber(preFactorview.getPreFactorFieldValue("GoodName")));
        holder.maxsellpriceTextView.setText(Farsi_number.PerisanNumber(preFactorview.getPreFactorFieldValue("MaxSellPrice")));
        holder.priceTextView.setText(Farsi_number.PerisanNumber(preFactorview.getPreFactorFieldValue("Price")));
        holder.total.setText(Farsi_number.PerisanNumber(String.valueOf(Integer.parseInt(preFactorview.getPreFactorFieldValue("FacAmount"))*Integer.parseInt(preFactorview.getPreFactorFieldValue("Price")))));
        holder.amount.setText(Farsi_number.PerisanNumber(preFactorview.getPreFactorFieldValue("FacAmount")));

        if(preFactorview.getPreFactorFieldValue("IsReserved").equals("1")){
            holder.good_ReservedRow.setText("در انبار موجود می باشد");
            holder.good_ReservedRow.setTextColor(mContext.getResources().getColor(R.color.green_900));

        }else{
            holder.good_ReservedRow.setText("مورد سفارش قرار گرفت");
            holder.good_ReservedRow.setTextColor(mContext.getResources().getColor(R.color.red_300));

        }


        call2 = apiInterface_image.GetImage("getImage",preFactorview.getPreFactorFieldValue("GoodCode"),0,110);
        call2.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call2, Response<String> response) {
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    try {
                    if(!response.body().equals("no_photo")) {
                        Glide.with(holder.img)
                                .asBitmap()
                                .load(Base64.decode(response.body(), Base64.DEFAULT))
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .fitCenter()
                                .into(holder.img);
                    }else {
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

    @Override
    public int getItemCount() {
        return preFactors.size();
    }
    class GoodViewHolder extends RecyclerView.ViewHolder {
        private TextView goodnameTextView;
        private TextView good_ReservedRow;
        private TextView maxsellpriceTextView;
        private TextView priceTextView;
        private TextView total;
        private TextView amount;
        private ImageView img;
        MaterialCardView rltv;

        GoodViewHolder(View itemView) {
            super(itemView);
            goodnameTextView = itemView.findViewById(R.id.good_buy_history_name);
            good_ReservedRow = itemView.findViewById(R.id.good_buy_history_ReservedRow);
            amount = itemView.findViewById(R.id.good_buy_history_amount);
            priceTextView = itemView.findViewById(R.id.good_buy_history_price);
            total = itemView.findViewById(R.id.good_buy_history_total);
            maxsellpriceTextView = itemView.findViewById(R.id.good_buy_history_maxprice);
            img = itemView.findViewById(R.id.good_buy_history_img);
            rltv = itemView.findViewById(R.id.good_buy_history);
        }
    }


}
