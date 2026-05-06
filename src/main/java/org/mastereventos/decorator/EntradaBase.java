package org.mastereventos.decorator;

public class EntradaBase implements EntradaComponent {

    private final double precioBase;
    private final String descripcion;

    public EntradaBase(double precioBase, String descripcion) {
        this.precioBase = precioBase;
        this.descripcion = descripcion;
    }

    @Override
    public double getCosto() {
        return precioBase;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }
}