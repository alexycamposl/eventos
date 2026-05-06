package org.mastereventos.state;

public class CompraConfirmada implements EstadoCompra {

    @Override
    public void manejarEstado() {
        System.out.println("La compra está confirmada.");
    }

    @Override
    public String getNombreEstado() {
        return "Confirmada";
    }
}