package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PreFactorRespons {


    @SerializedName("PreFactors")
    private ArrayList<PreFactor> PreFactors;


    public ArrayList<PreFactor> getPreFactors() {
        return PreFactors;
    }

    public void setPreFactors(ArrayList<PreFactor> preFactors) {
        PreFactors = preFactors;
    }
}
