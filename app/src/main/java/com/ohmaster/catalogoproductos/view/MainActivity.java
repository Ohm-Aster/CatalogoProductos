package com.ohmaster.catalogoproductos.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.ohmaster.catalogoproductos.R;
import com.ohmaster.catalogoproductos.adapter.ProductoAdapter;
import com.ohmaster.catalogoproductos.model.Producto;
import com.ohmaster.catalogoproductos.viewmodel.ProductoViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProductoAdapter adapter;
    private ListenerRegistration listener;
    private ProductoViewModel viewModel;
    private final ArrayList<Producto> productos = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageButton btnAgregar;

    private final ActivityResultLauncher<Intent> launcher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    viewModel.obtenerProductos();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerProductos);
        btnAgregar = findViewById(R.id.fabAgregar);

        adapter = new ProductoAdapter(productos, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(ProductoViewModel.class);
        viewModel.getProductosLiveData().observe(this, productos -> {
            this.productos.clear();
            this.productos.addAll(productos);
            adapter.notifyDataSetChanged();
        });

        viewModel.obtenerProductos();

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgregarProductoActivity.class);
            launcher.launch(intent);
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        listener = FirebaseFirestore.getInstance()
                .collection("productos")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    productos.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Producto p = doc.toObject(Producto.class);
                        p.setId(doc.getId());
                        productos.add(p);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) listener.remove();
    }
}
