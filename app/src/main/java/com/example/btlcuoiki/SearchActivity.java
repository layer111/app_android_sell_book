package com.example.btlcuoiki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btlcuoiki.adapter.MelonAdapter;
import com.example.btlcuoiki.model.EventBus.SuaXoa;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {
    Toolbar toolbar;
    AppCompatButton btnsearch;
    RecyclerView recyclerView;
    MelonAdapter melonAdapter;
    List<SP> spList;
    ApiBanHang apiBanHang;
    EditText edsearch, gia1, gia2;
    SP spsuaxoa;
    CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        ActionBar();
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initView() {
        gia1= findViewById(R.id.gia1);
        gia2= findViewById(R.id.gia2);
        btnsearch = findViewById(R.id.btnsearchprice);
        spList = new ArrayList<>();
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        edsearch = findViewById(R.id.edsearch);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_search);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);recyclerView.setHasFixedSize(true);
        edsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    spList.clear();
                    melonAdapter = new MelonAdapter(getApplicationContext(), spList);
                    recyclerView.setAdapter(melonAdapter);

                }else {
                    getDataSearch(charSequence.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price1 = gia1.getText().toString().trim();
                String price2 = gia2.getText().toString().trim();
                if(!price1.matches("\\d+")||!price2.matches("\\d+")){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng định dạng giá", Toast.LENGTH_LONG).show();

                } else if (Double.parseDouble(price1)>Double.parseDouble(price2)) {
                    Toast.makeText(getApplicationContext(), "Giá sau phải lớn hơn giá thứ nhất", Toast.LENGTH_LONG).show();
                } else {
                    compositeDisposable.add(apiBanHang.searchbyprice(price1, price2)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    spMoiModel -> {
                                        if (spMoiModel.isSuccess()){
                                            spList=spMoiModel.getResult();
                                            melonAdapter = new MelonAdapter(getApplicationContext(), spList);
                                            recyclerView.setAdapter(melonAdapter);
                                        } else{
                                            Toast.makeText(getApplicationContext(), "Không có sản phẩm trong mức giá này", Toast.LENGTH_LONG).show();
                                        }

                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), "Không ket noi", Toast.LENGTH_LONG).show();
                                        spList.clear();
                                        melonAdapter.notifyDataSetChanged();

                                    }
                            ));
                }

            }
        });

    }

    private void getDataSearch(String s) {
        spList.clear();
        String str_search = edsearch.getText().toString().trim();
        compositeDisposable.add(apiBanHang.timKiem(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        spMoiModel -> {
                            if (spMoiModel.isSuccess()){
                                spList=spMoiModel.getResult();
                                melonAdapter = new MelonAdapter(getApplicationContext(), spList);
                                recyclerView.setAdapter(melonAdapter);
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không ket noi", Toast.LENGTH_LONG).show();
                            spList.clear();
                            melonAdapter.notifyDataSetChanged();

                        }
                ));
    }
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals("Sửa")){
            suasp();
        }else if(item.getTitle().equals("Xóa")){
            xoasp();
        }
        return super.onContextItemSelected(item);
    }

    private void suasp() {
        Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
        intent.putExtra("sua", spsuaxoa);
        startActivity(intent);
    }

    private void xoasp() {
        String str= String.valueOf(spsuaxoa.getId());
        compositeDisposable.add(apiBanHang.xoaSP(spsuaxoa.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            Toast.makeText(getApplicationContext(), "Xóa thành công", Toast.LENGTH_LONG).show();

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        }
                ));
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenSuaXoa(SuaXoa event){
        if(event !=null){
            spsuaxoa = event.getSp();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}