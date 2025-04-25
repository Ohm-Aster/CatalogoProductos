package com.ohmaster.catalogoproductos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ohmaster.catalogoproductos.R;

import java.util.List;

public class ImagenesAdapter extends RecyclerView.Adapter<ImagenesAdapter.ViewHolder> {

    private final List<String> urls;
    private final Context context;

    public ImagenesAdapter(List<String> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @NonNull
    @Override
    public ImagenesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_imagen, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagenesAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(urls.get(position)).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCarrusel);
        }
    }
}
