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
    @SerializedName("Check")
    private boolean Check;


    public boolean isCheck() { return Check; }
    public void setCheck(boolean check) {
        Check = check;
    }


    public String getGoodImageName() {return GoodImageName;}
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






}
