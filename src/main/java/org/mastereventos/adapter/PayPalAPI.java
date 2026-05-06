package org.mastereventos.adapter;

public class PayPalAPI {

    public boolean sendPayment(double amount) {
        System.out.println("Procesando pago externo con PayPal por $" + amount);
        return true;
    }
}