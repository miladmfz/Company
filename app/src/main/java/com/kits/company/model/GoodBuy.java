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
    @SerializedName("MobileNo")
    private String MobileNo;
    @SerializedName("ImageCount")
    private Integer ImageCount;
    @SerializedName("GoodCode")
    private Integer GoodCode;
    @SerializedName("GoodName")
    private String GoodName;
    @SerializedName("MaxSellPrice")
    private Integer MaxSellPrice;

    @SerializedName("SellPrice1Str")
    private Integer SellPrice1Str;
    @SerializedName("HasStackAmount")
    private Integer HasStackAmount;

    @SerializedName("SumFacAmount")
    private String SumFacAmount;
    @SerializedName("SumPrice")
    private String SumPrice;
    @SerializedName("CountGood")
    private String CountGood;

    @SerializedName("PreFactorCode")
    private Integer PreFactorCode;

    @SerializedName("PreFactorDate")
    private String PreFactorDate;




    public Integer getPreFactorCode() {
        return PreFactorCode;
    }

    public void setPreFactorCode(Integer preFactorCode) {
        PreFactorCode = preFactorCode;
    }

    public String getPreFactorDate() {
        return PreFactorDate;
    }

    public void setPreFactorDate(String preFactorDate) {
        PreFactorDate = preFactorDate;
    }

    public String getSumFacAmount() {
        return SumFacAmount;
    }

    public void setSumFacAmount(String sumFacAmount) {
        SumFacAmount = sumFacAmount;
    }

    public String getSumPrice() {
        return SumPrice;
    }

    public void setSumPrice(String sumPrice) {
        SumPrice = sumPrice;
    }

    public String getCountGood() {
        return CountGood;
    }

    public void setCountGood(String countGood) {
        CountGood = countGood;
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

    public Integer getProcessStatus() {
        return ProcessStatus;
    }

    public void setProcessStatus(Integer processStatus) {
        ProcessStatus = processStatus;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public Integer getImageCount() {
        return ImageCount;
    }

    public void setImageCount(Integer imageCount) {
        ImageCount = imageCount;
    }

    public Integer getGoodCode() {
        return GoodCode;
    }

    public void setGoodCode(Integer goodCode) {
        GoodCode = goodCode;
    }

    public String getGoodName() {
        return GoodName;
    }

    public void setGoodName(String goodName) {
        GoodName = goodName;
    }

    public Integer getMaxSellPrice() {
        return MaxSellPrice;
    }

    public void setMaxSellPrice(Integer maxSellPrice) {
        MaxSellPrice = maxSellPrice;
    }

    public Integer getSellPrice1Str() {
        return SellPrice1Str;
    }

    public void setSellPrice1Str(Integer sellPrice1Str) {
        SellPrice1Str = sellPrice1Str;
    }

    public Integer getHasStackAmount() {
        return HasStackAmount;
    }

    public void setHasStackAmount(Integer hasStackAmount) {
        HasStackAmount = hasStackAmount;
    }
}
