package com.example.btlcuoiki.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlcuoiki.R;
import com.example.btlcuoiki.interfacee.ImageClickListener;
import com.example.btlcuoiki.model.EventBus.TinhTongEvent;
import com.example.btlcuoiki.model.GioHang;
import com.example.btlcuoiki.url.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> giohangList;

    public GioHangAdapter(Context context, List<GioHang> giohangList) {
        this.context = context;
        this.giohangList = giohangList;
    }

    @NonNull
    @Override
    public GioHangAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new GioHangAdapter.MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangAdapter.MyViewHolder holder, int position) {
        GioHang gioHang = giohangList.get(position);
        holder.ten.setText(gioHang.getTen());
        holder.sl.setText(gioHang.getSolg()+" ");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.gia.setText("Giá: "+decimalFormat.format(gioHang.getGia())+"vnđ");
        if(gioHang.getHinhanh().contains("http")){
            Glide.with(context).load(gioHang.getHinhanh()).into(holder.img);
        }else{
            String hinh = Utils.BASE_URL+ "images/"+gioHang.getHinhanh();
            Glide.with(context).load(hinh).into(holder.img);

        }
        long gia = gioHang.getSolg() * gioHang.getGia();
        holder.tong.setText(decimalFormat.format(gia)+"vnđ");
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Utils.mangmuahang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());

                }else {
                    for (int i=0; i<Utils.mangmuahang.size(); i++){
                        if(Utils.mangmuahang.get(i).getId() == gioHang.getId()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }

                }
            }
        });
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1){
                    if(giohangList.get(pos).getSolg()>1){
                        int slmoi = giohangList.get(pos).getSolg() - 1;
                        giohangList.get(pos).setSolg(slmoi);
                    }else if(giohangList.get(pos).getSolg()==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();

                    }
                }else if(giatri == 2){
                    int slmoi = giohangList.get(pos).getSolg() + 1;
                    giohangList.get(pos).setSolg(slmoi);
                }
                holder.sl.setText(giohangList.get(pos).getSolg()+" ");
                long gia = giohangList.get(pos).getSolg() * giohangList.get(pos).getGia();
                holder.tong.setText(decimalFormat.format(gia)+"vnđ");
                EventBus.getDefault().postSticky(new TinhTongEvent());
            }

        });
    }

    @Override
    public int getItemCount() {
        return giohangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img, tru, cong;
        TextView ten, gia, sl, tong, slgio;
        ImageClickListener imageClickListener;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tru = itemView.findViewById(R.id.item_giohang_tru);
            cong = itemView.findViewById(R.id.item_giohang_cong);
            slgio = itemView.findViewById(R.id.item_giohang_sl);
            img = itemView.findViewById(R.id.item_giohang_img);
            ten = itemView.findViewById(R.id.item_giohang_ten);
            gia = itemView.findViewById(R.id.item_giohang_gia);
            sl = itemView.findViewById(R.id.item_giohang_sl);
            tong = itemView.findViewById(R.id.item_giohang_gia1);
            checkBox = itemView.findViewById(R.id.item_giohang_checkbox);

            // event click
            cong.setOnClickListener(this);
            tru.setOnClickListener(this);
        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        @Override
        public void onClick(View view) {
            if(view == tru){
                imageClickListener.onImageClick(view, getAdapterPosition(), 1);
            }else if (view == cong){
                imageClickListener.onImageClick(view, getAdapterPosition(), 2);
            }
        }
    }
}
