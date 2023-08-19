package com.example.btlcuoiki.url;

import com.example.btlcuoiki.model.GioHang;
import com.example.btlcuoiki.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    //public static final String BASE_URL="http://192.168.1.31:8080/banhang/";
    //public static final String BASE_URL="http://192.168.229.196:8080/banhang/";
    public static final String BASE_URL="http://10.21.62.81:8080/banhang/";

    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang = new ArrayList<>();


    public static User user_current = new User();
}
