package org.mastereventos.factory;

import org.mastereventos.model.Evento;

public abstract class EventoFactory {

    public abstract Evento crearEvento(
            String id,
            String nombre,
            String categoria,
            String descripcion,
            String ciudad,
            String fecha,
            String estado
    );
}