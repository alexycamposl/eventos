package org.mastereventos.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;
    private double total;
    private String estado;
    private String fechaCreacion;
    private List<String> serviciosAdicionales;

    public Compra(String idCompra, Usuario usuario, Evento evento,
                  List<Entrada> entradas, double total, String estado) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.entradas = entradas;
        this.total = total;
        this.estado = estado;
        this.fechaCreacion = LocalDate.now().toString();
        this.serviciosAdicionales = new ArrayList<>();
    }

    public Compra(String idCompra, Usuario usuario, Evento evento,
                  List<Entrada> entradas, double total, String estado,
                  String fechaCreacion, List<String> serviciosAdicionales) {
        this(idCompra, usuario, evento, entradas, total, estado);
        this.fechaCreacion = fechaCreacion;
        this.serviciosAdicionales = serviciosAdicionales != null
                ? serviciosAdicionales : new ArrayList<>();
    }

    public String getIdCompra() { return idCompra; }
    public Usuario getUsuario() { return usuario; }
    public Evento getEvento() { return evento; }
    public List<Entrada> getEntradas() { return entradas; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public List<String> getServiciosAdicionales() { return serviciosAdicionales; }
    public void setServiciosAdicionales(List<String> s) {
        this.serviciosAdicionales = s != null ? s : new ArrayList<>();
    }

    @Override
    public String toString() {
        String nombreEvento = evento != null ? evento.getNombre() : "Sin evento";
        return idCompra + " | " + nombreEvento + " | $" + total + " | " + estado;
    }
}
