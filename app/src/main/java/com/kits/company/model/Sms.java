package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

public class Sms {

    @SerializedName("TokenKey")
    private String TokenKey;

    @SerializedName("IsSuccessful")
    private Boolean IsSuccessful;

    @SerializedName("Message")
    private String Message;

    @SerializedName("VerificationCodeId")
    private Float VerificationCodeId;


    public String getTokenKey() {
        return TokenKey;
    }

    public Boolean getSuccessful() {
        return IsSuccessful;
    }

    public void setSuccessful(Boolean successful) {
        IsSuccessful = successful;
    }

    public void setTokenKey(String tokenKey) {
        TokenKey = tokenKey;
    }



    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Float getVerificationCodeId() {
        return VerificationCodeId;
    }

    public void setVerificationCodeId(Float verificationCodeId) {
        VerificationCodeId = verificationCodeId;
    }
}
