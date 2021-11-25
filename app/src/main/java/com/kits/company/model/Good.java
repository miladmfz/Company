package com.kits.company.model;


import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Good implements Serializable {

    @SerializedName("GoodCode")
    String GoodCode;
    @SerializedName("GoodName")
    String GoodName;
    @SerializedName("SellPercent")
    String SellPercent;
    @SerializedName("SellPrice")
    String SellPrice;
    @SerializedName("MaxSellPrice")
    String MaxSellPrice;
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
    String ImageCount;
    @SerializedName("BasketAmount")
    String BasketAmount;
    @SerializedName("IsFavorite")
    String IsFavorite;
    @SerializedName("HasStackAmount")
    String HasStackAmount;
    @SerializedName("TotalAmount")
    String TotalAmount;
    @SerializedName("MinPrice")
    String MinPrice;
    @SerializedName("MaxPrice")
    String MaxPrice;
    @SerializedName("Active")
    String Active;
    @SerializedName("Check")
    boolean Check;
    @SerializedName("ErrCode")
    String ErrCode;
    @SerializedName("ErrDesc")
    String ErrDesc;


    @SerializedName("GoodUnitRef")
    String GoodUnitRef;
    @SerializedName("DefaultUnitValue")
    String DefaultUnitValue;

    @SerializedName("UnitName")
    String UnitName;
    @SerializedName("UnitRef1")
    String UnitRef1;
    @SerializedName("UnitRef2")
    String UnitRef2;
    @SerializedName("UnitRef3")
    String UnitRef3;
    @SerializedName("UnitRef4")
    String UnitRef4;
    @SerializedName("UnitRef5")
    String UnitRef5;
    @SerializedName("Ratio2")
    String Ratio2;
    @SerializedName("Ratio3")
    String Ratio3;
    @SerializedName("Ratio4")
    String Ratio4;
    @SerializedName("Ratio5")
    String Ratio5;
    @SerializedName("UnitName1")
    String UnitName1;
    @SerializedName("UnitName2")
    String UnitName2;
    @SerializedName("UnitName3")
    String UnitName3;
    @SerializedName("UnitName4")
    String UnitName4;
    @SerializedName("UnitName5")
    String UnitName5;

    @SerializedName("FacAmount")
    private String FacAmount;
    @SerializedName("Price")
    private String Price;
    @SerializedName("ProcessStatus")
    private String ProcessStatus;
    @SerializedName("SellPrice1Str")
    private String SellPrice1Str;
    @SerializedName("PreFactorCode")
    private String PreFactorCode;
    @SerializedName("MobileNo")
    private String MobileNo;
    @SerializedName("SumFacAmount")
    private String SumFacAmount;
    @SerializedName("SumPrice")
    private String SumPrice;
    @SerializedName("CountGood")
    private String CountGood;
    @SerializedName("PreFactorDate")
    private String PreFactorDate;
    @SerializedName("Reserved")
    private String Reserved;
    @SerializedName("NotReserved")
    private String NotReserved;
    @SerializedName("IsReserved")
    private String IsReserved;


    @SerializedName("bUnitRef")
    String bUnitRef;
    @SerializedName("bRatio")
    String bRatio;
    @SerializedName("bUnitName")
    String bUnitName;




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
        else if(iKey.equals("goodcode"))        {if(GoodCode==null)Res ="";else Res = GoodCode;}
        else if(iKey.equals("goodmaincode"))    {if(GoodMainCode==null)Res ="";else Res = GoodMainCode;}
        else if(iKey.equals("goodsubcode"))     {if(GoodSubCode==null)Res ="";else Res = GoodSubCode;}
        else if(iKey.equals("sellpercent"))     {if(SellPercent==null)Res ="";else Res = SellPercent;}
        else if(iKey.equals("sellprice"))       {if(SellPrice==null)Res ="";else Res = SellPrice;}
        else if(iKey.equals("maxsellprice"))    {if(MaxSellPrice==null)Res ="";else Res = MaxSellPrice;}
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
        else if(iKey.equals("printperiod"))     {if(PrintPeriod==null)Res ="";else PrintPeriod.substring(0,PrintPeriod.length()-2);}
        else if(iKey.equals("printyear"))       {if(PrintYear==null)Res ="";else Res = PrintYear;}
        else if(iKey.equals("size"))            {if(Size==null)Res ="";else Res = Size;}
        else if(iKey.equals("covertype"))       {if(CoverType==null)Res ="";else Res = CoverType;}
        else if(iKey.equals("pageno"))          { if(PageNo==null) Res ="";else Res = PageNo.substring(0,PageNo.length()-2); }
        else if(iKey.equals("bulletingroupname"))   {if(BulletinGroupName==null)Res ="";else Res = BulletinGroupName;}
        else if(iKey.equals("groupswhitoutcode"))   {if(GroupsWhitoutCode==null)Res ="";else Res = GroupsWhitoutCode;}
        else if(iKey.equals("goodimagename"))   {if(GoodImageName==null)Res ="";else Res = GoodImageName;}
        else if(iKey.equals("goodimageurl"))    {if(GoodImageUrl==null)Res ="";else Res = GoodImageUrl;}
        else if(iKey.equals("details"))         {if(Details==null)Res ="";else Res = Details;}
        else if(iKey.equals("itam_show"))       {if(Itam_Show==null)Res ="";else Res = Itam_Show;}
        else if(iKey.equals("imagecount"))      {if(ImageCount==null)Res ="";else Res = ImageCount;}
        else if(iKey.equals("basketamount"))    {if(BasketAmount==null)Res ="0";else Res = BasketAmount;}
        else if(iKey.equals("isfavorite"))      {if(IsFavorite==null)Res ="";else Res = IsFavorite;}
        else if(iKey.equals("hasstackamount"))  {if(HasStackAmount==null)Res ="";else Res = HasStackAmount;}
        else if(iKey.equals("totalamount"))  {

            if(TotalAmount==null) {
                Res = "0";
            } else {
                try {
                    if(Float.parseFloat(TotalAmount)>0){
                        Res = TotalAmount;
                    }else{
                        Res = "0";
                    }
                } catch (Exception e) {
                    Res = "0";
                }
            }

        }
        else if(iKey.equals("minprice"))  {if(MinPrice==null)Res ="";else Res = MinPrice;}
        else if(iKey.equals("maxprice"))  {if(MaxPrice==null)Res ="";else Res = MaxPrice;}
        else if(iKey.equals("goodunitref"))  {if(GoodUnitRef==null)Res ="1";else Res = GoodUnitRef;}
        else if(iKey.equals("defaultunitvalue"))  {if(DefaultUnitValue==null)Res ="1";else Res = DefaultUnitValue;}
        else if(iKey.equals("unitname"))  {if(UnitName==null)Res ="";else Res = UnitName;}
        else if(iKey.equals("unitref1"))  {if(UnitRef1==null)Res ="1";else Res = UnitRef1;}
        else if(iKey.equals("unitref2"))  {if(UnitRef2==null)Res ="0";else Res = UnitRef2;}
        else if(iKey.equals("unitref3"))  {if(UnitRef3==null)Res ="0";else Res = UnitRef3;}
        else if(iKey.equals("unitref4"))  {if(UnitRef4==null)Res ="0";else Res = UnitRef4;}
        else if(iKey.equals("unitref5"))  {if(UnitRef5==null)Res ="0";else Res = UnitRef5;}
        else if(iKey.equals("ratio2"))  {if(Ratio2==null)Res ="0";else Res = Ratio2;}
        else if(iKey.equals("ratio3"))  {if(Ratio3==null)Res ="0";else Res = Ratio3;}
        else if(iKey.equals("ratio4"))  {if(Ratio4==null)Res ="0";else Res = Ratio4;}
        else if(iKey.equals("ratio5"))  {if(Ratio5==null)Res ="0";else Res = Ratio5;}
        else if(iKey.equals("unitname1"))  {if(UnitName1==null)Res ="";else Res = UnitName1;}
        else if(iKey.equals("unitname2"))  {if(UnitName2==null)Res ="";else Res = UnitName2;}
        else if(iKey.equals("unitname3"))  {if(UnitName3==null)Res ="";else Res = UnitName3;}
        else if(iKey.equals("unitname4"))  {if(UnitName4==null)Res ="";else Res = UnitName4;}
        else if(iKey.equals("unitname5"))  {if(UnitName5==null)Res ="";else Res = UnitName5;}
        else if(iKey.equals("errcode")){if(ErrCode==null)Res ="";else Res = ErrCode;}
        else if(iKey.equals("errdesc")){if(ErrDesc==null)Res ="";else Res = ErrDesc;}
        else if(iKey.equals("active")){if(Active==null)Res ="";else Res = Active;}
        else if(iKey.equals("prefactorcode"))   {if(PreFactorCode==null)Res ="";else Res = PreFactorCode;}
        else if(iKey.equals("facamount"))       {if(FacAmount==null)Res ="";else Res = FacAmount;}
        else if(iKey.equals("price"))           {if(Price==null)Res ="";else Res = Price;}
        else if(iKey.equals("sumfacamount"))    {if(SumFacAmount==null)Res ="";else Res = SumFacAmount;}
        else if(iKey.equals("sumprice"))        {if(SumPrice==null)Res ="";else Res = SumPrice;}
        else if(iKey.equals("countgood"))       {if(CountGood==null)Res ="";else Res = CountGood;}
        else if(iKey.equals("notreserved"))     {if(NotReserved==null)Res ="";else Res = NotReserved;}
        else if(iKey.equals("isreserved"))      {if(IsReserved==null)Res ="";else Res = IsReserved;}
        else if(iKey.equals("bunitref"))  {if(bUnitRef==null)Res ="";else Res = bUnitRef;}
        else if(iKey.equals("bratio"))  {if(bRatio==null)Res ="";else Res = bRatio;}
        else if(iKey.equals("bunitname"))  {if(bUnitName==null)Res ="";else Res = bUnitName;}
        return Res;

    }





    public void setFacAmount(String facAmount) { FacAmount = facAmount; }

    public String getSellPrice() {
        return SellPrice;
    }

    public String getMaxSellPrice() {
        return MaxSellPrice;
    }
}
