package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class Column {


    @SerializedName("GoodType")
    private String GoodType;
    @SerializedName("ColumnName")
    private String ColumnName;
    @SerializedName("ColumnDesc")
    private String ColumnDesc;
    @SerializedName("ColumnDefinition")
    private String ColumnDefinition;
    @SerializedName("Search")
    private String Search;
    @SerializedName("SortOrder")
    private String SortOrder;
    @SerializedName("IsDefault")
    private String IsDefault;


    public void setSearch(String search) {
        Search = search;
    }

    public void setGoodType(String goodType) {
        GoodType = goodType;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }

    public String getColumnFieldValue(String AKey) {
        String iKey = AKey.toLowerCase();
        String Res = "";
        switch (iKey) {
            case "columnname":
                Res = ColumnName;
                break;
            case "goodtype":
                if (GoodType == null) Res = "";
                else Res = GoodType;
                break;
            case "columndesc":
                if (ColumnDesc == null) Res = "";
                else Res = ColumnDesc;
                break;
            case "columndefinition":
                if (ColumnDefinition == null) Res = "";
                else Res = ColumnDefinition;
                break;
            case "search":
                if (Search == null) Res = "";
                else Res = Search;
                break;
            case "sortorder":
                if (SortOrder == null) Res = "";
                else Res = SortOrder;
                break;
            case "isdefault":
                if (IsDefault == null) Res = "";
                else Res = IsDefault;
                break;
        }
        return Res;
    }

}
