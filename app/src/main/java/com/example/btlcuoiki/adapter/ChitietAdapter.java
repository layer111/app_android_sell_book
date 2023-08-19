package com.example.btlcuoiki.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlcuoiki.DanhgiaActivity;
import com.example.btlcuoiki.R;
import com.example.btlcuoiki.model.Item;
import com.example.btlcuoiki.retrofit.ApiBanHang;
import com.example.btlcuoiki.url.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class ChitietAdapter extends RecyclerView.Adapter<ChitietAdapter.MyViewHolder> {
    List<Item> itemList;
    Context context;
    ApiBanHang apiBanHang;


    public ChitietAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public ChitietAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ChitietAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ChitietAdapter.MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtten.setText(item.getTen()+"");
        if(item.getSao()!=0){
            holder.danhgia.setVisibility(View.GONE);
        }else{
            holder.danhgia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent= new Intent(context, DanhgiaActivity.class);
                    intent.putExtra("item", item);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }

        holder.txtsl.setText("Số lượng: "+ item.getSl());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgia.setText("Giá: "+decimalFormat.format(Double.parseDouble(item.getGia())*item.getSl())+"vnđ");
        if(item.getHinhanh().contains("http")){
            Glide.with(context).load(item.getHinhanh()).into(holder.imgchitiet);
        }else{
            String hinh = Utils.BASE_URL+ "images/"+item.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imgchitiet);

        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgchitiet;
        AppCompatButton danhgia;
        TextView txtten, txtsl, txtgia;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            danhgia = itemView.findViewById(R.id.danhgia);
            imgchitiet = itemView.findViewById(R.id.item_imgchitiet);
            txtten = itemView.findViewById(R.id.item_tenchitiet);
            txtsl = itemView.findViewById(R.id.item_slchitiet);
            txtgia = itemView.findViewById(R.id.item_giachitiet);
        }
    }
}
