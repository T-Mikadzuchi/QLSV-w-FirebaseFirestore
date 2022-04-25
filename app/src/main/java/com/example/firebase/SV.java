package com.example.firebase;

public class SV {

    public String hoten;
    public String mssv;

    public SV() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public SV(String name, String id) {
        this.hoten = name;
        this.mssv = id;
    }

    public String getHoten() {
        return hoten;
    }

    public String getMssv() {
        return mssv;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }
}