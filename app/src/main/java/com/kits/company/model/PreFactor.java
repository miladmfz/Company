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


    public Integer getIsReserved() {
        return IsReserved;
    }

    public void setIsReserved(Integer isReserved) {
        IsReserved = isReserved;
    }

    public Integer getFacAmount() {
        return FacAmount;
    }

    public void setFacAmount(Integer facAmount) {
        FacAmount = facAmount;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer price) {
        Price = price;
    }

    public String getProcessStatus() {
        return ProcessStatus;
    }

    public void setProcessStatus(String processStatus) {
        ProcessStatus = processStatus;
    }

    public String getImageCount() {
        return ImageCount;
    }

    public void setImageCount(String imageCount) {
        ImageCount = imageCount;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getGoodCode() {
        return GoodCode;
    }

    public void setGoodCode(String goodCode) {
        GoodCode = goodCode;
    }

    public String getGoodName() {
        return GoodName;
    }

    public void setGoodName(String goodName) {
        GoodName = goodName;
    }

    public String getMaxSellPrice() {
        return MaxSellPrice;
    }

    public void setMaxSellPrice(String maxSellPrice) {
        MaxSellPrice = maxSellPrice;
    }

    public String getSellPrice1Str() {
        return SellPrice1Str;
    }

    public void setSellPrice1Str(String sellPrice1Str) {
        SellPrice1Str = sellPrice1Str;
    }

    public String getHasStackAmount() {
        return HasStackAmount;
    }

    public void setHasStackAmount(String hasStackAmount) {
        HasStackAmount = hasStackAmount;
    }

    public String getPreFactorDate() {
        return PreFactorDate;
    }

    public void setPreFactorDate(String preFactorDate) {
        PreFactorDate = preFactorDate;
    }

    public String getPreFactorCode() {
        return PreFactorCode;
    }

    public void setPreFactorCode(String preFactorCode) {
        PreFactorCode = preFactorCode;
    }

    public String getPreFactorPrivateCode() {
        return PreFactorPrivateCode;
    }

    public void setPreFactorPrivateCode(String preFactorPrivateCode) {
        PreFactorPrivateCode = preFactorPrivateCode;
    }

    public String getRowsCount() {
        return RowsCount;
    }

    public void setRowsCount(String rowsCount) {
        RowsCount = rowsCount;
    }

    public String getSumAmount() {
        return SumAmount;
    }

    public void setSumAmount(String sumAmount) {
        SumAmount = sumAmount;
    }

    public String getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(String sumPrice) {
        SumPrice = sumPrice;
    }

    public String getSumnPrice() {
        return SumnPrice;
    }

    public void setSumnPrice(String sumnPrice) {
        SumnPrice = sumnPrice;
    }
}
