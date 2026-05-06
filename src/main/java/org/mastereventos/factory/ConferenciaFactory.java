package org.mastereventos.factory;

import org.mastereventos.model.Evento;

public class ConferenciaFactory extends EventoFactory {

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
                "Conferencia",
                descripcion,
                ciudad,
                fecha,
                estado
        );
    }
}