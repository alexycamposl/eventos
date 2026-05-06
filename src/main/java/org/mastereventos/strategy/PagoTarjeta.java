package org.mastereventos.strategy;

public class PagoTarjeta implements PagoStrategy {

    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Pago con tarjeta procesado por: $" + monto);
        return true;
    }

    @Override
    public String getMetodoPago() {
        return "Tarjeta";
    }
}