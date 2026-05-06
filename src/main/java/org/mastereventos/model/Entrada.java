package org.mastereventos.model;



public class Entrada {

    private String idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private String estado;

    public Entrada(String idEntrada, Zona zona, Asiento asiento,
                   double precioFinal, String estado) {
        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = estado;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}