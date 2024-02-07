package com.kits.company.webService;

import com.kits.company.model.RetrofitResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIInterface {


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> XUserCreate(@Field("tag") String tag,
                                       @Field("UName") String user,
                                       @Field("UPass") String pass,
                                       @Field("NewPass") String NewPass,
                                       @Field("FName") String fname,
                                       @Field("LName") String lname,
                                       @Field("mobile") String mobile,
                                       @Field("company") String company,
                                       @Field("address") String address,
                                       @Field("PostalCode") String PostalCode,
                                       @Field("email") String email,
                                       @Field("Flag") String Flag);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> GetAllGood(@Field("tag") String tag,
                                      @Field("GoodCode") String GoodCode,
                                      @Field("SearchTarget") String SearchTarget,
                                      @Field("Where") String Where,
                                      @Field("GroupCode") String GroupCode,
                                      @Field("PageNo") String PageNo,
                                      @Field("MobileNo") String MobileNo,
                                      @Field("OnlyFavorite") String OnlyFavorite);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> GetLikeGood(@Field("tag") String tag,
                                       @Field("LikeGood") String LikeGood,
                                       @Field("PageNo") String PageNo);



    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> VersionInfo(@Field("tag") String tag);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Getgrp(@Field("tag") String tag,
                                  @Field("GroupCode") String GroupCode);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Getkowsar_grp(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> GetColumn(@Field("tag") String tag,
                                     @Field("GoodCode") String GoodCode,
                                     @Field("GoodType") String GoodType,
                                     @Field("Type") String Type);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> GetGoodType(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> info(@Field("tag") String tag,
                                @Field("Where") String Where);



    @POST("index.php")
    @FormUrlEncoded
    Call <RetrofitResponse> GetImageCompany(@Field("tag") String tag,
                                     @Field("GoodCode") String GoodCode,
                                     @Field("IX") String IX,
                                     @Field("Scale") String Scale);

    @POST("index.php")
    @FormUrlEncoded
    Call <String> GetImage(@Field("tag") String tag,
                           @Field("GoodCode") String GoodCode,
                           @Field("IX") Integer IX,
                           @Field("Scale") Integer Scale);
    @POST("index.php")
    @FormUrlEncoded
    Call <RetrofitResponse> InsertBasket(@Field("tag") String tag,
                                         @Field("DeviceCode") String DeviceCode,
                                         @Field("GoodRef") String GoodRef,
                                         @Field("FacAmount") String FacAmount,
                                         @Field("Price") String Price,
                                         @Field("UnitRef") String UnitRef,
                                         @Field("Ratio") String Ratio,
                                         @Field("Explain") String Explain,
                                         @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Notification(
            @Field("tag") String tag,
            @Field("Condition") String Condition
    );

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Getbasket(@Field("tag") String tag,
                                     @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> BasketPreFactor(@Field("tag") String tag,
                                           @Field("Mobile") String Mobile,
                                           @Field("Code") String Code,
                                           @Field("ReservedRows") String ReservedRows);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> GetbasketSum(@Field("tag") String tag,
                                        @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> GetbasketSum(@Field("tag") String tag,
                                        @Field("Mobile") String Mobile,
                                        @Field("Explain") String Explain);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Basketdeleteall(@Field("tag") String tag,
                                           @Field("Mobile") String Mobile);



    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Basketdelete(@Field("tag") String tag,
                                        @Field("DeviceCode") String DeviceCode,
                                        @Field("GoodRef") String GoodRef,
                                        @Field("Mobile") String Mobile);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Banner_get(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Favorite_action(@Field("tag") String tag,
                                           @Field("Mobile") String Mobile,
                                           @Field("GoodRef") String GoodRef,
                                           @Field("DeleteFlag") String DeleteFlag);




    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Verification(@Field("tag") String tag,
                                        @Field("Code") String Code,
                                        @Field("MobileNumber") String MobileNumber);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitResponse> Errorlog(
            @Field("tag")         String tag
            , @Field("ErrorLog")    String ErrorLog
            , @Field("Broker")      String Broker
            , @Field("DeviceId")    String DeviceId
            , @Field("ServerName")  String ServerName
            , @Field("StrDate")     String StrDate
            , @Field("VersionName") String VersionName
    );




    @POST("SendSms")
    Call<String> Verification(@Query("RandomCode") Integer Code,
                              @Query("NumberPhone") String MobileNumber);


}


