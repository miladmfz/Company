package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("Code")
    private Integer Code;

    @SerializedName("XUserCode")
    private String XUserCode;

    @SerializedName("XUserName")
    private String XUserName;

    @SerializedName("FName")
    private String FName;

    @SerializedName("LName")
    private String LName;

    @SerializedName("address")
    private String address;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("email")
    private String email;

    @SerializedName("XRandomCode")
    private String XRandomCode;

    @SerializedName("Active")
    private String Active;

    @SerializedName("PostalCode")
    private String PostalCode;

    @SerializedName("CustomerName")
    private String CustomerName;


    public String getXUserName() {
        return XUserName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public void setXUserName(String XUserName) {
        this.XUserName = XUserName;
    }

    public String getXUserCode() {
        return XUserCode;
    }

    public void setXUserCode(String XUserCode) {
        this.XUserCode = XUserCode;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getXRandomCode() {
        return XRandomCode;
    }

    public void setXRandomCode(String XRandomCode) {
        this.XRandomCode = XRandomCode;
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }


    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public String getLName() {
        return LName;
    }

    public void setLName(String LName) {
        this.LName = LName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String active) {
        Active = active;
    }
}
