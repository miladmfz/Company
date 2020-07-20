package com.kits.company.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class GoodBuy implements Serializable {



    @SerializedName("FacAmount")
    private Integer FacAmount;
    @SerializedName("Price")
    private Integer Price;
    @SerializedName("ProcessStatus")
    private Integer ProcessStatus;
    @SerializedName("ImageCount")
    private Integer ImageCount;
    @SerializedName("GoodCode")
    private Integer GoodCode;
    @SerializedName("MaxSellPrice")
    private Integer MaxSellPrice;
    @SerializedName("SellPrice1Str")
    private Integer SellPrice1Str;
    @SerializedName("HasStackAmount")
    private Integer HasStackAmount;
    @SerializedName("PreFactorCode")
    private Integer PreFactorCode;
    @SerializedName("ErrCode")
    private Integer ErrCode;
    @SerializedName("SellPrice")
    private Integer SellPrice;
    @SerializedName("MobileNo")
    private String MobileNo;
    @SerializedName("GoodName")
    private String GoodName;
    @SerializedName("SumFacAmount")
    private String SumFacAmount;
    @SerializedName("SumPrice")
    private String SumPrice;
    @SerializedName("CountGood")
    private String CountGood;
    @SerializedName("ErrDesc")
    private String ErrDesc;
    @SerializedName("PreFactorDate")
    private String PreFactorDate;
    @SerializedName("Reserved")
    private String Reserved;
    @SerializedName("NotReserved")
    private String NotReserved;
    @SerializedName("IsReserved")
    private String IsReserved;


    public void setFacAmount(Integer facAmount) { FacAmount = facAmount; }






    public Integer getSellPrice() {
        return SellPrice;
    }
    public Integer getErrCode() {
        return ErrCode;
    }
    public Integer getPreFactorCode() {
        return PreFactorCode;
    }
    public Integer getFacAmount() {
        return FacAmount;
    }
    public Integer getPrice() {
        return Price;
    }








    public String getGoodBuyFieldValue(String AKey)
    {

        String iKey = AKey.toLowerCase();
        String Res = "";
        if(iKey.equals("goodname")){Res = GoodName;}
        else if(iKey.equals("goodcode"))        {if(GoodCode==null)Res ="";else Res = GoodCode.toString();}
        else if(iKey.equals("sellprice"))       {if(SellPrice==null)Res ="";else Res = SellPrice.toString();}
        else if(iKey.equals("errcode"))         {if(ErrCode==null)Res ="";else Res = ErrCode.toString();}
        else if(iKey.equals("prefactorcode"))   {if(PreFactorCode==null)Res ="";else Res = PreFactorCode.toString();}
        else if(iKey.equals("facamount"))       {if(FacAmount==null)Res ="";else Res = FacAmount.toString();}
        else if(iKey.equals("price"))           {if(Price==null)Res ="";else Res = Price.toString();}
        else if(iKey.equals("maxsellprice"))    {if(MaxSellPrice==null)Res ="";else Res = MaxSellPrice.toString();}
        else if(iKey.equals("errdesc"))         {if(ErrDesc==null)Res ="";else Res = ErrDesc;}
        else if(iKey.equals("sumfacamount"))    {if(SumFacAmount==null)Res ="";else Res = SumFacAmount;}
        else if(iKey.equals("sumprice"))        {if(SumPrice==null)Res ="";else Res = SumPrice;}
        else if(iKey.equals("countgood"))       {if(CountGood==null)Res ="";else Res = CountGood;}
        else if(iKey.equals("notreserved"))     {if(NotReserved==null)Res ="";else Res = NotReserved;}
        else if(iKey.equals("isreserved"))      {if(IsReserved==null)Res ="";else Res = IsReserved;}
        return Res;

    }






























}
