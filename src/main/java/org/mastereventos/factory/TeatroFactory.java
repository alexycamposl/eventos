package org.mastereventos.factory;

import org.mastereventos.model.Evento;

public class TeatroFactory extends EventoFactory {

    @Override
    public Evento crearEvento(String id, String nombre, String categoria,
                              String descripcion, String ciudad,
                              String fecha, String estado) {

        return new Evento(id, nombre, "Teatro", descripcion, ciudad, fecha, estado);
    }
}