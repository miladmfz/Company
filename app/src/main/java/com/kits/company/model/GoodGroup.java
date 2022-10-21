package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class GoodGroup {



    @SerializedName("GroupCode")
    private String GroupCode;
    @SerializedName("Name")
    private String Name;
    @SerializedName("L1")
    private String L1;
    @SerializedName("L2")
    private String L2;
    @SerializedName("L3")
    private String L3;
    @SerializedName("L4")
    private String L4;
    @SerializedName("L5")
    private String L5;
    @SerializedName("ChildNo")
    private String ChildNo;
    @SerializedName("ErrCode")
    private String ErrCode;
    @SerializedName("ErrDesc")
    private String ErrDesc;

    public void setName(String name) {Name = name;}

    public String getGoodGroupFieldValue(String AKey) {

        String iKey = AKey.toLowerCase();
        String Res = "";
        switch (iKey) {
            case "name":
                Res = Name;
                break;
            case "groupcode":
                if (GroupCode == null) Res = "";
                else Res = GroupCode;
                break;
            case "l1":
                if (L1 == null) Res = "";
                else Res = L1;
                break;
            case "l2":
                if (L2 == null) Res = "";
                else Res = L2;
                break;
            case "l3":
                if (L3 == null) Res = "";
                else Res = L3;
                break;
            case "l4":
                if (L4 == null) Res = "";
                else Res = L4;
                break;
            case "l5":
                if (L5 == null) Res = "";
                else Res = L5;
                break;
            case "childno":
                if (ChildNo == null) Res = "";
                else Res = ChildNo;
                break;
            case "errcode":
                if (ErrCode == null) Res = "";
                else Res = ErrCode;
                break;
            case "errdesc":
                if (ErrDesc == null) Res = "";
                else Res = ErrDesc;
                break;
        }

        return Res;
    }

    }
