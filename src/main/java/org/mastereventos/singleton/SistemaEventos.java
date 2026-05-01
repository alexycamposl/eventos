package org.mastereventos;



import com.cristhianlopez.model.Evento;
import com.cristhianlopez.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class SistemaEventos {

    private static SistemaEventos instancia;

    private final List<Usuario> usuarios;
    private final List<Evento> eventos;

    private SistemaEventos() {
        usuarios = new ArrayList<>();
        eventos = new ArrayList<>();
    }

    public static SistemaEventos getInstancia() {
        if (instancia == null) {
            instancia = new SistemaEventos();
        }
        return instancia;
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void agregarEvento(Evento evento) {
        eventos.add(evento);
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}
