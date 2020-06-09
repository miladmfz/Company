package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GoodGroupRespons {


    @SerializedName("Groups")
    private ArrayList<GoodGroup> Groups;


    public ArrayList<GoodGroup> getGroups() {
        return Groups;
    }

    public void setGroups(ArrayList<GoodGroup> groups) {Groups = groups;}





}
