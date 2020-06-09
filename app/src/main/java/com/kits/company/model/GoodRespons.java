package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodRespons {

    @SerializedName("Goods")
    private ArrayList<Good> Goods;

    @SerializedName("value")
    private  String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ArrayList<Good> getGoods() {
        return Goods;
    }

    public void setGoods(ArrayList<Good> goods) {
        this.Goods = goods;
    }
}
