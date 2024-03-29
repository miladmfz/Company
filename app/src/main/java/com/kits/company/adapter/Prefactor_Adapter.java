package com.kits.company.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.kits.company.R;
import com.kits.company.activity.BasketHistoryDetialActivity;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.PreFactor;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class Prefactor_Adapter extends RecyclerView.Adapter<Prefactor_Adapter.GoodViewHolder> {
    private final DecimalFormat decimalFormat = new DecimalFormat("0,000");
    private final APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    private final ArrayList<PreFactor> preFactors;
    BuyBox buyBox;
    private final Context mContext;
    private Intent intent;

    public Prefactor_Adapter(ArrayList<PreFactor> preFactors, Context context) {
        this.mContext=context;
        this.preFactors = preFactors;
        this.buyBox=new BuyBox(mContext);
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


        holder.factordate.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("PreFactorDate")));
        holder.factorcode.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("PreFactorCode")));
        holder.factorrow.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("RowsCount")));
        holder.factorsumamount.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("SumAmount")));
        holder.factorprice.setText(NumberFunctions.PerisanNumber(preFactors.get(position).getPreFactorFieldValue("SumPrice")));

        holder.rltv.setOnClickListener(v -> {


            intent = new Intent(mContext, BasketHistoryDetialActivity.class);
            intent.putExtra("id", preFactors.get(position).getPreFactorFieldValue("PreFactorCode"));
            intent.putExtra("ReservedRows", "2");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            mContext.startActivity(intent);
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
