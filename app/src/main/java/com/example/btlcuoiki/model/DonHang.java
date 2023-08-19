package com.example.btlcuoiki.model;

import com.google.type.DateTime;

import java.util.List;

public class DonHang {
    int id;
    int iduser;
    String diachi;
    String sdt;
    String tongtien;
    String tgmuahang;
    List<Item> item;

    public String getTgmuahang() {
        return tgmuahang;
    }

    public void setTgmuahang(String tgmuahang) {
        this.tgmuahang = tgmuahang;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getTongtien() {
        return tongtien;
    }

    public void setTongtien(String tongtien) {
        this.tongtien = tongtien;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}
