package com.example.btlcuoiki.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btlcuoiki.ChiTietActivity;
import com.example.btlcuoiki.R;
import com.example.btlcuoiki.interfacee.ItemClickListener;
import com.example.btlcuoiki.model.EventBus.SuaXoa;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.url.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class SPMoiAdapter extends RecyclerView.Adapter<SPMoiAdapter.MyViewHolder> {
    Context context;
    List<SP> array;

    public SPMoiAdapter(Context context, List<SP> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spmoi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SP sp= array.get(position);
        holder.txtten.setText(sp.getTen());
        holder.txttg.setText("Artist: "+sp.getTg());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sp.getGia()))+"vnđ");
        if(sp.getHinhanh().contains("http")){
            Glide.with(context).load(sp.getHinhanh()).into(holder.imghinhanh);
        }else{
            String hinh = Utils.BASE_URL+ "images/"+sp.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imghinhanh);

        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", sp);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtten, txttg, txtgia;
        ImageView imghinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtten = itemView.findViewById(R.id.itemsp_ten);
            txttg = itemView.findViewById(R.id.itemsp_tg);
            txtgia = itemView.findViewById(R.id.itemsp_gia);
            imghinhanh = itemView.findViewById(R.id.itemsp_img);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }


    }
}
