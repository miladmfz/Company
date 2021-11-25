package com.kits.company.application;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.kits.company.R;
import com.kits.company.activity.GrpActivity;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static android.view.animation.Animation.RELATIVE_TO_SELF;


public class CategoryViewHolder extends GroupViewHolder {

    private final TextView textView ;
    private final ImageView arrow;


    public CategoryViewHolder(View itemView) {
        super(itemView);
        textView=itemView.findViewById(R.id.item1_tv);
        arrow=itemView.findViewById(R.id.item1_img);
    }

    public void bind (Category company){
        textView.setText(company.getTitle());

    }

    public void hide (Category company){
        if(company.childno>0) {
            arrow.setVisibility(View.VISIBLE);
        }else {
            arrow.setVisibility(View.GONE);
        }
    }



    public void intent(final Category company,final  Context mContext){

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, GrpActivity.class);
                intent.putExtra("id", company.id);
                intent.putExtra("title",company.name);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            App.getContext().startActivity(intent);

            }
        });
    }


    @Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

}
