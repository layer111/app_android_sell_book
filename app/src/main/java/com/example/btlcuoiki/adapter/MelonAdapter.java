package com.example.btlcuoiki.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.btlcuoiki.ChiTietActivity;
import com.example.btlcuoiki.R;
import com.example.btlcuoiki.interfacee.ItemClickListener;
import com.example.btlcuoiki.model.EventBus.SuaXoa;
import com.example.btlcuoiki.model.SP;
import com.example.btlcuoiki.url.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MelonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SP> array;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_LOADING = 1;



    public MelonAdapter(Context context, List<SP> array) {
        this.context = context;
        this.array = array;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_melonbook, parent, false);
            return new MyViewHolder(item);
        }else {
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder=(MyViewHolder) holder;
            SP sp= array.get(position);
           //if(sp.getLoai()==1)
            myViewHolder.tensp.setText(sp.getTen());
            myViewHolder.ngay.setText("Ngày:"+sp.getNgayph());
            myViewHolder.tg.setText("Artist: "+sp.getTg());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.gia.setText("Giá: "+decimalFormat.format(Double.parseDouble(sp.getGia()))+"vnđ");
            if(sp.getHinhanh().contains("http")){
                Glide.with(context).load(sp.getHinhanh()).into(myViewHolder.img);
            }else{
                String hinh = Utils.BASE_URL+ "images/"+sp.getHinhanh();
                Glide.with(context).load(hinh).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(myViewHolder.img);

            }
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", sp);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } else if (Utils.user_current.getAdmin()==1) {
                        EventBus.getDefault().postSticky(new SuaXoa(sp));

                    }
                }
            });
        }else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return array.get(position) == null? VIEW_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.processbar);

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView tensp, gia, tg, ngay;
        ImageView img;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tensp = itemView.findViewById(R.id.itemb_ten);
            gia = itemView.findViewById(R.id.itemb_gia);
            tg = itemView.findViewById(R.id.itemb_tg);
            ngay = itemView.findViewById(R.id.itemb_ngay);
            img = itemView.findViewById(R.id.itemb_img);
            itemView.setOnClickListener(this);
            if(Utils.user_current.getAdmin()==1){
                itemView.setOnCreateContextMenuListener(this);
                itemView.setOnLongClickListener(this);
            }
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, getAdapterPosition(), "Sửa");
            contextMenu.add(0, 1, getAdapterPosition(), "Xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            if (Utils.user_current.getAdmin()==1) {
                itemClickListener.onClick(view, getAdapterPosition(), true);
            }
            return false;
        }
    }





}
