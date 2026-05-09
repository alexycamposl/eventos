package org.mastereventos.model;

import java.util.ArrayList;
import java.util.List;

public class Zona {

    private String idZona;
    private String nombre;
    private int capacidad;
    private double precioBase;
    private List<Asiento> asientos;

    public Zona(String idZona,
                String nombre,
                int capacidad,
                double precioBase) {

        this.idZona = idZona;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.asientos = new ArrayList<>();
    }

    // MÉTODOS

    public void agregarAsiento(Asiento asiento) {
        asientos.add(asiento);
    }

    public void eliminarAsiento(Asiento asiento) {
        asientos.remove(asiento);
    }

    public int obtenerOcupacion() {

        int ocupados = 0;

        for (Asiento asiento : asientos) {

            if (asiento.getEstado() != EstadoAsiento.DISPONIBLE) {
                ocupados++;
            }
        }

        return ocupados;
    }

    // GETTERS Y SETTERS

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }

    public void setAsientos(List<Asiento> asientos) {
        this.asientos = asientos;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precioBase;
    }
}