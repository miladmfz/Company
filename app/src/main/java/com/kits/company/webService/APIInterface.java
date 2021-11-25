package com.kits.company.webService;

import com.kits.company.model.RetrofitRespons;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface APIInterface {



    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> XUserCreate(@Field("tag") String tag,
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
    Call<RetrofitRespons> GetAllGood(@Field("tag") String tag,
                                 @Field("GoodCode") String GoodCode,
                                 @Field("SearchTarget") String SearchTarget,
                                 @Field("Where") String Where,
                                 @Field("GroupCode") String GroupCode,
                                 @Field("PageNo") String PageNo,
                                 @Field("MobileNo") String MobileNo,
                                 @Field("OnlyFavorite") String OnlyFavorite);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> GetLikeGood(@Field("tag") String tag,
                                  @Field("LikeGood") String LikeGood,
                                  @Field("PageNo") String PageNo);



    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> VersionInfo(@Field("tag") String tag);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Getgrp(@Field("tag") String tag,
                                  @Field("GroupCode") String GroupCode);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Getkowsar_grp(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> GetColumn(@Field("tag") String tag,
                                  @Field("GoodCode") String GoodCode,
                                  @Field("GoodType") String GoodType,
                                  @Field("Type") String Type);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> GetGoodType(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> info(@Field("tag") String tag,
                      @Field("Where") String Where);



    @POST("index.php")
    @FormUrlEncoded
    Call <RetrofitRespons> GetImage(@Field("tag") String tag,
                           @Field("GoodCode") String GoodCode,
                           @Field("IX") String IX,
                           @Field("Scale") String Scale);


    @POST("index.php")
    @FormUrlEncoded
    Call <RetrofitRespons> InsertBasket(@Field("tag") String tag,
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
    Call<RetrofitRespons> Getbasket(@Field("tag") String tag,
                                   @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> BasketPreFactor(@Field("tag") String tag,
                                           @Field("Mobile") String Mobile,
                                           @Field("Code") String Code,
                                           @Field("ReservedRows") String ReservedRows);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> GetbasketSum(@Field("tag") String tag,
                                      @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> GetbasketSum(@Field("tag") String tag,
                                      @Field("Mobile") String Mobile,
                                      @Field("Explain") String Explain);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Basketdeleteall(@Field("tag") String tag,
                                 @Field("Mobile") String Mobile);



    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Basketdelete(@Field("tag") String tag,
                              @Field("DeviceCode") String DeviceCode,
                              @Field("GoodRef") String GoodRef,
                              @Field("Mobile") String Mobile);


    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Banner_get(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Favorite_action(@Field("tag") String tag,
                                 @Field("Mobile") String Mobile,
                                 @Field("GoodRef") String GoodRef,
                                 @Field("DeleteFlag") String DeleteFlag);




    @POST("index.php")
    @FormUrlEncoded
    Call<RetrofitRespons> Verification(@Field("tag") String tag,
                              @Field("Code") String Code,
                              @Field("MobileNumber") String MobileNumber);




}


