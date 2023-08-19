package com.example.btlcuoiki.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.btlcuoiki.DangNhapActivity;
import com.example.btlcuoiki.GioHangActivity;
import com.example.btlcuoiki.MelonbookActivity;
import com.example.btlcuoiki.ProfileActivity;
import com.example.btlcuoiki.QuanliActivity;
import com.example.btlcuoiki.R;
import com.example.btlcuoiki.SearchActivity;
import com.example.btlcuoiki.XemDonActivity;
import com.example.btlcuoiki.adapter.LoaiSpAdapter;
import com.example.btlcuoiki.adapter.SPMoiAdapter;
import com.example.btlcuoiki.model.LoaiSp;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.model.User;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManhinhchinh;
    NavigationView navigationView;
    ListView listViewManhinhchinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SP> mangSpMoi;
    SPMoiAdapter spAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user")!=null){
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        InitView();
        ActionBar();
        ActionViewFlipper();
        if(isConnected(this)){
            //Toast.makeText(getApplicationContext(),"ok", Toast.LENGTH_LONG).show();
            ActionViewFlipper();
            getLoaiSanPham();
            getSPMoi();
            getEventClick();
        } else{
            Toast.makeText(getApplicationContext(),"khong co internet, vui long ket noi", Toast.LENGTH_LONG).show();
        }
    }

    private void getEventClick() {
        listViewManhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Utils.user_current.getAdmin()==1){
                    switch (i){
                        case 0:
                            Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(trangchu);
                            break;
                        case 1:
                            Intent melonbook = new Intent(getApplicationContext(), MelonbookActivity.class);
                            melonbook.putExtra("loai", 1);
                            startActivity(melonbook);
                            break;
                        case 2:
                            Intent goods = new Intent(getApplicationContext(), MelonbookActivity.class);
                            goods.putExtra("loai", 2);
                            startActivity(goods);
                            break;
                        case 3:
                            Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(profile);
                            break;
                        case 5:
                            Intent xemdon = new Intent(getApplicationContext(), XemDonActivity.class);
                            startActivity(xemdon);
                            break;
                        case 6:
                            Intent ql = new Intent(getApplicationContext(), QuanliActivity.class);
                            startActivity(ql);
                            break;
                        case 7:
                            //Paper.book().delete("user");
                            Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                            startActivity(dangxuat);
                            finish();
                            break;
                    }
                    }else{
                    switch (i){
                        case 0:
                            Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(trangchu);
                            break;
                        case 1:
                            Intent melonbook = new Intent(getApplicationContext(), MelonbookActivity.class);
                            melonbook.putExtra("loai", 1);
                            startActivity(melonbook);
                            break;
                        case 2:
                            Intent goods = new Intent(getApplicationContext(), MelonbookActivity.class);
                            goods.putExtra("loai", 2);
                            startActivity(goods);
                            break;
                        case 3:
                            Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(profile);
                            break;
                        case 5:
                            Intent xemdon = new Intent(getApplicationContext(), XemDonActivity.class);
                            startActivity(xemdon);
                            break;
                        case 6:
                            Paper.book().delete("user");
                            Intent dangxuat = new Intent(getApplicationContext(), DangNhapActivity.class);
                            startActivity(dangxuat);
                            finish();
                            break;
                    }
                }
            }
        });
    }

    private void getSPMoi(){
        compositeDisposable.add(apiBanHang.getSPMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        spMoiModel -> {
                            if(spMoiModel.isSuccess()){
                                mangSpMoi = spMoiModel.getResult();
                                spAdapter = new SPMoiAdapter(getApplicationContext(),mangSpMoi);
                                recyclerViewManhinhchinh.setAdapter(spAdapter);
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "khong ket noi"+ throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));

    }


    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if(loaiSpModel.isSuccess()){
                                //Toast.makeText(getApplicationContext(), loaiSpModel.getResult().get(0).getTen(), Toast.LENGTH_LONG).show();
                                mangloaisp = loaiSpModel.getResult();
                                if(Utils.user_current.getAdmin()==1) mangloaisp.add(new LoaiSp("Quản lí", ""));
                                mangloaisp.add(new LoaiSp("Đăng xuất", ""));
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                                listViewManhinhchinh.setAdapter(loaiSpAdapter);
                            }
                        }
                ));

    }
//https://pbs.twimg.com/media/ELzXZAbUYAAbPf-.jpg:large
    //https://images.ctfassets.net/g97ypubo1v2o/1xOJPXED8F5zKcpNeJmehd/55ff563d312bca86d2bd6b3ac3aa9bda/comic_10th_banner_2______.jpg?w=1200&fit=scale&q=75
    private void ActionViewFlipper() {
        List<String>mangquangcao = new ArrayList<>();
        mangquangcao.add("https://funglr.games/wp-content/uploads/2021/09/E_IWMa6XMAM4O2K.jpeg");
        mangquangcao.add("https://images.ctfassets.net/g97ypubo1v2o/1xOJPXED8F5zKcpNeJmehd/55ff563d312bca86d2bd6b3ac3aa9bda/comic_10th_banner_2______.jpg?w=1200&fit=scale&q=75");
        mangquangcao.add("https://pbs.twimg.com/media/ELzXZAbUYAAbPf-.jpg:large");
//        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-HC-Tra-Gop-800-300.png");
//        mangquangcao.add("http://mauweb.monamedia.net/thegioididong/wp-content/uploads/2017/12/banner-big-ky-nguyen-800-300.jpg");
        for(int i=0; i<mangquangcao.size();i++){
            ImageView imageView= new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);
        Animation slide_in= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void InitView() {
        toolbar=findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper=findViewById(R.id.viewlipper);
        recyclerViewManhinhchinh=findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewManhinhchinh.setLayoutManager(layoutManager);
        recyclerViewManhinhchinh.setHasFixedSize(true);
        listViewManhinhchinh=findViewById(R.id.listviewmanhinhchinh);
        navigationView=findViewById(R.id.navigationview);
        drawerLayout=findViewById(R.id.drawerLayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegihang);
        imgsearch = findViewById(R.id.search);
        // khoi tao list loai sp
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
        if(Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();
        }else {
            int totalItem = 0;
            for(int i=0; i<Utils.manggiohang.size(); i++){
                totalItem = totalItem + Utils.manggiohang.get(i).getSolg();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

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

    private boolean isConnected (Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile.isConnected())){
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}