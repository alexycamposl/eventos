package org.mastereventos.model;

public class Asiento {

    private String idAsiento;
    private String fila;
    private int numero;
    private EstadoAsiento estado;

    public Asiento(String idAsiento,
                   String fila,
                   int numero) {

        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    // GETTERS Y SETTERS

    public String getIdAsiento() {
        return idAsiento;
    }

    public void setIdAsiento(String idAsiento) {
        this.idAsiento = idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public EstadoAsiento getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsiento estado) {
        this.estado = estado;
    }

    // MÉTODOS

    public boolean estaDisponible() {
        return estado == EstadoAsiento.DISPONIBLE;
    }

    public void bloquear() {
        estado = EstadoAsiento.BLOQUEADO;
    }

    public void reservar() {
        estado = EstadoAsiento.RESERVADO;
    }

    public void vender() {
        estado = EstadoAsiento.VENDIDO;
    }

    public void liberar() {
        estado = EstadoAsiento.DISPONIBLE;
    }

    @Override
    public String toString() {
        return "Fila " + fila +
                " - Asiento " + numero +
                " (" + estado + ")";
    }
}