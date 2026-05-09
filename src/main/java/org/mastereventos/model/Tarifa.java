package org.mastereventos.model;

public class Tarifa {

    private String idTarifa;
    private String nombre;
    private String descripcion;
    private double precio;

    public Tarifa(String idTarifa, String nombre, String descripcion, double precio) {
        this.idTarifa = idTarifa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getIdTarifa() { return idTarifa; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
