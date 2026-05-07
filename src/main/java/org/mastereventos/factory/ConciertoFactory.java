package org.mastereventos.factory;

import org.mastereventos.model.EstadoEvento;
import org.mastereventos.model.Evento;

public class ConciertoFactory extends EventoFactory {

    @Override
    public Evento crearEvento(
            String id,
            String nombre,
            String categoria,
            String descripcion,
            String ciudad,
            String fecha,
            EstadoEvento estado
    ) {

        return new Evento(
                id,
                nombre,
                "Concierto",
                descripcion,
                ciudad,
                fecha,
                estado
        );
    }
}