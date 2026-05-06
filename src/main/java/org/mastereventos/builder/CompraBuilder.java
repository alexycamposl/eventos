package org.mastereventos.builder;

import org.mastereventos.model.Compra;
import org.mastereventos.model.Entrada;
import org.mastereventos.model.Usuario;
import org.mastereventos.model.Evento;

import java.util.ArrayList;
import java.util.List;

public class CompraBuilder {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;
    private double total;
    private String estado;

    public CompraBuilder() {
        this.entradas = new ArrayList<>();
        this.total = 0;
        this.estado = "Creada";
    }

    public CompraBuilder setIdCompra(String idCompra) {
        this.idCompra = idCompra;
        return this;
    }

    public CompraBuilder setUsuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public CompraBuilder setEvento(Evento evento) {
        this.evento = evento;
        return this;
    }

    public CompraBuilder agregarEntrada(Entrada entrada) {
        this.entradas.add(entrada);
        this.total += entrada.getPrecioFinal();
        return this;
    }

    public CompraBuilder agregarServicioVIP(double costo) {
        this.total += costo;
        return this;
    }

    public CompraBuilder agregarSeguro(double costo) {
        this.total += costo;
        return this;
    }

    public CompraBuilder agregarMerchandising(double costo) {
        this.total += costo;
        return this;
    }

    public CompraBuilder setEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public Compra build() {
        return new Compra(
                idCompra,
                usuario,
                evento,
                entradas,
                total,
                estado
        );
    }
}