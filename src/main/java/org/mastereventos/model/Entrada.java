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

    public String getIdEntrada() { return idEntrada; }
    public Zona getZona() { return zona; }
    public Asiento getAsiento() { return asiento; }
    public double getPrecioFinal() { return precioFinal; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        String zonaStr = zona != null ? zona.getNombre() : "Sin zona";
        String asientoStr = asiento != null ? asiento.toString() : "Sin asiento";
        return idEntrada + " | " + zonaStr + " | " + asientoStr + " | $" + precioFinal + " | " + estado;
    }
}
