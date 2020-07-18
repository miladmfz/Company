package com.kits.company.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ColumnRespons {

    @SerializedName("Columns")
    private  ArrayList<Column> Columns=null;

    public  ArrayList<Column> getColumns() {
        return Columns;
    }

    public  void setColumns(ArrayList<Column> columns) {
        Columns = columns;
    }


}
