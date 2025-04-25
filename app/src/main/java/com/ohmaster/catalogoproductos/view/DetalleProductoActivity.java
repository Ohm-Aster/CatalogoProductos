package com.ohmaster.catalogoproductos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ohmaster.catalogoproductos.R;
import com.ohmaster.catalogoproductos.adapter.ImagenesAdapter;
import com.ohmaster.catalogoproductos.model.Producto;

public class DetalleProductoActivity extends AppCompatActivity {

    private TextView tvNombre, tvPrecio, tvCosto, tvTiempo;
    private ViewPager2 viewPager;
    private Button btnEditar, btnEliminar;
    private Producto producto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);

        tvNombre = findViewById(R.id.tvNombre);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvCosto = findViewById(R.id.tvCosto);
        tvTiempo = findViewById(R.id.tvTiempo);
        viewPager = findViewById(R.id.viewPagerImagenes);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);

        producto = (Producto) getIntent().getSerializableExtra("producto");

        if (producto != null) {
            tvNombre.setText(producto.getNombre());
            tvPrecio.setText("Precio: $" + producto.getPrecio());
            tvCosto.setText("Costo: $" + producto.getCosto());
            tvTiempo.setText("Tiempo: " + producto.getTiempo());
            viewPager.setAdapter(new ImagenesAdapter(producto.getImagenes(), this));
        }

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgregarProductoActivity.class);
            intent.putExtra("producto", producto);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> confirmarEliminacion());
    }

    private void confirmarEliminacion() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Estás seguro de que deseas eliminar este producto?")
                .setPositiveButton("Sí", (dialog, which) -> eliminarProducto())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarProducto() {
        FirebaseFirestore.getInstance()
                .collection("productos")
                .document(producto.getId())
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}
