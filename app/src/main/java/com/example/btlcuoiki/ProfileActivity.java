package com.example.btlcuoiki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btlcuoiki.adapter.SPMoiAdapter;
import com.example.btlcuoiki.model.User;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ProfileActivity extends AppCompatActivity {
    TextView email, sdt, admin, username;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        ActionToolBar();
        initControl();
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
        username.setText(Utils.user_current.getUsername());
        email.setText(Utils.user_current.getEmail());
        sdt.setText(Utils.user_current.getMobile());
        if(Utils.user_current.getAdmin()==1){
            admin.setText("Admin");
        }else {
            admin.setText("User");
        }

    }

    private void initView() {
        email = findViewById(R.id.email);
        sdt = findViewById(R.id.sdt);
        username = findViewById(R.id.username);
        admin = findViewById(R.id.admin);
        toolbar = findViewById(R.id.toolbar);


    }


}