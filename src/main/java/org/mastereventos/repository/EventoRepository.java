package org.mastereventos.repository;

import java.util.ArrayList;
import java.util.List;

import org.mastereventos.model.Evento;

public class EventoRepository {

    private final List<Evento> eventos;

    public EventoRepository() {
        eventos = new ArrayList<>();

        eventos.add(new Evento(
                "E001",
                "Concierto Rock Nacional",
                "Concierto",
                "Evento musical con bandas nacionales.",
                "Bogotá",
                "2026-06-15 20:00",
                "Publicado"
        ));

        eventos.add(new Evento(
                "E002",
                "Obra de Teatro Clásico",
                "Teatro",
                "Presentación teatral para público general.",
                "Medellín",
                "2026-07-10 19:00",
                "Publicado"
        ));

        eventos.add(new Evento(
                "E003",
                "Conferencia de Tecnología",
                "Conferencia",
                "Charlas sobre innovación, software e inteligencia artificial.",
                "Cali",
                "2026-08-05 09:00",
                "Publicado"
        ));
    }

    public List<Evento> listarEventosPublicados() {
        List<Evento> publicados = new ArrayList<>();

        for (Evento evento : eventos) {
            if ("Publicado".equalsIgnoreCase(evento.getEstado())) {
                publicados.add(evento);
            }
        }

        return publicados;
    }

    public List<Evento> filtrarEventos(String ciudad, String categoria) {
        List<Evento> filtrados = new ArrayList<>();

        for (Evento evento : listarEventosPublicados()) {
            boolean coincideCiudad = ciudad == null
                    || ciudad.isBlank()
                    || "Todas".equalsIgnoreCase(ciudad)
                    || evento.getCiudad().equalsIgnoreCase(ciudad);

            boolean coincideCategoria = categoria == null
                    || categoria.isBlank()
                    || "Todas".equalsIgnoreCase(categoria)
                    || evento.getCategoria().equalsIgnoreCase(categoria);

            if (coincideCiudad && coincideCategoria) {
                filtrados.add(evento);
            }
        }

        return filtrados;
    }

    public List<Evento> getEventos() {
        return eventos;
    }
}
