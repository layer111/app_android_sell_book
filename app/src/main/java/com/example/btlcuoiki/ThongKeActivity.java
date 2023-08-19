//package com.example.btlcuoiki;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//
//import com.example.btlcuoiki.retrofit.ApiBanHang;
//import com.example.btlcuoiki.retrofit.RetrofitClient;
//import com.example.btlcuoiki.url.Utils;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.formatter.PercentFormatter;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
//import io.reactivex.rxjava3.disposables.CompositeDisposable;
//import io.reactivex.rxjava3.schedulers.Schedulers;
//
//public class ThongKeActivity extends AppCompatActivity {
//    Toolbar toolbar;
//    PieChart pieChart;
//    CompositeDisposable compositeDisposable = new CompositeDisposable();
//    ApiBanHang apiBanHang;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_thong_ke);
//        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
//        initView();
//        ActionBar();
//        getdataChart();
//    }
//
//    private void getdataChart() {
//        List<PieEntry> listdata = new ArrayList<>();
//        compositeDisposable.add(apiBanHang.getThongke()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        thongKeModel -> {
//                            for (int i=0; i<thongKeModel.getResult().size();i++){
//                                String ten = thongKeModel.getResult().get(i).getTen();
//                                int tong = thongKeModel.getResult().get(i).getSoluong();
//                                listdata.add(new PieEntry(tong, ten));
//
//                            }
//                            PieDataSet pieDataSet = new PieDataSet(listdata, "Thống kê số lượng sản phẩm được mua");
//                            PieData data = new PieData();
//                            data.setDataSet(pieDataSet);
//                            data.setValueTextSize(12f);
//                            data.setValueFormatter(new PercentFormatter());
//                            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//                            pieChart.setData(data);
//                            pieChart.animateXY(2000, 2000);
//                            pieChart.setUsePercentValues(true);
//                            pieChart.getDescription().setEnabled(false);
//                            pieChart.invalidate();
//                        }, throwable -> {
//                            Log.d("log", throwable.getMessage());
//                        }
//                ));
//    }
//
//    private void initView() {
//        pieChart = findViewById(R.id.piechart);
//        toolbar = findViewById(R.id.toolbar);
//    }
//
//    private void ActionBar() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        compositeDisposable.clear();
//        super.onDestroy();
//    }
//}