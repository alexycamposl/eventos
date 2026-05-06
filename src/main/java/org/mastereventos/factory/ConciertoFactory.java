package org.mastereventos.factory;

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
            String estado
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