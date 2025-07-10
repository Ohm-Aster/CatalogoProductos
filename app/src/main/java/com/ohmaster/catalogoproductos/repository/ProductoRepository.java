package com.ohmaster.catalogoproductos.repository;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ohmaster.catalogoproductos.model.Producto;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class ProductoRepository {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public interface ProductoCallback {
        void onComplete(List<Producto> productos);
    }

    public void obtenerProductos(ProductoCallback callback) {
        firestore.collection("productos")
                .get()
                .addOnSuccessListener(query -> {
                    List<Producto> lista = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        Producto producto = doc.toObject(Producto.class);
                        if (producto != null) {
                            producto.setId(doc.getId());
                            lista.add(producto);
                        }
                    }
                    callback.onComplete(lista);
                });
    }

    public void agregarProducto(String nombre, double gramos, double costo, double precio, String tiempo, List<Uri> imagenes, Context context) {
        String id = generarIdSeguro(nombre);
        subirImagenes(imagenes, id, context, urls -> {
            Producto producto = new Producto(id, nombre, gramos, costo, precio, tiempo, urls);
            firestore.collection("productos").document(id).set(producto)
                    .addOnSuccessListener(unused -> Toast.makeText(context, "Producto guardado", Toast.LENGTH_SHORT).show());
        });
    }

    public void actualizarProducto(String id, String nombre, double gramos, double costo, double precio, String tiempo, List<Uri> imagenes, Context context) {
        subirImagenes(imagenes, id, context, urls -> {
            Producto producto = new Producto(id, nombre, gramos, costo, precio, tiempo, urls);
            firestore.collection("productos").document(id).set(producto)
                    .addOnSuccessListener(unused -> Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show());
        });
    }

    private void subirImagenes(List<Uri> imagenes, String productoId, Context context, ImagenesCallback callback) {
        if (imagenes == null || imagenes.isEmpty()) {
            callback.onUploadComplete(new ArrayList<>());
            return;
        }

        List<String> urls = new ArrayList<>();
        for (int i = 0; i < imagenes.size(); i++) {
            Uri uri = imagenes.get(i);
            String nombreArchivo = productoId + "/" + UUID.randomUUID();
            StorageReference ref = storage.getReference("productos").child(nombreArchivo);
            int finalI = i;

            ref.putFile(uri).continueWithTask(task -> ref.getDownloadUrl())
                    .addOnSuccessListener(uriDescargada -> {
                        urls.add(uriDescargada.toString());
                        if (urls.size() == imagenes.size()) {
                            callback.onUploadComplete(urls);
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show());
        }
    }

    public interface ImagenesCallback {
        void onUploadComplete(List<String> urls);
    }

    private String generarIdSeguro(String nombre) {
        String base = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "") // quitar acentos
                .replaceAll("[^a-zA-Z0-9_-]", "_") // reemplazar símbolos raros
                .toLowerCase(Locale.ROOT)
                .trim();

        return base + "_" + UUID.randomUUID().toString().substring(0, 8); // Ej: zapatos_nike_a1b2c3d4
    }

}
