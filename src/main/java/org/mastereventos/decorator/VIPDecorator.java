package org.mastereventos.decorator;

public class VIPDecorator extends EntradaDecorator {

    public VIPDecorator(EntradaComponent entradaDecorada) {
        super(entradaDecorada);
    }

    @Override
    public double getCosto() {
        return super.getCosto() + 150000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Acceso VIP";
    }
}