package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class PreFactor {


    @SerializedName("FacAmount")
    private String FacAmount;

    @SerializedName("Price")
    private String Price;

    @SerializedName("ProcessStatus")
    private String ProcessStatus;

    @SerializedName("ImageCount")
    private String ImageCount;

    @SerializedName("MobileNo")
    private String MobileNo;

    @SerializedName("GoodCode")
    private String GoodCode;

    @SerializedName("GoodName")
    private String GoodName;

    @SerializedName("GoodImageName")
    private String GoodImageName="";

    @SerializedName("MaxSellPrice")
    private String MaxSellPrice;

    @SerializedName("SellPrice1Str")
    private String SellPrice1Str;

    @SerializedName("HasStackAmount")
    private String HasStackAmount;


    @SerializedName("PreFactorDate")
    private String PreFactorDate;

    @SerializedName("PreFactorCode")
    private String PreFactorCode;

    @SerializedName("PreFactorPrivateCode")
    private String PreFactorPrivateCode;

    @SerializedName("RowsCount")
    private String RowsCount;
    @SerializedName("SumAmount")
    private String SumAmount;
    @SerializedName("SumPrice")
    private String SumPrice;
    @SerializedName("SumnPrice")
    private String SumnPrice;
    @SerializedName("IsReserved")
    private String IsReserved;
    @SerializedName("ErrCode")
    private String ErrCode;
    @SerializedName("ErrDesc")
    private String ErrDesc;

    public void setGoodImageName(String goodImageName) {GoodImageName = goodImageName;}

    public String getPreFactorFieldValue(String AKey) {
        String iKey = AKey.toLowerCase();
        String Res = "";
        switch (iKey) {
            case "goodname":
                Res = GoodName;
                break;
            case "goodimagename":
                if (GoodImageName == null) Res = "";
                Res = GoodImageName;
                break;
            case "goodcode":
                if (GoodCode == null) Res = "";
                else Res = GoodCode;
                break;
            case "isreserved":
                if (IsReserved == null) Res = "";
                else Res = IsReserved.toString();
                break;
            case "facamount":
                if (FacAmount == null) Res = "";
                else Res = FacAmount;
                break;
            case "price":
                if (Price == null) Res = "";
                else Res = Price;
                break;
            case "maxsellprice":
                if (MaxSellPrice == null) Res = "";
                else Res = MaxSellPrice;
                break;
            case "prefactordate":
                if (PreFactorDate == null) Res = "";
                else Res = PreFactorDate;
                break;
            case "prefactorcode":
                if (PreFactorCode == null) Res = "";
                else Res = PreFactorCode;
                break;
            case "rowscount":
                if (RowsCount == null) Res = "";
                else Res = RowsCount;
                break;
            case "sumamount":
                if (SumAmount == null) Res = "";
                else Res = SumAmount;
                break;
            case "sumprice":
                if (SumPrice == null) Res = "";
                else Res = SumPrice;
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
