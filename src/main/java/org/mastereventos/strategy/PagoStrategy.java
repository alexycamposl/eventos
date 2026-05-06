package org.mastereventos.strategy;

public interface PagoStrategy {
    boolean procesarPago(double monto);
    String getMetodoPago();
}