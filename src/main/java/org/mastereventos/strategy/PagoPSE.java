package org.mastereventos.strategy;

public class PagoPSE implements PagoStrategy {

    @Override
    public boolean procesarPago(double monto) {
        System.out.println("Pago por PSE procesado por: $" + monto);
        return true;
    }

    @Override
    public String getMetodoPago() {
        return "PSE";
    }
}