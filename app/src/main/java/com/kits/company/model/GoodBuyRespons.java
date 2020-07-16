package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodBuyRespons {

    @SerializedName("Goodsbuy")
    private ArrayList<GoodBuy> Goodsbuy;
    @SerializedName("arrayGoodsbuy_name")
    private ArrayList<GoodBuy> arrayGoodsbuy_name;

    public ArrayList<GoodBuy> getGoodsbuy() {
        return Goodsbuy;
    }

    public void setGoodsbuy(ArrayList<GoodBuy> goodsbuy) {
        this.Goodsbuy = goodsbuy;
    }

}
