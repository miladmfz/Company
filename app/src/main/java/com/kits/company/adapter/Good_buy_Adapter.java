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
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.kits.company.R;
import com.kits.company.activity.BuyActivity;
import com.kits.company.activity.DetailActivity;
import com.kits.company.activity.GrpActivity;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Good_buy_Adapter extends RecyclerView.Adapter<Good_buy_Adapter.GoodViewHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    public Call<String> call2;
    private byte[] imageByteArray;
    private Context mContext;
    private ArrayList<GoodBuy> goodbuys;
    private String SERVER_IP_ADDRESS;
    private int amount = 0;
    private Intent intent;

    public Good_buy_Adapter(ArrayList<GoodBuy> goodbuys, Context mContext) {
        this.mContext = mContext;
        this.goodbuys = goodbuys;
        SERVER_IP_ADDRESS = mContext.getString(R.string.SERVERIP);
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.good_buy, parent, false);
        return new GoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, final int position) {

        final int myps= position;
        final GoodBuy goodbuyView = goodbuys.get(position);

        holder.img.setVisibility(View.INVISIBLE);

        holder.goodnameTextView.setText(Farsi_number.PerisanNumber(goodbuyView.getGoodBuyFieldValue("GoodName")));
        holder.amount.setText(Farsi_number.PerisanNumber(goodbuyView.getGoodBuyFieldValue("FacAmount")));
        holder.priceTextView.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuyView.getGoodBuyFieldValue("SellPrice")))));

        holder.maxsellpriceTextView.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuyView.getGoodBuyFieldValue("MaxSellPrice")))));
        holder.total.setText(Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(goodbuyView.getGoodBuyFieldValue("SellPrice"))*Integer.parseInt(goodbuyView.getGoodBuyFieldValue("FacAmount")))));
        holder.offer.setText(Farsi_number.PerisanNumber((100 - ((Integer.parseInt(goodbuyView.getGoodBuyFieldValue("SellPrice"))* 100) / Integer.parseInt(goodbuyView.getGoodBuyFieldValue("MaxSellPrice")))) + " درصد تخفیف "));
        holder.good_buy_NotReserved.setText(goodbuyView.getGoodBuyFieldValue("NotReserved"));

        if (goodbuyView.getGoodBuyFieldValue("IsReserved").equals("1")) {
            holder.good_buy_IsReserved.setVisibility(View.GONE);
        } else {
            if(Integer.parseInt(goodbuyView.getGoodBuyFieldValue("NotReserved"))>0){
                holder.good_buy_IsReserved.setVisibility(View.VISIBLE);
            }else{
                holder.good_buy_IsReserved.setVisibility(View.GONE);
            }
        }





        call2 = apiInterface_image.GetImage("getImage",goodbuyView.getGoodBuyFieldValue("GoodCode"),0,120);
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
            public void onFailure(Call<String> call2, Throwable t) {
                Log.e("onFailure",""+t.toString());
            }
        });



        holder.btndlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("توجه")
                        .setMessage("آیا کالا از لیست حذف گردد؟")
                        .setPositiveButton("بله", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Buy_box buy_box = new Buy_box(mContext);
                                buy_box.deletegoodfrombasket(Integer.parseInt(goodbuyView.getGoodBuyFieldValue("GoodCode")));
                                Intent bag = new Intent(mContext, BuyActivity.class);
                                ((Activity) mContext).finish();
                                ((Activity) mContext).overridePendingTransition(0, 0);
                                mContext.startActivity(bag);
                                ((Activity) mContext).overridePendingTransition(0, 0);

                            }
                        })
                        .setNegativeButton("خیر", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();


            }
        });


        holder.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buy_box buy_box = new Buy_box(mContext);
                buy_box.basketdialog(goodbuyView.getGoodBuyFieldValue("GoodName"),goodbuyView.getGoodBuyFieldValue("Price"),Integer.parseInt(goodbuyView.getGoodBuyFieldValue("GoodCode")),Integer.parseInt(goodbuyView.getGoodBuyFieldValue("FacAmount")),position);

            }
        });


        holder.pluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount=Integer.parseInt(goodbuyView.getGoodBuyFieldValue("FacAmount"));
                Buy_box buy_box = new Buy_box(mContext);
                buy_box.basketdsolo(goodbuyView.getGoodBuyFieldValue("Price"),Integer.parseInt(goodbuyView.getGoodBuyFieldValue("GoodCode")),amount+1,position);
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount=Integer.parseInt(goodbuyView.getGoodBuyFieldValue("FacAmount"));
                if(amount==1){
                    Toast.makeText(mContext, "برای حذف کالا از دکمه ی حذف استفاده کنید", Toast.LENGTH_SHORT).show();

                }else {
                    Buy_box buy_box = new Buy_box(mContext);
                    buy_box.basketdsolo(goodbuyView.getGoodBuyFieldValue("Price"),Integer.parseInt(goodbuyView.getGoodBuyFieldValue("GoodCode")),amount-1,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return goodbuys.size();
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {
        private TextView goodnameTextView;
        private TextView maxsellpriceTextView;
        private TextView priceTextView;
        private TextView total;
        private TextView amount;
        private TextView good_buy_NotReserved;
        private LinearLayoutCompat good_buy_IsReserved;
        private TextView offer;
        private Button btndlt;
        private ImageView pluse;
        private ImageView minus;
        private ImageView img;
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


            rltv = itemView.findViewById(R.id.good_buy);
        }
    }


}
