package org.mastereventos.factory;

import org.mastereventos.model.EstadoEvento;
import org.mastereventos.model.Evento;

public class TeatroFactory extends EventoFactory {

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
                "Teatro",
                descripcion,
                ciudad,
                fecha,
                estado
        );
    }
}
//coman