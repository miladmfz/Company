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
        if(iKey.equals("xusercode")){Res = XUserCode;}
        else if(iKey.equals("xusername")){if(XUserName==null)Res ="";else Res = XUserName;}
        else if(iKey.equals("postalcode")){if(PostalCode==null)Res ="";else Res = PostalCode;}
        else if(iKey.equals("xrandomcode")){if(XRandomCode==null)Res ="";else Res = XRandomCode;}
        else if(iKey.equals("fname")){if(FName==null)Res ="";else Res = FName;}
        else if(iKey.equals("lname")){if(LName==null)Res ="";else Res = LName;}
        else if(iKey.equals("address")){if(address==null)Res ="";else Res = address;}
        else if(iKey.equals("mobile")){if(mobile==null)Res ="";else Res = mobile;}
        else if(iKey.equals("email")){if(email==null)Res ="";else Res = email;}
        else if(iKey.equals("active")){if(Active==null)Res ="";else Res = Active;}
        else if(iKey.equals("customername")){if(CustomerName==null)Res ="مشتری پیش فرض";else Res = CustomerName;}
        else if(iKey.equals("errcode")){if(ErrCode==null)Res ="";else Res = ErrCode;}
        else if(iKey.equals("errdesc")){if(ErrDesc==null)Res ="";else Res = ErrDesc;}


        return Res;

    }



}
