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
        if (iKey.equals("columnname")) { Res = ColumnName;}
        else if (iKey.equals("goodtype")) { if (GoodType == null) Res = "";else Res = GoodType; }
        else if (iKey.equals("columndesc")) { if (ColumnDesc == null) Res = "";else Res = ColumnDesc; }
        else if (iKey.equals("columndefinition")) { if (ColumnDefinition == null) Res = "";else Res = ColumnDefinition; }
        else if (iKey.equals("search")) { if (Search == null) Res = "";else Res = Search; }
        else if (iKey.equals("sortorder")) { if (SortOrder == null) Res = "";else Res = SortOrder; }
        else if (iKey.equals("isdefault")) { if (IsDefault == null) Res = "";else Res = IsDefault; }
        return Res;
    }

}
