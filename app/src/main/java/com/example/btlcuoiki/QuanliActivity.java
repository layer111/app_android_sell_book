package com.example.btlcuoiki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlcuoiki.adapter.MelonAdapter;
import com.example.btlcuoiki.adapter.SPMoiAdapter;
import com.example.btlcuoiki.model.EventBus.SuaXoa;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class QuanliActivity extends AppCompatActivity {
    ImageView them;
    CompositeDisposable compositeDisposable= new CompositeDisposable();
    ApiBanHang apiBanHang;
    Button tk;
    PieChart pieChart;
    Spinner spinner;
    int thang=0;
    Toolbar toolbar;
    TextView top10, doanhthu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quanli);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        initControl();
        ActionToolBar();

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



    private void initControl() {

        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
                startActivity(intent);
            }
        });

        List<String> str_list = new ArrayList<>();
        str_list.add("Chọn tháng");
        for (int i=1; i<=12;i++){
            str_list.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, str_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                thang = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdataChart(thang);


            }
        });
    }
    private void getDoanhthu(int thang){
        compositeDisposable.add(apiBanHang.getThongkeDoanhthu(thang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        doanhthuModel -> {
//                            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//                            String gia= "Giá: "+decimalFormat.format(doanhthuModel.getResult())+"vnđ";
                                doanhthu.setText("Doanh thu tháng "+ thang+ " là: "+ doanhthuModel.getResult()+"vnd");

                        }, throwable -> {
                            Log.d("log", throwable.getMessage());
                        }
                ));

    }

    private void getdataChart(int thang) {
        top10.setText("Top 10 sản phẩm có lượng tiêu thụ nhiều nhất trong tháng "+ thang);
        List<PieEntry> listdata = new ArrayList<>();
        compositeDisposable.add(apiBanHang.getThongke(thang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        thongKeModel -> {
                            if(thongKeModel.isSuccess()){
                                for (int i=0; i<thongKeModel.getResult().size();i++){
                                    String ten = thongKeModel.getResult().get(i).getTen();
                                    int tong = thongKeModel.getResult().get(i).getSoluong();
                                    listdata.add(new PieEntry(tong, ten));

                                }
                                pieChart.setVisibility(View.VISIBLE);
                                PieDataSet pieDataSet = new PieDataSet(listdata, "Thống kê số lượng sản phẩm được mua");
                                PieData data = new PieData();
                                data.setDataSet(pieDataSet);
                                data.setValueTextSize(13f);
                                data.setValueFormatter(new PercentFormatter());
                                pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                                pieChart.setData(data);
                                pieChart.animateXY(2000, 2000);
                                pieChart.setUsePercentValues(true);
                                pieChart.getDescription().setEnabled(false);
                                pieChart.invalidate();
                                getDoanhthu(thang);
                            }else {
                                top10.setText("");
                                doanhthu.setText("");
                                pieChart.clear();
                                pieChart.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Tháng này không có dữ liệu", Toast.LENGTH_LONG).show();
                            }
                        }, throwable -> {
                            Log.d("log", throwable.getMessage());
                        }
                ));
    }
    private void initView() {
        spinner = findViewById(R.id.spinner_tk);
        tk = findViewById(R.id.tk);
        them = findViewById(R.id.img_them);
        pieChart = findViewById(R.id.piechart);
        top10 = findViewById(R.id.top10);
        doanhthu = findViewById(R.id.doanhthu);
        toolbar = findViewById(R.id.toolbar);

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}