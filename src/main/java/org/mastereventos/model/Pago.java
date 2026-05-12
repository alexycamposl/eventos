package org.mastereventos.model;

public class Pago {

    private String idPago;
    private Compra compra;
    private double monto;
    private String metodo;
    private boolean confirmado;

    public Pago(String idPago, Compra compra, double monto, String metodo) {
        this.idPago = idPago;
        this.compra = compra;
        this.monto = monto;
        this.metodo = metodo;
        this.confirmado = false;
    }

    public void confirmarPago() {
        this.confirmado = true;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public double getMonto() {
        return monto;
    }
}