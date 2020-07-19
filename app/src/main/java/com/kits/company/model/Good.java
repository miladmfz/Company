package com.kits.company.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Good implements Serializable {

    @SerializedName("GoodCode")
    private Integer GoodCode;
    @SerializedName("GoodName")
    private String GoodName;
    @SerializedName("SellPercent")
    private String SellPercent;
    @SerializedName("SellPrice")
    private String SellPrice;
    @SerializedName("MaxSellPrice")
    private Integer MaxSellPrice;
    @SerializedName("GoodExplain1")
    private String GoodExplain1;
    @SerializedName("GoodExplain2")
    private String GoodExplain2;
    @SerializedName("GoodExplain3")
    private String GoodExplain3;
    @SerializedName("GoodExplain4")
    private String GoodExplain4;
    @SerializedName("GoodExplain5")
    private String GoodExplain5;
    @SerializedName("GoodExplain6")
    private String GoodExplain6;
    @SerializedName("GoodMainCode")
    private String GoodMainCode;
    @SerializedName("GoodSubCode")
    private String GoodSubCode;
    @SerializedName("GoodType")
    private String GoodType;
    @SerializedName("BarCode")
    private String BarCode;
    @SerializedName("ISBN")
    private String ISBN;
    @SerializedName("Writer")
    private String Writer;
    @SerializedName("DragoMan")
    private String DragoMan;
    @SerializedName("Nasher")
    private String Nasher;
    @SerializedName("TahvilDate")
    private String TahvilDate;
    @SerializedName("PrintPeriod")
    private String PrintPeriod;
    @SerializedName("PrintYear")
    private String PrintYear;
    @SerializedName("Size")
    private String Size;
    @SerializedName("CoverType")
    private String CoverType;
    @SerializedName("PageNo")
    private String PageNo;
    @SerializedName("BulletinGroupName")
    private String BulletinGroupName;
    @SerializedName("GroupsWhitoutCode")
    private String GroupsWhitoutCode;
    @SerializedName("GoodImageName")
    private String GoodImageName;
    @SerializedName("GoodImageUrl")
    private String GoodImageUrl;
    @SerializedName("Details")
    private String Details;
    @SerializedName("Itam_Show")
    private String Itam_Show;
    @SerializedName("ImageCount")
    private Integer ImageCount;
    @SerializedName("BasketAmount")
    private Integer BasketAmount;


    @SerializedName("IsFavorite")
    private Integer IsFavorite;
    @SerializedName("HasStackAmount")
    private Integer HasStackAmount;
    @SerializedName("IMG")
    private byte[] IMG;
    @SerializedName("Check")
    private boolean Check;




    public boolean isCheck() {
        return Check;
    }
    public void setCheck(boolean check) {
        Check = check;
    }




    public Integer getBasketAmount() {
        return BasketAmount;
    }
    public Integer getIsFavorite() {
        return IsFavorite;
    }
    public Integer getHasStackAmount() {
        return HasStackAmount;
    }

    public String getSellPrice() {
        return SellPrice;
    }
    public Integer getMaxSellPrice() {
        return MaxSellPrice;
    }






    public String getGoodImageName() {return GoodImageName;}
    public void setGoodImageName(String goodImageName) {GoodImageName = goodImageName;}


    public String getGoodFieldValue(String AKey)
    {
        String iKey = AKey.toLowerCase();
        String Res = "";
        if(iKey.equals("goodname")){Res = GoodName;}
        else if(iKey.equals("goodcode")){Res = GoodCode.toString();}
        else if(iKey.equals("goodmaincode")){Res = GoodMainCode;}
        else if(iKey.equals("goodsubcode")){Res = GoodSubCode;}
        else if(iKey.equals("sellpercent")){Res = SellPercent;}
        else if(iKey.equals("sellprice")){Res = SellPrice;}
        else if(iKey.equals("maxsellprice")){Res = MaxSellPrice.toString();}
        else if(iKey.equals("goodexplain1")){Res = GoodExplain1;}
        else if(iKey.equals("goodexplain2")){Res = GoodExplain2;}
        else if(iKey.equals("goodexplain3")){Res = GoodExplain3;}
        else if(iKey.equals("goodexplain4")){Res = GoodExplain4;}
        else if(iKey.equals("goodexplain5")){Res = GoodExplain5;}
        else if(iKey.equals("goodexplain6")){Res = GoodExplain6;}
        else if(iKey.equals("goodtype")){Res = GoodType;}
        else if(iKey.equals("barcode")){Res = BarCode;}
        else if(iKey.equals("isbn")){Res = ISBN;}
        else if(iKey.equals("writer")){Res = Writer;}
        else if(iKey.equals("dragoman")){Res = DragoMan;}
        else if(iKey.equals("nasher")){Res = Nasher;}
        else if(iKey.equals("tahvildate")){Res = TahvilDate;}
        else if(iKey.equals("printperiod")){Res = PrintPeriod;}
        else if(iKey.equals("printyear")){Res = PrintYear;}
        else if(iKey.equals("size")){Res = Size;}
        else if(iKey.equals("covertype")){Res = CoverType;}
        else if(iKey.equals("pageno")){Res = PageNo;}
        else if(iKey.equals("bulletingroupname")){Res = BulletinGroupName;}
        else if(iKey.equals("groupswhitoutcode")){Res = GroupsWhitoutCode;}
        else if(iKey.equals("goodimagename")){Res = GoodImageName;}
        else if(iKey.equals("goodimageurl")){Res = GoodImageUrl;}
        else if(iKey.equals("details")){Res = Details;}
        else if(iKey.equals("itam_show")){Res = Itam_Show;}
        else if(iKey.equals("imagecount")){Res = ImageCount.toString();}
        else if(iKey.equals("basketamount")){Res = BasketAmount.toString();}
        else if(iKey.equals("isfavorite")){Res = IsFavorite.toString();}
        else if(iKey.equals("hasstackamount")){Res = HasStackAmount.toString();}
        return Res;
    }






}
