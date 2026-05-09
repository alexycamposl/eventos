package org.mastereventos.singleton;

import org.mastereventos.model.Compra;
import org.mastereventos.model.Evento;
import org.mastereventos.model.Incidencia;
import org.mastereventos.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class SistemaEventos {

    private static SistemaEventos instancia;

    private final List<Usuario> usuarios;
    private final List<Evento> eventos;
    private final List<Compra> compras;
    private final List<Incidencia> incidencias;

    private SistemaEventos() {

        usuarios = new ArrayList<>();
        eventos = new ArrayList<>();
        compras = new ArrayList<>();
        incidencias = new ArrayList<>();
    }

    public static SistemaEventos getInstancia() {

        if (instancia == null) {
            instancia = new SistemaEventos();
        }

        return instancia;
    }

    // =========================
    // USUARIOS
    // =========================

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    // =========================
    // EVENTOS
    // =========================

    public void agregarEvento(Evento evento) {
        eventos.add(evento);
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    // COMPRAS

    public void agregarCompra(Compra compra) {
        compras.add(compra);
    }

    public List<Compra> getCompras() {
        return compras;
    }

    // INCIDENCIAS

    public void agregarIncidencia(Incidencia incidencia) {
        incidencias.add(incidencia);
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }
}