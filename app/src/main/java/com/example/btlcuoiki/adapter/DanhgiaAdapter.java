package com.example.btlcuoiki.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btlcuoiki.R;
import com.example.btlcuoiki.model.Danhgia;
import com.example.btlcuoiki.model.Item;

import java.util.List;

public class DanhgiaAdapter extends RecyclerView.Adapter<DanhgiaAdapter.MyViewHolder>{
    List<Danhgia> danhgiaList;
    Context context;

    public DanhgiaAdapter(Context context, List<Danhgia> danhgiaList) {
        this.danhgiaList = danhgiaList;
        this.context = context;
    }

    @NonNull
    @Override
    public DanhgiaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhgia, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull DanhgiaAdapter.MyViewHolder holder, int position) {
        Danhgia danhgia = danhgiaList.get(position);
        holder.username.setText(danhgia.getUsername());
        holder.txtdanhgia.setText(danhgia.getDanhgia());
        holder.sao.setRating(danhgia.getSao());


    }

    @Override
    public int getItemCount() {
        return danhgiaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdanhgia, username;
        RatingBar sao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdanhgia = itemView.findViewById(R.id.txtdanhgia);
            username = itemView.findViewById(R.id.username);
            sao = itemView.findViewById(R.id.sao);

        }
    }
}
