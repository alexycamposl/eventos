package org.mastereventos.strategy;

public class PagoPayPal implements PagoStrategy {

    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Pago por PayPal procesado por: $" + monto);
        return true;
    }

    @Override
    public String getMetodoPago() {
        return "PayPal";
    }
}