package org.mastereventos.state;

public class CompraCancelada implements EstadoCompra {

    @Override
    public void manejarEstado() {
        System.out.println("La compra ha sido cancelada.");
    }

    @Override
    public String getNombreEstado() {
        return "Cancelada";
    }
}