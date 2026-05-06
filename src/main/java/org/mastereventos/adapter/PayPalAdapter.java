package org.mastereventos.adapter;

public class PayPalAdapter implements PagoExterno {

    private PayPalAPI paypalAPI;

    public PayPalAdapter(PayPalAPI paypalAPI) {
        this.paypalAPI = paypalAPI;
    }

    @Override
    public boolean pagar(double monto) {
        return paypalAPI.sendPayment(monto);
    }
}