package org.mastereventos.builder;

import org.mastereventos.model.Compra;
import org.mastereventos.model.Entrada;
import org.mastereventos.model.Evento;
import org.mastereventos.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class CompraBuilder {

    private String idCompra;
    private Usuario usuario;
    private Evento evento;
    private List<Entrada> entradas;

    public CompraBuilder() {
        this.entradas = new ArrayList<>();
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
        return this;
    }

    public Compra build() {

        return new Compra(
                idCompra,
                usuario,
                evento,
                entradas
        );
    }
}