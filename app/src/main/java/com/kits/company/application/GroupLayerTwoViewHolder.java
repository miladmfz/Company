 package com.kits.company.application;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.kits.company.R;
import com.kits.company.activity.SearchActivity;
import com.kits.company.model.GroupLayerTwo;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

 public class GroupLayerTwoViewHolder extends ChildViewHolder {
     private final TextView mtextView;

     public GroupLayerTwoViewHolder(View itemView) {
         super(itemView);
         mtextView=itemView.findViewById(R.id.item2_tv);
     }

     public void bind(GroupLayerTwo product){
         mtextView.setText(product.name);
     }

     public void intent(final GroupLayerTwo product, final  Context mContext){

         mtextView.setOnClickListener(v -> {

             Intent intent = new Intent(mContext, SearchActivity.class);
             intent.putExtra("id", product.id);
             intent.putExtra("title",product.name);
             intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             App.getContext().startActivity(intent);

         });
     }

 }
