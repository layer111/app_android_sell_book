package com.example.btlcuoiki;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btlcuoiki.activity.MainActivity;
import com.example.btlcuoiki.databinding.ActivityThemSpactivityBinding;
import com.example.btlcuoiki.model.Item;
import com.example.btlcuoiki.model.MessageModel;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.retrofit.RetrofitClient;
import com.example.btlcuoiki.url.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    int loai=0;
    String mediaPath;
    SP spsua;
    ActivityThemSpactivityBinding binding;
    ApiBanHang apiBanHang;
    boolean flag = false;
    CompositeDisposable compositeDisposable= new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        binding = ActivityThemSpactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initData();
        ActionToolBar();
        Intent intent= getIntent();
        spsua = (SP) intent.getSerializableExtra("sua");
        if(spsua==null){
            flag = false;
        }else {
            flag = true;
            binding.btnthemsp.setText("Sửa sản phẩm");
            // show data
            binding.themTen.setText(spsua.getTen());
            binding.themNgay.setText(spsua.getNgayph());
            binding.themHa.setText(spsua.getHinhanh());
            binding.themTg.setText(spsua.getTg());
            binding.themGia.setText(spsua.getGia()+"");
            binding.themMota.setText(spsua.getMota());
            binding.spinnerLoai.setSelection(spsua.getLoai());



        }

    }


    private void initData() {
        List<String> str_list = new ArrayList<>();
        str_list.add("Chọn loại sản phẩm");
        str_list.add("Melonbook");
        str_list.add("Goods");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, str_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.themNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(ThemSPActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                        String date = "";
                        if (m > 8) {
                            date = d + "/" + (m + 1) + "/" + y;
                        } else {
                            date = d + "/0" + (m + 1) + "/" + y;
                        }
                        binding.themNgay.setText(date);
                    }
                }, year, month, day);
                dialog.show();
            }
        });

        binding.btnthemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == false){
                    themsp();
                }else {
                    suasp();
                }

            }
        });

        binding.cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ThemSPActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });



    }

    private void suasp() {
        String str_ten = binding.themTen.getText().toString().trim();
        String str_tg = binding.themTg.getText().toString().trim();
        Date date = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(binding.themNgay.getText().toString().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String str_ngay = dateFormat1.format(date);;
        String str_gia = binding.themGia.getText().toString().trim();
        String str_hinhanh = binding.themHa.getText().toString().trim();
        String str_mota = binding.themMota.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten)||TextUtils.isEmpty(str_gia)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||TextUtils.isEmpty(str_ngay)||TextUtils.isEmpty(str_tg)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
        } else if (!str_gia.matches("\\d+")) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng định dạng của giá!", Toast.LENGTH_LONG).show();

        } else {
            compositeDisposable.add(apiBanHang.updatesp(str_ten, str_tg, str_ngay, str_gia, str_hinhanh, str_mota, loai, spsua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), "Sửa thành công!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }else {
                                    Toast.makeText(getApplicationContext(), "0 ok", Toast.LENGTH_LONG).show();

                                }

                            },throwable -> {

                                Toast.makeText(getApplicationContext(), "0 ok", Toast.LENGTH_LONG).show();
                            }
                    ));

        }
    }

    private String getPath(Uri uri){
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if(cursor == null){
            result = uri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();

        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult"+ mediaPath);
    }

    // Uploading Image/Video
    private void uploadMultipleFiles() {
        Uri uri = Uri.parse(mediaPath);
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(getPath(uri));
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file); // requestbody truyen nd tep
        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
        Call<MessageModel>call= apiBanHang.uploadFile1(fileToUpload1);
        if(flag==true){
            call = apiBanHang.uploadFile2(fileToUpload1, spsua.getId());
            Log.d("log", "onActivityResult"+ mediaPath);
        }
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call <MessageModel> call, Response<MessageModel> response) {
                MessageModel serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.isSuccess()) {
                        binding.themHa.setText(serverResponse.getName());
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call <MessageModel> call, Throwable t) {
                Log.d("log", t.getMessage());
            }
        });
    }

    private void themsp() {
        String str_ten = binding.themTen.getText().toString().trim();
        String str_tg = binding.themTg.getText().toString().trim();
        Date date = new Date();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(binding.themNgay.getText().toString().toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String str_ngay = dateFormat1.format(date);;
        String str_gia = binding.themGia.getText().toString().trim();
        String str_hinhanh = binding.themHa.getText().toString().trim();
        String str_mota = binding.themMota.getText().toString().trim();
        if (TextUtils.isEmpty(str_ten)||TextUtils.isEmpty(str_gia)||TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||TextUtils.isEmpty(str_ngay)||TextUtils.isEmpty(str_tg)){
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_LONG).show();
        } else if (!str_gia.matches("\\d+")) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng định dạng của giá", Toast.LENGTH_LONG).show();


        } else {
            compositeDisposable.add(apiBanHang.insertsp(str_ten, str_tg, str_ngay, str_gia, str_hinhanh, str_mota, loai)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if(messageModel.isSuccess()){

                                        Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);

                                }else {

                                        Toast.makeText(getApplicationContext(), "0 ok", Toast.LENGTH_LONG).show();

                                }

                            },throwable -> {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            }
                    ));

        }
    }


    private void initView() {
        spinner = findViewById(R.id.spinner_loai);

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