package org.mastereventos.factory;

import org.mastereventos.model.EstadoEvento;
import org.mastereventos.model.Evento;

public abstract class EventoFactory {

    public abstract Evento crearEvento(
            String id,
            String nombre,
            String descripcion,
            String ciudad,
            String fecha,
            EstadoEvento estado
    );
}