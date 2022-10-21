package com.kits.company.adapter;

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
import com.kits.company.activity.SearchActivity;
import com.kits.company.application.App;
import com.kits.company.model.GoodGroup;

import java.util.ArrayList;

public class GrpAdapter extends RecyclerView.Adapter<GrpAdapter.GoodGroupViewHolder>{

    private final ArrayList<GoodGroup> GoodGroups;
    private Intent intent;
    Context mContext;


    public GrpAdapter(ArrayList<GoodGroup> GoodGroups, Context mContext)
    {
        this.GoodGroups = GoodGroups;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public GoodGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grp_v_list_detail, parent, false);
        return new GoodGroupViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GoodGroupViewHolder holder, int position)
    {



        holder.grpname.setText(GoodGroups.get(position).getGoodGroupFieldValue("Name"));
        holder.rltv.setBackgroundResource(R.drawable.bg_round_red);

        holder.grpname.setOnClickListener(v -> {

            intent = new Intent(mContext, SearchActivity.class);
            intent.putExtra("id", Integer.parseInt(GoodGroups.get(position).getGoodGroupFieldValue("groupcode")));
            intent.putExtra("title",GoodGroups.get(position).getGoodGroupFieldValue("Name"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            App.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount()
    {
        return GoodGroups.size();
    }

    class GoodGroupViewHolder extends RecyclerView.ViewHolder
    {
        private final TextView grpname;
        MaterialCardView rltv;
        GoodGroupViewHolder(View itemView)
        {
            super(itemView);
            grpname = itemView.findViewById(R.id.grp_vlist_detail_name);
            rltv =  itemView.findViewById(R.id.grp_vlist_detail);
        }
    }
}
