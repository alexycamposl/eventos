package org.mastereventos.model;

import java.util.List;

import org.mastereventos.state.CompraCancelada;
import org.mastereventos.state.CompraConfirmada;
import org.mastereventos.state.CompraContext;
import org.mastereventos.state.CompraCreada;
import org.mastereventos.state.CompraPagada;
import org.mastereventos.state.CompraReembolsada;

public class Compra {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;
    private double total;

    // STATE
    private CompraContext estadoContext;

    public Compra(
            String idCompra,
            Usuario usuario,
            Evento evento,
            List<Entrada> entradas
    ) {

        this.idCompra = idCompra;
        this.usuario = usuario;
        this.evento = evento;
        this.entradas = entradas;

        this.total = calcularTotal();

        // ESTADO INICIAL
        estadoContext = new CompraContext();

        estadoContext.setEstado(
                new CompraCreada()
        );
    }


    // CÁLCULO TOTAL

    private double calcularTotal() {

        double suma = 0;

        for (Entrada entrada : entradas) {

            suma += entrada.getPrecioFinal();
        }

        return suma;
    }


    // CAMBIOS DE ESTADO

    public void pagarCompra() {

        estadoContext.setEstado(
                new CompraPagada()
        );
    }

    public void confirmarCompra() {

        estadoContext.setEstado(
                new CompraConfirmada()
        );
    }

    public void cancelarCompra() {

        estadoContext.setEstado(
                new CompraCancelada()
        );
    }

    public void reembolsarCompra() {

        estadoContext.setEstado(
                new CompraReembolsada()
        );
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================

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

    // OBTENER ESTADO ACTUAL

    public String getEstado() {

        return estadoContext.getEstadoActual();
    }

    @Override
    public String toString() {

        return "Compra{" +
                "id='" + idCompra + '\'' +
                ", usuario=" + usuario.getNombre() +
                ", evento=" + evento.getNombre() +
                ", total=" + total +
                ", estado=" + getEstado() +
                '}';
    }
}