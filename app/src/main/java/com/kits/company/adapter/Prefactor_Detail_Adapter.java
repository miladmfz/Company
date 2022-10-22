package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.kits.company.model.PreFactor;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Prefactor_Detail_Adapter extends RecyclerView.Adapter<Prefactor_Detail_Adapter.GoodViewHolder> {

    private final APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    private Context mContext;
    private final ArrayList<PreFactor> preFactors;
    private String SERVER_IP_ADDRESS;
    public Call<RetrofitResponse> call2;
    public Prefactor_Detail_Adapter(ArrayList<PreFactor> preFactors, Context mContext) {
        this.preFactors = preFactors;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_buy_history, parent, false);
        return new GoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("GoodName")));
        holder.maxsellpriceTextView.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("MaxSellPrice")));
        holder.priceTextView.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("Price")));
        holder.total.setText(NumberFunctions.PerisanNumber(
                String.valueOf(
                        Float.parseFloat(preFactors.get(position).getPreFactorFieldValue("FacAmount"))
                                *Float.parseFloat(preFactors.get(position).getPreFactorFieldValue("Price"))
                )
        ));
        holder.amount.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("FacAmount")));

        if(preFactors.get(position).getPreFactorFieldValue("IsReserved").equals("1")){
            holder.good_ReservedRow.setText("در انبار موجود می باشد");
            holder.good_ReservedRow.setTextColor(App.getContext().getResources().getColor(R.color.green_900));

        }else{
            holder.good_ReservedRow.setText("مورد سفارش قرار گرفت");
            holder.good_ReservedRow.setTextColor(App.getContext().getResources().getColor(R.color.red_300));

        }

        if (!preFactors.get(position).getPreFactorFieldValue("GoodImageName").equals("")) {

            Glide.with(holder.img)
                    .asBitmap()
                    .load(R.drawable.white)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .fitCenter()
                    .into(holder.img);
            holder.img.setVisibility(View.VISIBLE);

            Glide.with(holder.img)
                    .asBitmap()
                    .load(Base64.decode(preFactors.get(position).getPreFactorFieldValue("GoodImageName"), Base64.DEFAULT))
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
                    preFactors.get(position).getPreFactorFieldValue("GoodCode"),
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
                            preFactors.get(position).setGoodImageName(response.body().getText());
                        } else {
                            preFactors.get(position).setGoodImageName(String.valueOf(R.string.no_photo));

                        }
                        notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RetrofitResponse> call2, @NonNull Throwable t) {
                }
            });
        }

        holder.rltv.setOnClickListener(view -> {

            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("id", Integer.parseInt(preFactors.get(position).getPreFactorFieldValue("GoodCode")));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return preFactors.size();
    }
    class GoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView goodnameTextView;
        private final TextView good_ReservedRow;
        private final TextView maxsellpriceTextView;
        private final TextView priceTextView;
        private final TextView total;
        private final TextView amount;
        private final ImageView img;
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
