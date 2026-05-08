package org.mastereventos.repository;

import java.util.ArrayList;
import java.util.List;

import org.mastereventos.model.EstadoEvento;
import org.mastereventos.model.Evento;

public class EventoRepository {

    private static final List<Evento> eventos =
            new ArrayList<>();

    public EventoRepository() {

        if (!eventos.isEmpty()) {
            return;
        }

        eventos.add(new Evento(
                "E001",
                "Concierto Rock Nacional",
                "Concierto",
                "Evento musical con bandas nacionales.",
                "Bogotá",
                "2026-06-15 20:00",
                EstadoEvento.PUBLICADO
        ));

        eventos.add(new Evento(
                "E002",
                "Obra de Teatro Clásico",
                "Teatro",
                "Presentación teatral para público general.",
                "Medellín",
                "2026-07-10 19:00",
                EstadoEvento.PUBLICADO
        ));

        eventos.add(new Evento(
                "E003",
                "Conferencia de Tecnología",
                "Conferencia",
                "Charlas sobre innovación e IA.",
                "Cali",
                "2026-08-05 09:00",
                EstadoEvento.PUBLICADO
        ));
    }

    // =========================
    // LISTAR TODOS
    // =========================

    public List<Evento> listarEventos() {
        return eventos;
    }

    // =========================
    // LISTAR PUBLICADOS
    // =========================

    public List<Evento> listarEventosPublicados() {

        List<Evento> publicados =
                new ArrayList<>();

        for (Evento evento : eventos) {

            if (
                    evento.getEstado()
                            == EstadoEvento.PUBLICADO
            ) {

                publicados.add(evento);
            }
        }

        return publicados;
    }

    // =========================
    // FILTRAR
    // =========================

    public List<Evento> filtrarEventos(
            String ciudad,
            String categoria) {

        List<Evento> filtrados =
                new ArrayList<>();

        for (Evento evento : listarEventosPublicados()) {

            boolean coincideCiudad =

                    ciudad == null
                            ||
                            ciudad.isBlank()
                            ||
                            ciudad.equalsIgnoreCase("Todas")
                            ||
                            evento.getCiudad()
                                    .equalsIgnoreCase(ciudad);

            boolean coincideCategoria =

                    categoria == null
                            ||
                            categoria.isBlank()
                            ||
                            categoria.equalsIgnoreCase("Todas")
                            ||
                            evento.getCategoria()
                                    .equalsIgnoreCase(categoria);

            if (
                    coincideCiudad
                            &&
                            coincideCategoria
            ) {

                filtrados.add(evento);
            }
        }

        return filtrados;
    }
}