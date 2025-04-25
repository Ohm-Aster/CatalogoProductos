package com.ohmaster.catalogoproductos.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ohmaster.catalogoproductos.R;
import com.ohmaster.catalogoproductos.model.Producto;
import com.ohmaster.catalogoproductos.view.DetalleProductoActivity;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {

    private final List<Producto> productos;
    private final Context context;

    public ProductoAdapter(List<Producto> productos, Context context) {
        this.productos = productos;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvPrecio.setText("$" + producto.getPrecio());

        if (!producto.getImagenes().isEmpty()) {
            Glide.with(context).load(producto.getImagenes().get(0)).into(holder.imgProducto);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleProductoActivity.class);
            intent.putExtra("producto", producto);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvPrecio;
        ImageView imgProducto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}
