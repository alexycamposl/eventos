package org.mastereventos.model;



public class Incidencia {

    private String idIncidencia;
    private String tipo;
    private String descripcion;
    private String fecha;
    private String entidadAfectada;

    public Incidencia(String idIncidencia, String tipo,
                      String descripcion, String fecha,
                      String entidadAfectada) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.entidadAfectada = entidadAfectada;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }
}