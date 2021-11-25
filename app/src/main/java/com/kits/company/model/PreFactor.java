package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class PreFactor {





    @SerializedName("FacAmount")
    private Integer FacAmount;

    @SerializedName("Price")
    private Integer Price;

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
    private Integer IsReserved;
    @SerializedName("ErrCode")
    private Integer ErrCode;
    @SerializedName("ErrDesc")
    private String ErrDesc;

    public String getPreFactorFieldValue(String AKey)
    {
        String iKey = AKey.toLowerCase();
        String Res = "";
        if(iKey.equals("goodname")){Res = GoodName;}
        else if(iKey.equals("goodcode")){if(GoodCode==null)Res ="";else Res = GoodCode;}
        else if(iKey.equals("isreserved")){if(IsReserved==null)Res ="";else Res = IsReserved.toString();}
        else if(iKey.equals("facamount")){if(FacAmount==null)Res ="";else Res = FacAmount.toString();}
        else if(iKey.equals("price")){if(Price==null)Res ="";else Res = Price.toString();}
        else if(iKey.equals("maxsellprice")){if(MaxSellPrice==null)Res ="";else Res = MaxSellPrice;}
        else if(iKey.equals("prefactordate")){if(PreFactorDate==null)Res ="";else Res = PreFactorDate;}
        else if(iKey.equals("prefactorcode")){if(PreFactorCode==null)Res ="";else Res = PreFactorCode;}
        else if(iKey.equals("rowscount")){if(RowsCount==null)Res ="";else Res = RowsCount;}
        else if(iKey.equals("sumamount")){if(SumAmount==null)Res ="";else Res = SumAmount;}
        else if(iKey.equals("sumprice")){if(SumPrice==null)Res ="";else Res = SumPrice;}
        else if(iKey.equals("errcode")){if(ErrCode==null)Res ="";else Res = ErrCode.toString();}
        else if(iKey.equals("errdesc")){if(ErrDesc==null)Res ="";else Res = ErrDesc;}

        return Res;

    }








}
