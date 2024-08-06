package com.example.asm.Model;

public class BietOn {
    private String date;
    private String loibieton;

    // Constructor không tham số cần thiết cho việc deserialize Firestore
    public BietOn() {
    }

    // Constructor có tham số của bạn
    public BietOn(String date, String loibieton) {
        this.date = date;
        this.loibieton = loibieton;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoibieton() {
        return loibieton;
    }

    public void setLoibieton(String loibieton) {
        this.loibieton = loibieton;
    }
}
