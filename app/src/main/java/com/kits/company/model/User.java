package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class User {


    @SerializedName("XUserCode")
    private String XUserCode;
    @SerializedName("XUserName")
    private String XUserName;
    @SerializedName("FName")
    private String FName;
    @SerializedName("LName")
    private String LName;
    @SerializedName("address")
    private String address;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("email")
    private String email;
    @SerializedName("XRandomCode")
    private String XRandomCode;
    @SerializedName("Active")
    private String Active;
    @SerializedName("PostalCode")
    private String PostalCode;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("ErrCode")
    private String ErrCode;
    @SerializedName("ErrDesc")
    private String ErrDesc;
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getUserFieldValue(String AKey)
    {
        String iKey = AKey.toLowerCase();
        String Res = "";
        switch (iKey) {
            case "xusercode":
                Res = XUserCode;
                break;
            case "xusername":
                if (XUserName == null) Res = "";
                else Res = XUserName;
                break;
            case "postalcode":
                if (PostalCode == null) Res = "";
                else Res = PostalCode;
                break;
            case "xrandomcode":
                if (XRandomCode == null) Res = "";
                else Res = XRandomCode;
                break;
            case "fname":
                if (FName == null) Res = "";
                else Res = FName;
                break;
            case "lname":
                if (LName == null) Res = "";
                else Res = LName;
                break;
            case "address":
                if (address == null) Res = "";
                else Res = address;
                break;
            case "mobile":
                if (mobile == null) Res = "";
                else Res = mobile;
                break;
            case "email":
                if (email == null) Res = "";
                else Res = email;
                break;
            case "active":
                if (Active == null) Res = "";
                else Res = Active;
                break;
            case "customername":
                if (CustomerName == null) Res = "مشتری پیش فرض";
                else Res = CustomerName;
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
