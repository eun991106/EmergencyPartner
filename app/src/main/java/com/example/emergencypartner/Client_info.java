package com.example.emergencypartner;


public class Client_info {

    public String name;
    public String birth;
    public String addr;
    public String readdr;
    public String phone;
    public String guardphone;
    public String email;
    public String hospname;
    public String curdisease;
    public String guard_ck;
    public String surhistory;
    public String fmlyhistory;
    public String symptom;
    public String state;



    public Client_info(){}



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getReaddr() {
        return readdr;
    }

    public void setReaddr(String readdr) {
        this.readdr = readdr;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGuardphone() {
        return guardphone;
    }

    public void setGuardphone(String guardphone) {
        this.guardphone = guardphone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHospname() {
        return hospname;
    }

    public void setHospname(String hospname) {
        this.hospname = hospname;
    }

    public String getCurdisease() {
        return curdisease;
    }

    public void setCurdisease(String curdisease) {
        this.curdisease = curdisease;
    }

    public String getGuard_ck() {
        return guard_ck;
    }

    public void setGuard_ck(String guard_ck) {
        this.guard_ck = guard_ck;
    }

    public String getSurhistory() {
        return surhistory;
    }

    public void setSurhistory(String surhistory) {
        this.surhistory = surhistory;
    }

    public String getFmlyhistory() {
        return fmlyhistory;
    }

    public void setFmlyhistory(String fmlyhistory) {
        this.fmlyhistory = fmlyhistory;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }


    public Client_info( String name,String birth,String addr,String readdr,String phone,String guardphone,String email,
                       String hospname,String guard_ck,String curdisease,String surhistory,String fmlyhistory, String symptom,String state) {

        this.name = name;
        this.birth = birth;
        this.addr = addr;
        this.readdr = readdr;
        this.phone = phone;
        this.guardphone = guardphone;
        this.email = email;
        this.hospname = hospname;
        this.guard_ck = guard_ck;
        this.curdisease = curdisease;
        this.surhistory = surhistory;
        this.fmlyhistory = fmlyhistory;
        this.symptom = symptom;
        this.state = state;


    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}