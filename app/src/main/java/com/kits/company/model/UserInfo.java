package com.kits.company.model;

public class UserInfo {

    private String Email;
    private String NameFamily;
    private String Address;
    private String Mobile;
    private String Phone;
    private String BirthDate;
    private String MelliCode;
    private String PostalCode;
    private String ActiveCode;

    public String getBrokerCode() {
        return BrokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        BrokerCode = brokerCode;
    }

    private String BrokerCode;




    public String getEmail() {return Email;}
    public void setEmail(String email) {Email = email;}

    public String getNameFamily() {return NameFamily;}
    public void setNameFamily(String nameFamily) {NameFamily = nameFamily;}

    public String getAddress() {return Address;}
    public void setAddress(String address) {Address = address;}

    public String getMobile() {return Mobile;}
    public void setMobile(String mobile) {Mobile = mobile;}

    public String getPhone() {return Phone;}
    public void setPhone(String phone) {Phone = phone;}

    public String getBirthDate() {return BirthDate;}
    public void setBirthDate(String birthDate) {BirthDate = birthDate;}

    public String getMelliCode() {return MelliCode;}
    public void setMelliCode(String melliCode) {MelliCode = melliCode;}

    public String getPostalCode() {return PostalCode;}
    public void setPostalCode(String postalCode) {PostalCode = postalCode;}

    public String getActiveCode() {return ActiveCode;}
    public void setActiveCode(String activeCode) {ActiveCode = activeCode;}

}
