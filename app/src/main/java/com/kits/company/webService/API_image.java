package com.kits.company.webService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API_image {

    private  static Retrofit retrofit_image=null;
//public  static  final  String BASE_URL = "http://87.107.78.234:60005/login/";
public  static  final  String BASE_URL = "http://87.107.78.234:60005/login/";

    public static Retrofit getCleint(){
        if(retrofit_image==null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit_image = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit_image;
    }


}
