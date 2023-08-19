package com.example.btlcuoiki.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SP implements Serializable {
    Date date = new Date();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
    private int id;
    private String ten;
    private String tg;
    private String ngayph;
    private String gia;
    private String hinhanh;
    private String mota;
    private int loai;

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

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getNgayph() {

        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(ngayph);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return dateFormat.format(date);
    }

    public void setNgayph(String ngayph) {
        try {
            date = new SimpleDateFormat("dd-MM-yyyy").parse(ngayph);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateFormat1.format(date);
        String ngay= String.valueOf(date);
        this.ngayph = ngay;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getLoai() {
        return loai;
    }

    public void setLoai(int loai) {
        this.loai = loai;
    }
}


//{"id","ten":","tacgia","ngayph","gia","hinhanh":,"mota":,"loai":"2"