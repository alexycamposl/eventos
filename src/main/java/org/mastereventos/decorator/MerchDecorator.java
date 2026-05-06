package org.mastereventos.decorator;

public class MerchDecorator extends EntradaDecorator {

    public MerchDecorator(EntradaComponent entradaDecorada) {
        super(entradaDecorada);
    }

    @Override
    public double getCosto() {
        return super.getCosto() + 50000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Merchandising";
    }
}