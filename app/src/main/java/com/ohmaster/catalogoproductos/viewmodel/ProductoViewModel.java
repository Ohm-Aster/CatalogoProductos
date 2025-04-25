package com.ohmaster.catalogoproductos.viewmodel;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ohmaster.catalogoproductos.model.Producto;
import com.ohmaster.catalogoproductos.repository.ProductoRepository;

import java.util.List;

public class ProductoViewModel extends ViewModel {

    private final ProductoRepository repository = new ProductoRepository();
    private final MutableLiveData<List<Producto>> productosLiveData = new MutableLiveData<>();

    public LiveData<List<Producto>> getProductosLiveData() {
        return productosLiveData;
    }

    public void obtenerProductos() {
        repository.obtenerProductos(productosLiveData::setValue);
    }

    public void agregarProducto(String nombre, double costo, double precio, String tiempo, List<Uri> imagenes, Context context) {
        repository.agregarProducto(nombre, costo, precio, tiempo, imagenes, context);
    }

    public void actualizarProducto(String id, String nombre, double costo, double precio, String tiempo, List<Uri> imagenes, Context context) {
        repository.actualizarProducto(id, nombre, costo, precio, tiempo, imagenes, context);
    }
}
