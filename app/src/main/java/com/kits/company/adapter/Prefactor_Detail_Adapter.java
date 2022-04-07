package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
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
import com.kits.company.application.App;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.PreFactor;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;
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

        holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(preFactorview.getPreFactorFieldValue("GoodName")));
        holder.maxsellpriceTextView.setText(NumberFunctions.PerisanNumber(preFactorview.getPreFactorFieldValue("MaxSellPrice")));
        holder.priceTextView.setText(NumberFunctions.PerisanNumber(preFactorview.getPreFactorFieldValue("Price")));
        holder.total.setText(NumberFunctions.PerisanNumber(String.valueOf(Integer.parseInt(preFactorview.getPreFactorFieldValue("FacAmount"))*Integer.parseInt(preFactorview.getPreFactorFieldValue("Price")))));
        holder.amount.setText(NumberFunctions.PerisanNumber(preFactorview.getPreFactorFieldValue("FacAmount")));

        if(preFactorview.getPreFactorFieldValue("IsReserved").equals("1")){
            holder.good_ReservedRow.setText("در انبار موجود می باشد");
            holder.good_ReservedRow.setTextColor(App.getContext().getResources().getColor(R.color.green_900));

        }else{
            holder.good_ReservedRow.setText("مورد سفارش قرار گرفت");
            holder.good_ReservedRow.setTextColor(App.getContext().getResources().getColor(R.color.red_300));

        }


        call2 = apiInterface_image.GetImage(
                "getImage",
                preFactorview.getPreFactorFieldValue("GoodCode"),
                "0",
                "110"
        );
        call2.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(Call<RetrofitResponse> call2, Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    try {
                    if(!response.body().getText().equals("no_photo")) {
                        Glide.with(holder.img)
                                .asBitmap()
                                .load(Base64.decode(response.body().getText(), Base64.DEFAULT))
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
            public void onFailure(Call<RetrofitResponse> call2, Throwable t) {
                Log.e("onFailure",""+t.toString());
            }
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
