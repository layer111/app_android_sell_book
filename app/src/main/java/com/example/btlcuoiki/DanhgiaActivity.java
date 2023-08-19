package com.example.btlcuoiki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.btlcuoiki.activity.MainActivity;
import com.example.btlcuoiki.databinding.ActivityDanhgiaBinding;
import com.example.btlcuoiki.databinding.ActivityThemSpactivityBinding;
import com.example.btlcuoiki.model.Item;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;

import java.text.DecimalFormat;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DanhgiaActivity extends AppCompatActivity {
    ActivityDanhgiaBinding binding;
    ApiBanHang apiBanHang;
    Item item ;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        binding = ActivityDanhgiaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent= getIntent();
        item = (Item) intent.getSerializableExtra("item");
        initControl();
        ActionToolBar();


    }

    private void initControl() {
        if(item.getHinhanh().contains("http")){
            Glide.with(getApplicationContext()).load(item.getHinhanh()).into(binding.itemImgchitiet);
        }else{
            String hinh = Utils.BASE_URL+ "images/"+item.getHinhanh();
            Glide.with(getApplicationContext()).load(hinh).into(binding.itemImgchitiet);

        }
        binding.itemTenchitiet.setText(item.getTen());
//        binding.itemGiachitiet.setText("Giá: "+item.getGia());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        binding.itemGiachitiet.setText("Giá: "+decimalFormat.format(Double.parseDouble(item.getGia()))+"vnđ");
        binding.itemSlchitiet.setText("Số lượng: "+Integer.toString(item.getSl()));
        binding.btndanhgia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sao=  binding.sao.getNumStars();
                String str_danhgia = binding.danhgiasp.getText().toString().trim();

                if (sao<1||sao>5) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn số sao để đánh giá!", Toast.LENGTH_LONG).show();
                }
                if (TextUtils.isEmpty(str_danhgia)){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đánh giá của bạn!", Toast.LENGTH_LONG).show();
                }else {
                    compositeDisposable.add(apiBanHang.danhgiasp(item.getIdsp(),item.getIddonhang(), sao, str_danhgia)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    messageModel -> {
                                        if(messageModel.isSuccess()){
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(getApplicationContext(), "Đã thêm đánh giá!", Toast.LENGTH_LONG).show();

                                        }else {

                                            Toast.makeText(getApplicationContext(), "0 ok", Toast.LENGTH_LONG).show();

                                        }

                                    },throwable -> {
                                        Toast.makeText(getApplicationContext(), "not ok", Toast.LENGTH_LONG).show();


                                    }
                            ));

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void ActionToolBar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}