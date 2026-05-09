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

    public void agregarAsiento(Asiento asiento) { asientos.add(asiento); }
    public void eliminarAsiento(String idAsiento) {
        asientos.removeIf(a -> a.getIdAsiento().equals(idAsiento));
    }

    public List<Asiento> getAsientos() { return asientos; }

    public String getIdZona() { return idZona; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public int contarVendidos() {
        int count = 0;
        for (Asiento a : asientos) {
            if ("Vendido".equalsIgnoreCase(a.getEstado())) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return idZona + " - " + nombre + " (cap: " + capacidad + ") - $" + precioBase;
    }
}
