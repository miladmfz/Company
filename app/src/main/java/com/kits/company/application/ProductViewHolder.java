package com.kits.company.application;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.kits.company.R;
import com.kits.company.activity.GrpActivity;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class ProductViewHolder extends ChildViewHolder {
    private final TextView mtextView;

    public ProductViewHolder(View itemView) {
        super(itemView);
        mtextView=itemView.findViewById(R.id.item2_tv);
    }

    public void bind(Product product){
        mtextView.setText(product.name);
    }

    public void intent(final Product product,final  Context mContext){

        mtextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, GrpActivity.class);
                intent.putExtra("id", product.id);
                intent.putExtra("title",product.name);
                App.getContext().startActivity(intent);

            }
        });
    }

}
