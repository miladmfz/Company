package com.kits.company.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RetrofitResponse {


    @SerializedName("Column")
    private Column column;
    @SerializedName("Columns")
    private ArrayList<Column> Columns;


    @SerializedName("Group")
    private GoodGroup group;
    @SerializedName("Groups")
    private ArrayList<GoodGroup> Groups;


    @SerializedName("Good")
    private Good good;
    @SerializedName("Goods")
    private ArrayList<Good> Goods;


    @SerializedName("PreFactor")
    private PreFactor preFactor;
    @SerializedName("PreFactors")
    private ArrayList<PreFactor> PreFactors;

    @SerializedName("user")
    private User user;
    @SerializedName("users")
    private ArrayList<User> users;



    @SerializedName("value")
    private String value;
    @SerializedName("Text")
    private String Text;


    @SerializedName("ErrCode")
    private String ErrCode;
    @SerializedName("ErrDesc")
    private String ErrDesc;











    public ArrayList<Good> getGoods() {
        return Goods;
    }

    public void setGoods(ArrayList<Good> goods) {
        Goods = goods;
    }

    public ArrayList<GoodGroup> getGroups() {
        return Groups;
    }

    public void setGroups(ArrayList<GoodGroup> groups) {
        Groups = groups;
    }

    public ArrayList<PreFactor> getPreFactors() {
        return PreFactors;
    }

    public void setPreFactors(ArrayList<PreFactor> preFactors) {
        PreFactors = preFactors;
    }

    public ArrayList<Column> getColumns() {
        return Columns;
    }

    public void setColumns(ArrayList<Column> columns) {
        Columns = columns;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public GoodGroup getGroup() {
        return group;
    }

    public void setGroup(GoodGroup group) {
        this.group = group;
    }

    public Good getGood() {
        return good;
    }

    public void setGood(Good good) {
        this.good = good;
    }

    public PreFactor getPreFactor() {
        return preFactor;
    }

    public void setPreFactor(PreFactor preFactor) {
        this.preFactor = preFactor;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getErrCode() {
        return ErrCode;
    }

    public void setErrCode(String errCode) {
        ErrCode = errCode;
    }

    public String getErrDesc() {
        return ErrDesc;
    }

    public void setErrDesc(String errDesc) {
        ErrDesc = errDesc;
    }
}
