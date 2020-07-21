package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class Column {


    @SerializedName("GoodType")
    private String GoodType;

    @SerializedName("ColumnName")
    private String ColumnName;

    @SerializedName("ColumnDesc")
    private String ColumnDesc;

    @SerializedName("SortOrder")
    private Integer SortOrder;



    public String getColumnFieldValue(String AKey) {
        String iKey = AKey.toLowerCase();
        String Res = "";
        if (iKey.equals("columnname")) { Res = ColumnName;}
        else if (iKey.equals("goodtype")) { if (GoodType == null) Res = "";else Res = GoodType; }
        else if (iKey.equals("columndesc")) { if (ColumnDesc == null) Res = "";else Res = ColumnDesc; }
        else if (iKey.equals("sortorder")) { if (SortOrder == null) Res = "";else Res = SortOrder.toString(); }
        return Res;
    }

}
