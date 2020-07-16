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


public class Prefactor_Adapter extends RecyclerView.Adapter<Prefactor_Adapter.GoodViewHolder> {
    private DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    private Context mContext;
    private ArrayList<PreFactor> preFactors;
    private String SERVER_IP_ADDRESS;
    private int amount = 0;
    private Intent intent;

    public Prefactor_Adapter(ArrayList<PreFactor> preFactors, Context mContext) {
        this.mContext = mContext;
        this.preFactors = preFactors;
        SERVER_IP_ADDRESS = mContext.getString(R.string.SERVERIP);
    }

    @NonNull
    @Override
    public GoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prefactor_header, parent, false);
        return new GoodViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final GoodViewHolder holder, int position) {
        final int myps= position;
        final PreFactor preFactorview = preFactors.get(position);

        holder.factordate.setText(Farsi_number.PerisanNumber(preFactorview.getPreFactorDate()));
        holder.factorcode.setText(Farsi_number.PerisanNumber(preFactorview.getPreFactorCode()));
        holder.factorrow.setText(Farsi_number.PerisanNumber(preFactorview.getRowsCount()));
        holder.factorsumamount.setText(Farsi_number.PerisanNumber(preFactorview.getSumAmount()));
        holder.factorprice.setText(Farsi_number.PerisanNumber(preFactorview.getSumPrice()));

        holder.rltv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                intent = new Intent(mContext, BuyhistoryDetialActivity.class);
                intent.putExtra("id", preFactorview.getPreFactorCode());
                intent.putExtra("ReservedRows", "2");
                mContext.startActivity(intent);
            }

        });


    }

    @Override
    public int getItemCount() {
        return preFactors.size();
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {

        TextView factordate;
        TextView factorcode;
        TextView factorrow;
        TextView factorsumamount;
        TextView factorprice;
        MaterialCardView rltv;

        GoodViewHolder(View itemView) {
            super(itemView);


            factordate = itemView.findViewById(R.id.pf_header_box_date);
            factorcode = itemView.findViewById(R.id.pf_header_box_code);
            factorrow = itemView.findViewById(R.id.pf_header_box_row);
            factorsumamount = itemView.findViewById(R.id.pf_header_box_count);
            factorprice = itemView.findViewById(R.id.pf_header_box_price);
            rltv = itemView.findViewById(R.id.pf_header);
        }
    }


}
