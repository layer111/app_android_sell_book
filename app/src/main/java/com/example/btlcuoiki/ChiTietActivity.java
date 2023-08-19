package com.example.btlcuoiki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.btlcuoiki.adapter.DanhgiaAdapter;
import com.example.btlcuoiki.adapter.MelonAdapter;
import com.example.btlcuoiki.model.Danhgia;
import com.example.btlcuoiki.model.GioHang;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.security.SecurityPermission;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChiTietActivity extends AppCompatActivity {
    TextView ten, gia, tg, mota, ngay, sosao, loai, sl;
    ImageView img, tru, cong;
    Button btnadd;
    DanhgiaAdapter danhgiaAdapter;
    Toolbar toolbar;
    RecyclerView recyclerView;
    SP sp ;
    NotificationBadge badge;
    RatingBar saotb;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        InitView();
        ActionToolBar();
        InitData();
        initControl();
    }

    private void initControl() {
        tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slg = Integer.parseInt(sl.getText().toString().trim());
                if (slg==1){
                    Toast.makeText(getApplicationContext(), "Số lượng sản phẩm không nhỏ hơn 1", Toast.LENGTH_SHORT).show();

                }else{
                    sl.setText(String.valueOf(slg-1));
                }
            }
        });
        cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int slg = Integer.parseInt(sl.getText().toString().trim());
                sl.setText(String.valueOf(slg+1));

            }
        });
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGiohang();
            }
        });
    }

    private void themGiohang() {
        if (Utils.manggiohang.size() > 0){
            boolean flag = false;
            int solg = Integer.parseInt(sl.getText().toString().trim());
            for (int i=0; i<Utils.manggiohang.size(); i++){
                if(Utils.manggiohang.get(i).getId() == sp.getId()){
                    Utils.manggiohang.get(i).setSolg(solg+Utils.manggiohang.get(i).getSolg());
                    long gia = Long.parseLong(sp.getGia())* Utils.manggiohang.get(i).getSolg();
                    Utils.manggiohang.get(i).setGia(gia);
                    flag = true;
                }
            }
            if (flag==false) {
                //solg = Integer.parseInt(spinner.getSelectedItem().toString());
                long gia = Long.parseLong(sp.getGia())* solg;
                GioHang gioHang = new GioHang();
                gioHang.setGia(gia);
                gioHang.setSolg(solg);
                gioHang.setId(sp.getId());
                gioHang.setTen(sp.getTen());
                gioHang.setHinhanh(sp.getHinhanh());
                Utils.manggiohang.add(gioHang);
            }
        }else {
            int solg = Integer.parseInt(sl.getText().toString().trim());
            Long gia = Long.parseLong(sp.getGia())* solg;
            GioHang gioHang = new GioHang();
            gioHang.setGia(gia);
            gioHang.setSolg(solg);
            gioHang.setId(sp.getId());
            gioHang.setTen(sp.getTen());
            gioHang.setHinhanh(sp.getHinhanh());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem = 0;
        for(int i=0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSolg();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void InitData() {
        sp = (SP)getIntent().getSerializableExtra("chitiet");
        ten.setText(sp.getTen());
        if(sp.getLoai()==1){
            loai.setText("Melonbook");
        }else{
            loai.setText("Goods");
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        gia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sp.getGia()))+"vnđ");
        Glide.with(getApplicationContext()).load(sp.getHinhanh()).into(img);
        if(sp.getHinhanh().contains("http")){
            Glide.with(getApplicationContext()).load(sp.getHinhanh()).into(img);
        }else{
            String hinh = Utils.BASE_URL+ "images/"+sp.getHinhanh();
            Glide.with(getApplicationContext()).load(hinh).into(img);

        }
        tg.setText("- Tác giả: " +sp.getTg());
        mota.setText("- Mô tả: "+ sp.getMota());
        ngay.setText("- Ngày phát hành: "+sp.getNgayph());

        compositeDisposable.add(apiBanHang.getsao(sp.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        doanhthuModel -> {
                            if(doanhthuModel.isSuccess()){
                                String sao = doanhthuModel.getResult();
                                saotb.setRating(Float.parseFloat(sao));
                                sosao.setText(sao);
                            }else {
                                sosao.setText("chưa có đánh giá");
                            }
                            //Toast.makeText(getApplicationContext(), doanhthuModel.getResult(), Toast.LENGTH_LONG).show();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "khong thanh cong", Toast.LENGTH_LONG).show();
                        }
                ));
        compositeDisposable.add(apiBanHang.getdanhgia(sp.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        danhgiaModel -> {
                            if(danhgiaModel.isSuccess()){
                                List<Danhgia> danhgiaList= danhgiaModel.getResult();
                                danhgiaAdapter = new DanhgiaAdapter(getApplicationContext(), danhgiaList);
                                recyclerView.setAdapter(danhgiaAdapter);
                                //Toast.makeText(getApplicationContext(), "thanh cong", Toast.LENGTH_LONG).show();
                            }else {
                                //Toast.makeText(getApplicationContext(), "khong co danh gia", Toast.LENGTH_LONG).show();

                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "khong thanh cong", Toast.LENGTH_LONG).show();
                        }
                ));

    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void InitView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        saotb = findViewById(R.id.saotb);
        recyclerView = findViewById(R.id.recycleview_danhgia);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ngay = findViewById(R.id.txtngay);
        ten = findViewById(R.id.txtten);
        tg = findViewById(R.id.txttg);
        gia = findViewById(R.id.txtgia);
        mota = findViewById(R.id.txtmota);
        sosao = findViewById(R.id.sosao);
        img = findViewById(R.id.imgchitiet);
        sl = findViewById(R.id.sl);
        cong = findViewById(R.id.cong);
        tru = findViewById(R.id.tru);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        btnadd = findViewById(R.id.btnaddcart);
        loai= findViewById(R.id.txtloai);
        FrameLayout frameLayoutGiohang = findViewById(R.id.framegihang);
        frameLayoutGiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if(Utils.manggiohang != null){
            int totalItem = 0;
            for(int i=0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSolg();
            }
            badge.setText(String.valueOf(totalItem));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i=0; i<Utils.manggiohang.size(); i++){
            totalItem = totalItem + Utils.manggiohang.get(i).getSolg();
        }
        badge.setText(String.valueOf(totalItem));
    }
}