package com.example.btlcuoiki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.btlcuoiki.adapter.MelonAdapter;
import com.example.btlcuoiki.adapter.SPMoiAdapter;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MelonbookActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    int page = 1;
    int loai;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MelonAdapter melonAdapter;
    List<SP> spList;
    //LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_melonbook);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        InitView();
        if(loai==1) toolbar.setTitle("Melonbook"); else toolbar.setTitle("Goods");
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false){
                    if(gridLayoutManager.findLastCompletelyVisibleItemPosition()==spList.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }


        });
    }
    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                spList.add(null);
                melonAdapter.notifyItemInserted(spList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                spList.remove(spList.size()-1);
                melonAdapter.notifyItemRemoved(spList.size());
                page=page+1;
                getData(page);
                melonAdapter.notifyDataSetChanged();
                isLoading=false;
            }
        }, 2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSP(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        spMoiModel -> {
                            if(spMoiModel.isSuccess()){
                                if(melonAdapter==null){
                                    spList = spMoiModel.getResult();
                                    melonAdapter = new MelonAdapter(getApplicationContext(),spList);
                                    recyclerView.setAdapter(melonAdapter);
                                }else {
                                    int vitri = spList.size()-1;
                                    int soluongadd = spMoiModel.getResult().size();
                                    for (int i=0; i<soluongadd; i++){
                                        spList.add(spMoiModel.getResult().get(i));
                                    }
                                    melonAdapter.notifyItemRangeInserted(vitri,soluongadd);
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), "Hết dữ liệu ròi", Toast.LENGTH_LONG).show();
                                isLoading=true;
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
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
        toolbar = findViewById(R.id.toolbar_melon);
        recyclerView = findViewById(R.id.recycleview_melonbook);
        //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        spList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}