package com.kits.company.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kits.company.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class ProductAdapter extends ExpandableRecyclerViewAdapter<CategoryViewHolder,ProductViewHolder> {

    Context mContext;

    public ProductAdapter(List<? extends ExpandableGroup> groups, Context mContext) {
        super(groups);
        this.mContext=mContext;
    }

    @Override
    public CategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public ProductViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(ProductViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final Product product = (Product) group.getItems().get(childIndex);
        holder.bind(product);
        holder.intent(product,App.getContext());


    }

    @Override
    public void onBindGroupViewHolder(CategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Category company = (Category) group;
        holder.bind(company);
        holder.intent(company, App.getContext());
        holder.hide(company);

    }
}
