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


    public String getGoodImageUrl() {
        return GoodImageUrl;
    }

    public void setGoodImageUrl(String goodImageUrl) {
        GoodImageUrl = goodImageUrl;
    }

    public boolean isCheck() {
        return Check;
    }

    public void setCheck(boolean check) {
        Check = check;
    }

    public Integer getBasketAmount() {
        return BasketAmount;
    }

    public void setBasketAmount(Integer basketAmount) {
        BasketAmount = basketAmount;
    }

    public Integer getIsFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        IsFavorite = isFavorite;
    }

    public Integer getImageCount() {
        return ImageCount;
    }

    public void setImageCount(Integer imageCount) {
        ImageCount = imageCount;
    }


    public Integer getHasStackAmount() {
        return HasStackAmount;
    }

    public void setHasStackAmount(Integer hasStackAmount) {
        HasStackAmount = hasStackAmount;
    }


    public String getItam_Show() {
        return Itam_Show;
    }

    public void setItam_Show(String itam_Show) {
        Itam_Show = itam_Show;
    }
    public byte[] getIMG() { return IMG; }

    public void setIMG(byte[] IMG) { this.IMG = IMG; }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getGoodName() {return GoodName;}
    void setGoodName(String goodName) {GoodName = goodName;}

    public Integer getGoodCode() {return GoodCode;}
    void setGoodCode(Integer goodCode) {GoodCode = goodCode;}

    public String getSellPercent() {
        return SellPercent;
    }
    public void setSellPercent(String sellPercent) {
        SellPercent = sellPercent;
    }

    public String getSellPrice() {
        return SellPrice;
    }
    public void setSellPrice(String sellPrice) {
        SellPrice = sellPrice;
    }

    public String getGoodExplain3() {
        return GoodExplain3;
    }
    public void setGoodExplain3(String goodExplain3) {
        GoodExplain3 = goodExplain3;
    }

    public String getGoodExplain4() {
        return GoodExplain4;
    }
    public void setGoodExplain4(String goodExplain4) {
        GoodExplain4 = goodExplain4;
    }

    public String getGoodExplain5() {
        return GoodExplain5;
    }
    public void setGoodExplain5(String goodExplain5) {
        GoodExplain5 = goodExplain5;
    }

    public String getGoodExplain6() {
        return GoodExplain6;
    }
    public void setGoodExplain6(String goodExplain6) {
        GoodExplain6 = goodExplain6;
    }

    public String getGoodSubCode() {
        return GoodSubCode;
    }
    public void setGoodSubCode(String goodSubCode) {
        GoodSubCode = goodSubCode;
    }

    public String getBarCode() {
        return BarCode;
    }
    public void setBarCode(String barCode) {
        BarCode = barCode;
    }

    public String getISBN() {
        return ISBN;
    }
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getGoodType() {return GoodType;}
    public void setGoodType(String goodType) {GoodType = goodType;}

    public String getGoodExplain1() {return GoodExplain1;}
    void setGoodExplain1(String goodExplain1) {GoodExplain1 = goodExplain1;}

    public String getGoodExplain2() {return GoodExplain2;}
    void setGoodExplain2(String goodExplain2) {GoodExplain2 = goodExplain2;}

    public String getGoodMainCode() {
        return GoodMainCode;
    }
    public void setGoodMainCode(String goodMainCode) {
        GoodMainCode = goodMainCode;
    }

    public Integer getMaxSellPrice() {
        return MaxSellPrice;
    }
    public void setMaxSellPrice(Integer maxSellPrice) {
        MaxSellPrice = maxSellPrice;
    }

    public String getWriter() {
        return Writer;
    }
    public void setWriter(String writer) {
        Writer = writer;
    }

    public String getDragoMan() {
        return DragoMan;
    }
    public void setDragoMan(String dragoMan) {
        DragoMan = dragoMan;
    }

    public String getNasher() {
        return Nasher;
    }
    public void setNasher(String nasher) {
        Nasher = nasher;
    }

    public String getTahvilDate() {
        return TahvilDate;
    }
    public void setTahvilDate(String tahvilDate) {
        TahvilDate = tahvilDate;
    }

    public String getPrintPeriod() {
        return PrintPeriod;
    }
    public void setPrintPeriod(String printPeriod) {
        PrintPeriod = printPeriod;
    }

    public String getPrintYear() {
        return PrintYear;
    }
    public void setPrintYear(String printYear) {
        PrintYear = printYear;
    }

    public String getSize() {
        return Size;
    }
    public void setSize(String size) {
        Size = size;
    }

    public String getCoverType() {
        return CoverType;
    }
    public void setCoverType(String coverType) {
        CoverType = coverType;
    }

    public String getPageNo() {
        return PageNo;
    }
    public void setPageNo(String pageNo) {
        PageNo = pageNo;
    }

    public String getBulletinGroupName() {
        return BulletinGroupName;
    }
    public void setBulletinGroupName(String bulletinGroupName) {BulletinGroupName = bulletinGroupName;}

    public String getGroupsWhitoutCode() {return GroupsWhitoutCode;}
    public void setGroupsWhitoutCode(String groupsWhitoutCode) {GroupsWhitoutCode = groupsWhitoutCode;}

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

    //    @SerializedName("state")
//     private Integer state;
//
//
//    public Integer getState() {
//        return state;
//    }
//
//    public void setState(Integer state) {
//        this.state = state;
//    }





}
