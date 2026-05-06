package org.mastereventos.state;

public class CompraCreada implements EstadoCompra {

    @Override
    public void manejarEstado() {
        System.out.println("La compra ha sido creada.");
    }

    @Override
    public String getNombreEstado() {
        return "Creada";
    }
}