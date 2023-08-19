package com.example.btlcuoiki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlcuoiki.activity.MainActivity;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien, txtsdt, txtemail;
    EditText diachi;
    AppCompatButton btnmua;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        InitView();
        countItem();
        InitControl();
        ActionToolBar();
    }

    private void countItem() {
        totalItem = 0;
        for(int i=0; i<Utils.mangmuahang.size(); i++){
            totalItem = totalItem + Utils.mangmuahang.get(i).getSolg();
        }
    }

    private void InitControl() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        long tongtien = getIntent().getLongExtra("tongtien", 0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getMobile());

        btnmua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_diachi = diachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập địa chỉ!", Toast.LENGTH_LONG).show();

                }else {
                    String dateTime;
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    dateTime = dtf.format(now);
                    String str_email = Utils.user_current.getEmail();
                    String str_sdt = Utils.user_current.getMobile();
                    int id = Utils.user_current.getId();
                    Log.d("test", new Gson().toJson(Utils.mangmuahang));
                    compositeDisposable.add(apiBanHang.creatOrder(str_email, str_sdt, String.valueOf(tongtien), id, str_diachi,totalItem,dateTime, new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        if(userModel.isSuccess()){
                                            //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                                            for (int i=0; i<Utils.mangmuahang.size();i++){
                                                for (int j=0;j<Utils.manggiohang.size();j++){
                                                    if(Utils.mangmuahang.get(i).getId()==Utils.manggiohang.get(j).getId()){
                                                        Utils.manggiohang.remove(j);
                                                        break;
                                                    }
                                                }
                                            }
                                            Utils.mangmuahang.clear();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Mua hàng thành công", Toast.LENGTH_LONG).show();
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "not ok", Toast.LENGTH_LONG).show();
                                        }
                                    },throwable -> {
                                        Toast.makeText(getApplicationContext(), dateTime, Toast.LENGTH_LONG).show();
                                    }
                            ));

                }

            }
        });
    }

    private void InitView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar = findViewById(R.id.toolbar);
        txttongtien = findViewById(R.id.txttongtien);
        txtemail = findViewById(R.id.txtmail);
        txtsdt = findViewById(R.id.txtsdt);
        diachi = findViewById(R.id.txtdiachi);
        btnmua = findViewById(R.id.btnmua);


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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}