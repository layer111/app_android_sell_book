package com.example.btlcuoiki.model;

public class LoaiSp {
    private int id;
    private String ten;
    private String hinhanh;

    public LoaiSp(String ten, String hinhanh) {
        this.ten = ten;
        this.hinhanh = hinhanh;
    }

    public LoaiSp() {
    }

    public LoaiSp(int id, String ten, String hinhanh) {
        this.id = id;
        this.ten = ten;
        this.hinhanh = hinhanh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }
}
