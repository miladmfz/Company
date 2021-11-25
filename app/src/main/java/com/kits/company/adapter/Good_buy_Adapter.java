package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.kits.company.activity.BuyActivity;
import com.kits.company.activity.DetailActivity;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitRespons;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Good_buy_Adapter extends RecyclerView.Adapter<Good_buy_Adapter.GoodViewHolder> {
    private final DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private final APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public Call<RetrofitRespons> call2;
    private final ArrayList<Good> Goods;
    private int amount = 0;
    BuyBox buyBox;
    private final Context mContext;

    public Good_buy_Adapter(ArrayList<Good> Goods, Context mContext) {
        this.mContext=mContext;
        this.Goods = Goods;
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
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, final int position) {

        buyBox=new BuyBox(mContext);
        final Good GoodView = Goods.get(position);

        long totalbuy=0;
        holder.img.setVisibility(View.INVISIBLE);

        holder.goodnameTextView.setText(NumberFunctions.PerisanNumber(GoodView.getGoodFieldValue("GoodName")));
        holder.amount.setText(NumberFunctions.PerisanNumber(GoodView.getGoodFieldValue("FacAmount")));
        holder.priceTextView.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(GoodView.getGoodFieldValue("SellPrice")))));

        holder.maxsellpriceTextView.setText(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(GoodView.getGoodFieldValue("MaxSellPrice")))));
        totalbuy= (long) (Float.parseFloat(GoodView.getGoodFieldValue("SellPrice"))* Float.parseFloat(GoodView.getGoodFieldValue("FacAmount"))*Float.parseFloat(GoodView.getGoodFieldValue("bRatio")));
        holder.total.setText(NumberFunctions.PerisanNumber(decimalFormat.format(totalbuy)));
        holder.good_buy_NotReserved.setText(GoodView.getGoodFieldValue("NotReserved"));
        holder.good_buy_unit.setText(NumberFunctions.PerisanNumber(GoodView.getGoodFieldValue("bUnitName")+"("+GoodView.getGoodFieldValue("bRatio")+ ")" ));
        holder.offer.setText(NumberFunctions.PerisanNumber((100 - ((Integer.parseInt(GoodView.getGoodFieldValue("SellPrice"))* 100) / Integer.parseInt(GoodView.getGoodFieldValue("MaxSellPrice")))) + " درصد تخفیف "));


        if (GoodView.getGoodFieldValue("IsReserved").equals("1")) {
            if(Integer.parseInt(GoodView.getGoodFieldValue("NotReserved"))>0){
                holder.good_buy_IsReserved.setVisibility(View.VISIBLE);

            }else{
                holder.good_buy_IsReserved.setVisibility(View.GONE);
            }
        } else {
            if(Integer.parseInt(GoodView.getGoodFieldValue("NotReserved"))>0){
                holder.good_buy_IsReserved.setVisibility(View.VISIBLE);
            }else{
                holder.good_buy_IsReserved.setVisibility(View.GONE);
            }
        }





        call2 = apiInterface_image.GetImage(
                "getImage",
                GoodView.getGoodFieldValue("GoodCode"),
                "0",
                "120"
        );
        call2.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call2, Response<RetrofitRespons> response) {
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
            public void onFailure(Call<RetrofitRespons> call2, Throwable t) {
                Log.e("onFailure",""+t.toString());
            }
        });



        holder.btndlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("توجه")
                        .setMessage("آیا کالا از لیست حذف گردد؟")
                        .setPositiveButton("بله", (dialogInterface, i) -> {

                            buyBox.deletegoodfrombasket(GoodView.getGoodFieldValue("GoodCode"));
                            Intent bag = new Intent(App.getContext(), BuyActivity.class);
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


        holder.amount.setOnClickListener(view -> buyBox.basketdialog(GoodView,position));


        holder.pluse.setOnClickListener(view -> buyBox.basketdsolo(
                GoodView,
                String.valueOf(Float.parseFloat(GoodView.getGoodFieldValue("FacAmount"))+1),
                position));


        holder.minus.setOnClickListener(view -> {
            amount=Integer.parseInt(GoodView.getGoodFieldValue("FacAmount"));
            if(Integer.parseInt(GoodView.getGoodFieldValue("FacAmount"))<2){
                App.showToast( "برای حذف کالا از دکمه ی حذف استفاده کنید");
            }else {
                buyBox.basketdsolo(
                        GoodView,
                        String.valueOf(Float.parseFloat(GoodView.getGoodFieldValue("FacAmount"))-1),
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
