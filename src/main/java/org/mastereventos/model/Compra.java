package org.mastereventos.model;



import java.util.List;

public class Compra {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;
    private double total;
    private String estado;

    public Compra(String idCompra, Usuario usuario, Evento evento,
                  List<Entrada> entradas, double total, String estado) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.entradas = entradas;
        this.total = total;
        this.estado = estado;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }
}
