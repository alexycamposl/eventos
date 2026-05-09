package org.mastereventos.repository;

import org.mastereventos.model.Evento;
import org.mastereventos.singleton.DataStore;

import java.util.ArrayList;
import java.util.List;

public class EventoRepository {

    private final List<Evento> eventos = DataStore.getInstancia().getEventos();

    public List<Evento> getEventos() {
        return eventos;
    }

    public List<Evento> listarEventosPublicados() {
        List<Evento> publicados = new ArrayList<>();
        for (Evento e : eventos) {
            if ("Publicado".equalsIgnoreCase(e.getEstado())) publicados.add(e);
        }
        return publicados;
    }

    public List<Evento> filtrarEventos(String ciudad, String categoria) {
        List<Evento> resultado = new ArrayList<>();
        for (Evento e : listarEventosPublicados()) {
            boolean coincideCiudad = ciudad == null || ciudad.isBlank()
                    || "Todas".equalsIgnoreCase(ciudad)
                    || e.getCiudad().equalsIgnoreCase(ciudad);
            boolean coincideCategoria = categoria == null || categoria.isBlank()
                    || "Todas".equalsIgnoreCase(categoria)
                    || e.getCategoria().equalsIgnoreCase(categoria);
            if (coincideCiudad && coincideCategoria) resultado.add(e);
        }
        return resultado;
    }

    public List<Evento> filtrarEventosAdmin(String ciudad, String categoria, String estado) {
        List<Evento> resultado = new ArrayList<>();
        for (Evento e : eventos) {
            boolean coincideCiudad = ciudad == null || ciudad.isBlank()
                    || "Todas".equalsIgnoreCase(ciudad)
                    || e.getCiudad().equalsIgnoreCase(ciudad);
            boolean coincideCategoria = categoria == null || categoria.isBlank()
                    || "Todas".equalsIgnoreCase(categoria)
                    || e.getCategoria().equalsIgnoreCase(categoria);
            boolean coincideEstado = estado == null || estado.isBlank()
                    || "Todos".equalsIgnoreCase(estado)
                    || e.getEstado().equalsIgnoreCase(estado);
            if (coincideCiudad && coincideCategoria && coincideEstado) resultado.add(e);
        }
        return resultado;
    }

    public Evento buscarPorId(String idEvento) {
        for (Evento e : eventos) {
            if (e.getIdEvento().equals(idEvento)) return e;
        }
        return null;
    }

    public void guardar(Evento evento) {
        for (int i = 0; i < eventos.size(); i++) {
            if (eventos.get(i).getIdEvento().equals(evento.getIdEvento())) {
                eventos.set(i, evento);
                return;
            }
        }
        eventos.add(evento);
    }

    public void eliminar(String idEvento) {
        eventos.removeIf(e -> e.getIdEvento().equals(idEvento));
    }

    public void cambiarEstado(String idEvento, String nuevoEstado) {
        Evento e = buscarPorId(idEvento);
        if (e != null) e.setEstado(nuevoEstado);
    }

    public String generarNuevoId() {
        int max = eventos.stream()
                .mapToInt(e -> {
                    try { return Integer.parseInt(e.getIdEvento().replace("E", "")); }
                    catch (NumberFormatException ex) { return 0; }
                })
                .max().orElse(0);
        return "E" + String.format("%03d", max + 1);
    }
}
