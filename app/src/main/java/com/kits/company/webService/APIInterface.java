package com.kits.company.webService;

import com.kits.company.model.ColumnRespons;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodGroupRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.model.PreFactorRespons;
import com.kits.company.model.UsersRespons;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface APIInterface {



    @POST("index.php")
    @FormUrlEncoded
    Call<UsersRespons> XUserCreate(@Field("tag") String tag,
                                   @Field("UName") String user,
                                   @Field("UPass") String pass,
                                   @Field("NewPass") String NewPass,
                                   @Field("FName") String fname,
                                   @Field("LName") String lname,
                                   @Field("mobile") String mobile,
                                   @Field("address") String address,
                                   @Field("PostalCode") String PostalCode,
                                   @Field("email") String email,
                                   @Field("Flag") String Flag);


    @POST("index.php")
    @FormUrlEncoded
    Call<GoodRespons> GetAllGood(@Field("tag") String tag  ,
                                 @Field("SearchTarget") String SearchTarget,
                                 @Field("Where") String Where,
                                 @Field("GroupCode") Integer GroupCode,
                                 @Field("PageNo") Integer PageNo,
                                 @Field("MobileNo") String MobileNo,
                                 @Field("OnlyFavorite") int OnlyFavorite);

    @POST("index.php")
    @FormUrlEncoded
    Call<GoodRespons> GetLikeGood(@Field("tag") String tag  ,
                                  @Field("LikeGood") Integer LikeGood,
                                  @Field("PageNo") Integer PageNo);

    @POST("index.php")
    @FormUrlEncoded
    Call<GoodRespons> check(@Field("tag") String tag );

    @POST("index.php")
    @FormUrlEncoded
    Call<GoodGroupRespons> Getgrp(@Field("tag") String tag ,
                                  @Field("GroupCode") Integer GroupCode);

    @POST("index.php")
    @FormUrlEncoded
    Call<GoodGroupRespons> Getkowsar_grp(@Field("tag") String tag );

    @POST("index.php")
    @FormUrlEncoded
    Call<ColumnRespons> asd(@Field("tag") String tag ,
                            @Field("GoodCode") Integer GoodCode);



    @POST("index.php")
    @FormUrlEncoded
    Call<String> info(@Field("tag") String tag,
                      @Field("Where") Integer Where );

    @POST("index.php")
    @FormUrlEncoded
    Call <String> GetImage(@Field("tag") String tag,
                           @Field("GoodCode") String GoodCode,
                           @Field("IX") Integer IX,
                           @Field("Scale") Integer Scale);


    @POST("index.php")
    @FormUrlEncoded
    Call <GoodBuyRespons> InsertBasket(@Field("tag") String tag,
                               @Field("DeviceCode") String DeviceCode,
                               @Field("GoodRef") Integer GoodRef,
                               @Field("FacAmount") Integer FacAmount,
                               @Field("Price") Integer Price,
                               @Field("Explain") String Explain,
                               @Field("Mobile") String Mobile);


    @POST("index.php")
    @FormUrlEncoded
    Call<GoodBuyRespons> Getbasket(@Field("tag") String tag  ,
                                   @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<PreFactorRespons> BasketPreFactor(@Field("tag") String tag  ,
                                           @Field("Mobile") String Mobile,
                                           @Field("Code") String Code,
                                           @Field("ReservedRows") String ReservedRows);


    @POST("index.php")
    @FormUrlEncoded
    Call<GoodBuyRespons> GetbasketSum(@Field("tag") String tag  ,
                                      @Field("Mobile") String Mobile);

    @POST("index.php")
    @FormUrlEncoded
    Call<GoodBuyRespons> GetbasketSum(@Field("tag") String tag  ,
                                      @Field("Mobile") String Mobile ,
                                      @Field("Explain") String Explain);


    @POST("index.php")
    @FormUrlEncoded
    Call<String> Basketdeleteall(@Field("tag") String tag  ,
                                 @Field("Mobile") String Mobile);



    @POST("index.php")
    @FormUrlEncoded
    Call<String> Basketdelete(@Field("tag") String tag,
                              @Field("DeviceCode") String DeviceCode,
                              @Field("GoodRef") Integer GoodRef ,
                              @Field("Mobile") String Mobile);


    @POST("index.php")
    @FormUrlEncoded
    Call<GoodRespons> Banner_get(@Field("tag") String tag);

    @POST("index.php")
    @FormUrlEncoded
    Call<String> Favorite_action(@Field("tag") String tag,
                                   @Field("Mobile") String Mobile,
                                   @Field("GoodRef") Integer GoodRef,
                                   @Field("DeleteFlag") Integer DeleteFlag);




    @POST("index.php")
    @FormUrlEncoded
    Call<String> Verification(@Field("tag") String tag,
                              @Field("Code") Integer Code,
                              @Field("MobileNumber") String MobileNumber);




}


