package com.example.asm.Model;

public class ChayBo {
    private int buocchan;
    private double khoangcach;
    private double calori;
    private long thoigian;

    public ChayBo(int buocchan, double khoangcach, double calori, long thoigian) {
        this.buocchan = buocchan;
        this.khoangcach = khoangcach;
        this.calori = calori;
        this.thoigian = thoigian;
    }

    public int getBuocchan() {
        return buocchan;
    }

    public void setBuocchan(int buocchan) {
        this.buocchan = buocchan;
    }

    public double getKhoangcach() {
        return khoangcach;
    }

    public void setKhoangcach(double khoangcach) {
        this.khoangcach = khoangcach;
    }

    public double getCalori() {
        return calori;
    }

    public void setCalori(double calori) {
        this.calori = calori;
    }

    public long getThoigian() {
        return thoigian;
    }

    public void setThoigian(long thoigian) {
        this.thoigian = thoigian;
    }
}
