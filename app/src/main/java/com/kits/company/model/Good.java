package com.kits.company.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Good implements Serializable {

    @SerializedName("GoodCode")
    Integer GoodCode;
    @SerializedName("GoodName")
    String GoodName;
    @SerializedName("SellPercent")
    String SellPercent;
    @SerializedName("SellPrice")
    String SellPrice;
    @SerializedName("MaxSellPrice")
    Integer MaxSellPrice;
    @SerializedName("GoodExplain1")
    String GoodExplain1;
    @SerializedName("GoodExplain2")
    String GoodExplain2;
    @SerializedName("GoodExplain3")
    String GoodExplain3;
    @SerializedName("GoodExplain4")
    String GoodExplain4;
    @SerializedName("GoodExplain5")
    String GoodExplain5;
    @SerializedName("GoodExplain6")
    String GoodExplain6;
    @SerializedName("GoodMainCode")
    String GoodMainCode;
    @SerializedName("GoodSubCode")
    String GoodSubCode;
    @SerializedName("GoodType")
    String GoodType;
    @SerializedName("BarCode")
    String BarCode;
    @SerializedName("ISBN")
    String ISBN;
    @SerializedName("Writer")
    String Writer;
    @SerializedName("DragoMan")
    String DragoMan;
    @SerializedName("Nasher")
    String Nasher;
    @SerializedName("TahvilDate")
    String TahvilDate;
    @SerializedName("PrintPeriod")
    String PrintPeriod;
    @SerializedName("PrintYear")
    String PrintYear;
    @SerializedName("Size")
    String Size;
    @SerializedName("CoverType")
    String CoverType;
    @SerializedName("PageNo")
    String PageNo;
    @SerializedName("BulletinGroupName")
    String BulletinGroupName;
    @SerializedName("GroupsWhitoutCode")
    String GroupsWhitoutCode;
    @SerializedName("GoodImageName")
    String GoodImageName;
    @SerializedName("GoodImageUrl")
    String GoodImageUrl;
    @SerializedName("Details")
    String Details;
    @SerializedName("Itam_Show")
    String Itam_Show;
    @SerializedName("ImageCount")
    Integer ImageCount;
    @SerializedName("BasketAmount")
    Integer BasketAmount;
    @SerializedName("IsFavorite")
    Integer IsFavorite;
    @SerializedName("HasStackAmount")
    Integer HasStackAmount;
    @SerializedName("Check")
    boolean Check;


    public String getGoodName() {
        return GoodName;
    }

    public boolean isCheck() { return Check; }
    public void setCheck(boolean check) {
        Check = check;
    }
    public void setGoodImageName(String goodImageName) {GoodImageName = goodImageName;}


    public String getGoodFieldValue(String AKey)
    {

        String iKey = AKey.toLowerCase();
        String Res = "";
        if(iKey.equals("goodname")){Res = GoodName;}
        else if(iKey.equals("goodcode"))        {if(GoodCode==null)Res ="";else Res = GoodCode.toString();}
        else if(iKey.equals("goodmaincode"))    {if(GoodMainCode==null)Res ="";else Res = GoodMainCode;}
        else if(iKey.equals("goodsubcode"))     {if(GoodSubCode==null)Res ="";else Res = GoodSubCode;}
        else if(iKey.equals("sellpercent"))     {if(SellPercent==null)Res ="";else Res = SellPercent;}
        else if(iKey.equals("sellprice"))       {if(SellPrice==null)Res ="";else Res = SellPrice;}
        else if(iKey.equals("maxsellprice"))    {if(MaxSellPrice==null)Res ="";else Res = MaxSellPrice.toString();}
        else if(iKey.equals("goodexplain1"))    {if(GoodExplain1==null)Res ="";else Res = GoodExplain1;}
        else if(iKey.equals("goodexplain2"))    {if(GoodExplain2==null)Res ="";else Res = GoodExplain2;}
        else if(iKey.equals("goodexplain3"))    {if(GoodExplain3==null)Res ="";else Res = GoodExplain3;}
        else if(iKey.equals("goodexplain4"))    {if(GoodExplain4==null)Res ="";else Res = GoodExplain4;}
        else if(iKey.equals("goodexplain5"))    {if(GoodExplain5==null)Res ="";else Res = GoodExplain5;}
        else if(iKey.equals("goodexplain6"))    {if(GoodExplain6==null)Res ="";else Res = GoodExplain6;}
        else if(iKey.equals("goodtype"))        {if(GoodType==null)Res ="";else Res = GoodType;}
        else if(iKey.equals("barcode"))         {if(BarCode==null)Res ="";else Res = BarCode;}
        else if(iKey.equals("isbn"))            {if(ISBN==null)Res ="";else Res = ISBN;}
        else if(iKey.equals("writer"))          {if(Writer==null)Res ="";else Res = Writer;}
        else if(iKey.equals("dragoman"))        {if(DragoMan==null)Res ="";else Res = DragoMan;}
        else if(iKey.equals("nasher"))          {if(Nasher==null)Res ="";else Res = Nasher;}
        else if(iKey.equals("tahvildate"))      {if(TahvilDate==null)Res ="";else Res = TahvilDate;}
        else if(iKey.equals("printperiod"))     {if(PrintPeriod==null)Res ="";else Res = PrintPeriod;}
        else if(iKey.equals("printyear"))       {if(PrintYear==null)Res ="";else Res = PrintYear;}
        else if(iKey.equals("size"))            {if(Size==null)Res ="";else Res = Size;}
        else if(iKey.equals("covertype"))       {if(CoverType==null)Res ="";else Res = CoverType;}
        else if(iKey.equals("pageno"))          {if(PageNo==null)Res ="";else Res = PageNo;}
        else if(iKey.equals("bulletingroupname"))   {if(BulletinGroupName==null)Res ="";else Res = BulletinGroupName;}
        else if(iKey.equals("groupswhitoutcode"))   {if(GroupsWhitoutCode==null)Res ="";else Res = GroupsWhitoutCode;}
        else if(iKey.equals("goodimagename"))   {if(GoodImageName==null)Res ="";else Res = GoodImageName;}
        else if(iKey.equals("goodimageurl"))    {if(GoodImageUrl==null)Res ="";else Res = GoodImageUrl;}
        else if(iKey.equals("details"))         {if(Details==null)Res ="";else Res = Details;}
        else if(iKey.equals("itam_show"))       {if(Itam_Show==null)Res ="";else Res = Itam_Show;}
        else if(iKey.equals("imagecount"))      {if(ImageCount==null)Res ="";else Res = ImageCount.toString();}
        else if(iKey.equals("basketamount"))    {if(BasketAmount==null)Res ="";else Res = BasketAmount.toString();}
        else if(iKey.equals("isfavorite"))      {if(IsFavorite==null)Res ="";else Res = IsFavorite.toString();}
        else if(iKey.equals("hasstackamount"))  {if(HasStackAmount==null)Res ="";else Res = HasStackAmount.toString();}
        return Res;

    }


    public String getSellPrice() {
        return SellPrice;
    }

    public Integer getMaxSellPrice() {
        return MaxSellPrice;
    }
}
