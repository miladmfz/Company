package com.kits.company.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIVerification {

    private  static Retrofit retrofit=null;
public  static  final  String BASE_URL = "http://178.131.31.161:60005/login/";

    public static Retrofit getCleint(){
        if(retrofit==null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }


}
