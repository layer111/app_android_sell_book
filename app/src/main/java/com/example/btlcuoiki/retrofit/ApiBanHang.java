package com.example.btlcuoiki.retrofit;

import com.example.btlcuoiki.model.DanhgiaModel;
import com.example.btlcuoiki.model.DoanhthuModel;
import com.example.btlcuoiki.model.DonHangModel;
import com.example.btlcuoiki.model.LoaiSpModel;
import com.example.btlcuoiki.model.MessageModel;
import com.example.btlcuoiki.model.SPMoiModel;
import com.example.btlcuoiki.model.ThongKeModel;
import com.example.btlcuoiki.model.UserModel;
import com.google.type.DateTime;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
    @GET("getloaisp.php")
    io.reactivex.rxjava3.core.Observable<LoaiSpModel> getLoaiSp();
    //Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SPMoiModel> getSPMoi();

    @FormUrlEncoded
    @POST("thongke.php")
    Observable<ThongKeModel> getThongke(
            @Field("thang") int thang
    );
    @FormUrlEncoded
    @POST("thongkedoanhthu.php")
    Observable<DoanhthuModel> getThongkeDoanhthu(
            @Field("thang") int thang
    );

    @FormUrlEncoded
    @POST("saotb.php")
    Observable<DoanhthuModel> getsao(
            @Field("idsp") int idsp
    );

    @FormUrlEncoded
    @POST("getdanhgia.php")
    Observable<DanhgiaModel> getdanhgia(
            @Field("idsp") int idsp
    );



    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SPMoiModel> getSP(
            @Field("page") int page,
            @Field("loai") int loai

            );

    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangKi(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile


    );

    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangNhap(
            @Field("email") String email,
            @Field("pass") String pass

    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> creatOrder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("sl") int sl,
            @Field("tgmuahang")String tgmuahang,
            @Field("chitiet") String chitiet

            );

    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id

    );

    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SPMoiModel> timKiem(
            @Field("search") String search

    );

    @POST("searchbyprice.php")
    @FormUrlEncoded
    Observable<SPMoiModel> searchbyprice(
            @Field("gia1") String gia1,
            @Field("gia2") String gia2

    );

    @POST("xoa.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSP(
            @Field("id") int id

    );

    @POST("insertsp.php")
    @FormUrlEncoded
    Observable<MessageModel> insertsp(
            @Field("ten") String ten,
            @Field("tg") String tg,
            @Field("ngayph") String ngayph,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai

    );

    @POST("updatesp.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesp(
            @Field("ten") String ten,
            @Field("tg") String tg,
            @Field("ngayph") String ngayph,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai,
            @Field("id") int id

            );

    @POST("themdanhgia.php")
    @FormUrlEncoded
    Observable<MessageModel> danhgiasp(
            @Field("idsp") int idsp,
            @Field("iddonhang") int iddonhang,
            @Field("sao") int sao,
            @Field("danhgia") String danhgia
    );

    @Multipart
    @POST("upload.php")
    Call<MessageModel> uploadFile1(@Part MultipartBody.Part file);

    @Multipart
    @POST("uploadsua.php")
    Call<MessageModel> uploadFile2(@Part MultipartBody.Part file,
                                   @Part("id") int id);
}

