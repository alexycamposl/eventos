package org.mastereventos.models;


public class Asiento {

    private String idAsiento;
    private String fila;
    private int numero;
    private String estado;

    public Asiento(String idAsiento, String fila, int numero, String estado) {
        this.idAsiento = idAsiento;
        this.fila = fila;
        this.numero = numero;
        this.estado = estado;
    }

    public String getIdAsiento() {
        return idAsiento;
    }

    public String getFila() {
        return fila;
    }

    public int getNumero() {
        return numero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}