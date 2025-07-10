package com.ohmaster.catalogoproductos.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ohmaster.catalogoproductos.R;
import com.ohmaster.catalogoproductos.model.Producto;
import com.ohmaster.catalogoproductos.view.AgregarProductoActivity;
import com.ohmaster.catalogoproductos.view.DetalleProductoActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ViewHolder holder, int position) {
        Producto producto = productos.get(position);
        holder.txtNombre.setText(producto.getNombre());
        holder.txtPrecio.setText("$" + producto.getPrecio());

        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
            Glide.with(context).load(producto.getImagenes().get(0)).into(holder.imgProducto);
        }
       holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleProductoActivity.class);
            intent.putExtra("producto", producto);
            context.startActivity(intent);
        });

        holder.btnBorrar.setOnClickListener(v -> eliminarProducto(producto));

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

    public void eliminarProducto(Producto producto) {
        FirebaseFirestore.getInstance().collection("productos")
                .document(producto.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
                        for (String url : producto.getImagenes()) {
                            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                            ref.delete();
                        }
                    }
                    Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    // YA NO remover manualmente de productos ni llamar a notifyItemRemoved(index)
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                );
    }

   /* public void eliminarProducto(Producto producto) {
        int index = productos.indexOf(producto);

        if (index >= 0 && index < productos.size()) {
            FirebaseFirestore.getInstance().collection("productos")
                    .document(producto.getId())
                    .delete()
                    .addOnSuccessListener(unused -> {
                        // Eliminar imágenes del Storage
                        if (producto.getImagenes() != null && !producto.getImagenes().isEmpty()) {
                            for (String url : producto.getImagenes()) {
                                StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(url);
                                ref.delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Imagen eliminada con éxito (opcional)
                                        })
                                        .addOnFailureListener(e -> {
                                            // Manejo de error (opcional)
                                        });
                            }
                        }

                        // Ahora eliminamos el producto de la lista
                        if (index >= 0 && index < productos.size()) {
                            productos.remove(index);
                            notifyItemRemoved(index);
                        }
                        Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Error al eliminar producto", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(context, "Error: producto no encontrado", Toast.LENGTH_SHORT).show();
        }
    }
*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtPrecio;
        CircleImageView imgProducto;
        ImageButton btnBorrar, btnEditar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.tvNombre);
            txtPrecio = itemView.findViewById(R.id.tvPrecio);
            imgProducto = itemView.findViewById(R.id.imgProducto);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            btnEditar = itemView.findViewById(R.id.btnEditar);
        }
    }
}
