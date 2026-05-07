package org.mastereventos.model;

import java.util.List;

public class Compra {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;
    private double total;
    private EstadoCompra estado;

    public Compra(String idCompra,
                  Usuario usuario,
                  Evento evento,
                  List<Entrada> entradas) {

        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.entradas = entradas;
        this.total = calcularTotal();
        this.estado = EstadoCompra.CREADA;
    }

    // CÁLCULO AUTOMÁTICO

    private double calcularTotal() {

        double suma = 0;

        for (Entrada entrada : entradas) {
            suma += entrada.getPrecioFinal();
        }

        return suma;
    }

    // CAMBIOS DE ESTADO

    public void pagarCompra() {
        estado = EstadoCompra.PAGADA;
    }

    public void confirmarCompra() {
        estado = EstadoCompra.CONFIRMADA;
    }

    public void cancelarCompra() {
        estado = EstadoCompra.CANCELADA;
    }

    public void reembolsarCompra() {
        estado = EstadoCompra.REEMBOLSADA;
    }

    // GETTERS Y SETTERS

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
        this.total = calcularTotal();
    }

    public double getTotal() {
        return total;
    }

    public EstadoCompra getEstado() {
        return estado;
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id='" + idCompra + '\'' +
                ", usuario=" + usuario.getNombre() +
                ", evento=" + evento.getNombre() +
                ", total=" + total +
                ", estado=" + estado +
                '}';
    }
}