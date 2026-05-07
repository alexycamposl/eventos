package org.mastereventos.model;

public class Entrada {

    private String idEntrada;
    private Zona zona;
    private Asiento asiento;
    private double precioFinal;
    private EstadoEntrada estado;

    public Entrada(String idEntrada,
                   Zona zona,
                   Asiento asiento,
                   double precioFinal) {

        this.idEntrada = idEntrada;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estado = EstadoEntrada.ACTIVA;
    }

    // GETTERS Y SETTERS

    public String getIdEntrada() {
        return idEntrada;
    }

    public void setIdEntrada(String idEntrada) {
        this.idEntrada = idEntrada;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

    public double getPrecioFinal() {
        return precioFinal;
    }

    public void setPrecioFinal(double precioFinal) {
        this.precioFinal = precioFinal;
    }

    public EstadoEntrada getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntrada estado) {
        this.estado = estado;
    }

    // MÉTODOS

    public void usarEntrada() {
        estado = EstadoEntrada.USADA;
    }

    public void anularEntrada() {
        estado = EstadoEntrada.ANULADA;
    }

    @Override
    public String toString() {
        return "Entrada{" +
                "zona=" + zona.getNombre() +
                ", asiento=" + asiento +
                ", precio=" + precioFinal +
                ", estado=" + estado +
                '}';
    }
}