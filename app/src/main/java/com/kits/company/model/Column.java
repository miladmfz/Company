package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class Column {


    @SerializedName("GoodType")
    private String GoodType;

    @SerializedName("ColumnName")
    private String ColumnName;

    @SerializedName("ColumnDesc")
    private String ColumnDesc;

    @SerializedName("DetailVisible")
    private Integer DetailVisible;


    public String getGoodType() {
        return GoodType;
    }

    public void setGoodType(String goodType) {
        GoodType = goodType;
    }

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

    public String getColumnDesc() {
        return ColumnDesc;
    }

    public void setColumnDesc(String columnDesc) {
        ColumnDesc = columnDesc;
    }

    public Integer getDetailVisible() {
        return DetailVisible;
    }

    public void setDetailVisible(Integer detailVisible) {
        DetailVisible = detailVisible;
    }
}
