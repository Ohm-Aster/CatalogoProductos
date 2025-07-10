package com.ohmaster.catalogoproductos.model;

import java.io.Serializable;
import java.util.List;

public class Producto implements Serializable {

    private String id;
    private String nombre;
    private double gramos;
    private double costo;
    private double precio;
    private String tiempo;
    private List<String> imagenes;

    public Producto() {
        // Requerido para Firebase
    }

    public Producto(String id, String nombre, double gramos, double costo, double precio, String tiempo, List<String> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.gramos = gramos;
        this.costo = costo;
        this.precio = precio;
        this.tiempo = tiempo;
        this.imagenes = imagenes;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
    public double getGramos() { return gramos; }

    public double getCosto() {
        return costo;
    }

    public double getPrecio() {
        return precio;
    }

    public String getTiempo() {
        return tiempo;
    }

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setGramos(double gramos) {
        this.costo = gramos;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }
}
