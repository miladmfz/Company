package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class GoodGroup {



    @SerializedName("GroupCode")
    private Integer GroupCode;
    @SerializedName("Name")
    private String Name;
    @SerializedName("L1")
    private Integer L1;
    @SerializedName("L2")
    private Integer L2;
    @SerializedName("L3")
    private Integer L3;
    @SerializedName("L4")
    private Integer L4;
    @SerializedName("L5")
    private Integer L5;
    @SerializedName("ChildNo")
    private Integer ChildNo;
    @SerializedName("ErrCode")
    private Integer ErrCode;
    @SerializedName("ErrDesc")
    private String ErrDesc;

    public void setName(String name) {Name = name;}

    public String getGoodGroupFieldValue(String AKey) {

        String iKey = AKey.toLowerCase();
        String Res = "";
        if (iKey.equals("name")) {Res = Name;}
        else if (iKey.equals("groupcode")) {if (GroupCode == null) Res = "";else Res = GroupCode.toString();}
        else if (iKey.equals("l1")) {if (L1 == null) Res = "";else Res = L1.toString();}
        else if (iKey.equals("l2")) {if (L2 == null) Res = "";else Res = L2.toString();}
        else if (iKey.equals("l3")) {if (L3 == null) Res = "";else Res = L3.toString();}
        else if (iKey.equals("l4")) {if (L4 == null) Res = "";else Res = L4.toString();}
        else if (iKey.equals("l5")) {if (L5 == null) Res = "";else Res = L5.toString();}
        else if (iKey.equals("childno")) {if (ChildNo == null) Res = "";else Res = ChildNo.toString();}
        else if (iKey.equals("errcode")) {if (ErrCode == null) Res = "";else Res = ErrCode.toString();}
        else if (iKey.equals("errdesc")) {if (ErrDesc == null) Res = "";else Res = ErrDesc;}

        return Res;
    }

    }
