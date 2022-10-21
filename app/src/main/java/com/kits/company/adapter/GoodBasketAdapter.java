package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.kits.company.activity.BasketActivity;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GoodBasketAdapter extends RecyclerView.Adapter<GoodBasketAdapter.GoodViewHolder> {
    private final DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private final APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public Call<RetrofitResponse> call2;
    private final ArrayList<Good> Goods;
    private int amount = 0;
    BuyBox buyBox;
    private final Context mContext;

    public GoodBasketAdapter(ArrayList<Good> Goods, Context mContext) {
        this.mContext=mContext;
        this.Goods = Goods;
        this.buyBox=new BuyBox(mContext);
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(GetShared.ReadString("AppBasketItem").equals("one")){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.good_buy_one, parent, false);
        }else if(GetShared.ReadString("AppBasketItem").equals("two")){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.good_buy_two, parent, false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_buy_one, parent, false);
        }

        return new GoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        

        long totalbuy=0;
        holder.img.setVisibility(View.INVISIBLE);

        holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(Goods.get(position).getGoodFieldValue("GoodName")));
        holder.amount.setText(NumberFunctions.PerisanNumber(Goods.get(position).getGoodFieldValue("FacAmount")));
        holder.priceTextView.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(Goods.get(position).getGoodFieldValue("SellPrice")))));

        holder.maxsellpriceTextView.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(Goods.get(position).getGoodFieldValue("MaxSellPrice")))));
        totalbuy= (long) (Float.parseFloat(Goods.get(position).getGoodFieldValue("SellPrice"))* Float.parseFloat(Goods.get(position).getGoodFieldValue("FacAmount"))*Float.parseFloat(Goods.get(position).getGoodFieldValue("bRatio")));
        holder.total.setText(NumberFunctions.PerisanNumber(decimalFormat.format(totalbuy)));
        holder.good_buy_NotReserved.setText(Goods.get(position).getGoodFieldValue("NotReserved"));
        holder.good_buy_unit.setText(NumberFunctions.PerisanNumber(Goods.get(position).getGoodFieldValue("bUnitName")+"("+Goods.get(position).getGoodFieldValue("bRatio")+ ")" ));
        holder.offer.setText(NumberFunctions.PerisanNumber((100 - ((Integer.parseInt(Goods.get(position).getGoodFieldValue("SellPrice"))* 100) / Integer.parseInt(Goods.get(position).getGoodFieldValue("MaxSellPrice")))) + " درصد تخفیف "));


        if(Integer.parseInt(Goods.get(position).getGoodFieldValue("NotReserved"))>0){
            holder.good_buy_IsReserved.setVisibility(View.VISIBLE);

        }else{
            holder.good_buy_IsReserved.setVisibility(View.GONE);
        }


        call2 = apiInterface_image.GetImage(
                "getImage",
                Goods.get(position).getGoodFieldValue("GoodCode"),
                "0",
                "120"
        );
        call2.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitResponse> call2, @NonNull Response<RetrofitResponse> response) {
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
                        holder.img.setVisibility(View.VISIBLE);

                    }else {
                        Glide.with(holder.img)
                                .asBitmap()
                                .load(R.drawable.no_photo)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .fitCenter()
                                .into(holder.img);
                        holder.img.setVisibility(View.VISIBLE);

                    }

                } catch (Exception e) {
                    e.getMessage();
                }

                }
            }
            @Override
            public void onFailure(@NonNull Call<RetrofitResponse> call2, @NonNull Throwable t) {
            }
        });



        holder.btndlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("توجه")
                        .setMessage("آیا کالا از لیست حذف گردد؟")
                        .setPositiveButton("بله", (dialogInterface, i) -> {

                            buyBox.deletegoodfrombasket(Goods.get(position).getGoodFieldValue("GoodCode"));
                            Intent bag = new Intent(mContext, BasketActivity.class);
                            ((Activity) mContext).finish();
                            ((Activity) mContext).overridePendingTransition(0, 0);
                            mContext.startActivity(bag);
                            ((Activity) mContext).overridePendingTransition(0, 0);

                        })
                        .setNegativeButton("خیر", (dialogInterface, i) -> {

                        })
                        .show();


            }
        });


        holder.amount.setOnClickListener(view -> buyBox.TestDialog(Goods.get(position),position,"1"));


        holder.pluse.setOnClickListener(view -> buyBox.basketdsolo(
                Goods.get(position),
                String.valueOf(Float.parseFloat(Goods.get(position).getGoodFieldValue("FacAmount"))+1),
                position));


        holder.minus.setOnClickListener(view -> {
            amount=Integer.parseInt(Goods.get(position).getGoodFieldValue("FacAmount"));
            if(Integer.parseInt(Goods.get(position).getGoodFieldValue("FacAmount"))<2){
                App.showToast( "برای حذف کالا از دکمه ی حذف استفاده کنید");
            }else {
                buyBox.basketdsolo(
                        Goods.get(position),
                        String.valueOf(Float.parseFloat(Goods.get(position).getGoodFieldValue("FacAmount"))-1),
                        position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Goods.size();
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView goodnameTextView;
        private final TextView maxsellpriceTextView;
        private final TextView priceTextView;
        private final TextView total;
        private final TextView amount;
        private final TextView good_buy_NotReserved;
        private final LinearLayoutCompat good_buy_IsReserved;
        private final TextView good_buy_unit;

        private final TextView offer;
        private final Button btndlt;
        private final ImageView pluse;
        private final ImageView minus;
        private final ImageView img;
        MaterialCardView rltv;

        GoodViewHolder(View itemView) {
            super(itemView);
            goodnameTextView = itemView.findViewById(R.id.good_buy_name);
            maxsellpriceTextView = itemView.findViewById(R.id.good_buy_maxprice);
            priceTextView = itemView.findViewById(R.id.good_buy_price);
            amount = itemView.findViewById(R.id.good_buy_amount);
            good_buy_NotReserved = itemView.findViewById(R.id.good_buy_NotReserved);
            good_buy_IsReserved = itemView.findViewById(R.id.good_buy_IsReserved);
            total = itemView.findViewById(R.id.good_buy_total);
            img = itemView.findViewById(R.id.good_buy_img);
            btndlt = itemView.findViewById(R.id.good_buy_btndlt);
            offer = itemView.findViewById(R.id.good_buy_offer);
            pluse = itemView.findViewById(R.id.good_buy_pluse);
            minus = itemView.findViewById(R.id.good_buy_minus);
            good_buy_unit = itemView.findViewById(R.id.good_buy_unit);
            rltv = itemView.findViewById(R.id.good_buy);
        }
    }


}
