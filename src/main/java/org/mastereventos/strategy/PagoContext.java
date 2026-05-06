package org.mastereventos.strategy;

public class PagoContext {

    private PagoStrategy estrategia;

    public void setEstrategia(PagoStrategy estrategia) {
        this.estrategia = estrategia;
    }

    public boolean ejecutarPago(double monto) {
        if (estrategia == null) {
            throw new IllegalStateException("No se ha seleccionado método de pago");
        }
        return estrategia.procesarPago(monto);
    }
}