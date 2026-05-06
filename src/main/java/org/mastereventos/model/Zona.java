package org.mastereventos.model;



import java.util.ArrayList;
import java.util.List;

public class Zona {

    private String idZona;
    private String nombre;
    private int capacidad;
    private double precioBase;
    private List<Asiento> asientos;

    public Zona(String idZona, String nombre, int capacidad, double precioBase) {
        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.asientos = new ArrayList<>();
    }

    public void agregarAsiento(Asiento asiento) {
        asientos.add(asiento);
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precioBase;
    }

}