package com.ohmaster.catalogoproductos.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.ohmaster.catalogoproductos.R;
import com.ohmaster.catalogoproductos.model.Producto;
import com.ohmaster.catalogoproductos.viewmodel.ProductoViewModel;

import java.util.ArrayList;
import java.util.List;

public class AgregarProductoActivity extends AppCompatActivity {

    private EditText etNombre, etGramos, etCosto, etPrecio, etTiempo;
    private Button btnSeleccionarImagenes, btnGuardar;
    private ProductoViewModel viewModel;
    private Producto productoEditar = null;
    private final List<Uri> imagenesSeleccionadas = new ArrayList<>();

    private final ActivityResultLauncher<Intent> selectorImagenes =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    if (result.getData().getClipData() != null) {
                        int count = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < count; i++) {
                            Uri uri = result.getData().getClipData().getItemAt(i).getUri();
                            imagenesSeleccionadas.add(uri);
                        }
                    } else if (result.getData().getData() != null) {
                        imagenesSeleccionadas.add(result.getData().getData());
                    }

                    Toast.makeText(this, imagenesSeleccionadas.size() + " imágenes seleccionadas", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        etNombre = findViewById(R.id.etNombre);
        etGramos = findViewById(R.id.etGramos);
        etCosto = findViewById(R.id.etCosto);
        etPrecio = findViewById(R.id.etPrecio);
        etTiempo = findViewById(R.id.etTiempo);
        btnSeleccionarImagenes = findViewById(R.id.btnSeleccionarImagenes);
        btnGuardar = findViewById(R.id.btnGuardar);

        viewModel = new ViewModelProvider(this).get(ProductoViewModel.class);

        if (getIntent().hasExtra("producto")) {
            productoEditar = (Producto) getIntent().getSerializableExtra("producto");
            etNombre.setText(productoEditar.getNombre());
            etGramos.setText(String.valueOf(productoEditar.getGramos()));
            etCosto.setText(String.valueOf(productoEditar.getCosto()));
            etPrecio.setText(String.valueOf(productoEditar.getPrecio()));
            etTiempo.setText(productoEditar.getTiempo());
        }

        btnSeleccionarImagenes.setOnClickListener(v -> abrirGaleria());

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String gramos = etGramos.getText().toString();
            String costo = etCosto.getText().toString();
            String precio = etPrecio.getText().toString();
            String tiempo = etTiempo.getText().toString();

            if (nombre.isEmpty() || costo.isEmpty() || precio.isEmpty() || tiempo.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double gramosD = Double.parseDouble(gramos);
            double costoD = Double.parseDouble(costo);
            double precioD = Double.parseDouble(precio);

            if (productoEditar == null) {
                viewModel.agregarProducto(nombre, gramosD, costoD, precioD, tiempo, imagenesSeleccionadas, this);
            } else {
                viewModel.actualizarProducto(productoEditar.getId(), nombre, gramosD, costoD, precioD, tiempo, imagenesSeleccionadas, this);
            }

            finish();
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        selectorImagenes.launch(Intent.createChooser(intent, "Selecciona imágenes"));
    }
}
