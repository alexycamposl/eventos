package org.mastereventos.state;

public class CompraPagada implements EstadoCompra {

    @Override
    public void manejarEstado() {
        System.out.println("La compra ha sido pagada.");
    }

    @Override
    public String getNombreEstado() {
        return "Pagada";
    }
}