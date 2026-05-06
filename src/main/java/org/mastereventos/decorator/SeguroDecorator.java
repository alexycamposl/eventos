package org.mastereventos.decorator;

public class SeguroDecorator extends EntradaDecorator {

    public SeguroDecorator(EntradaComponent entradaDecorada) {
        super(entradaDecorada);
    }

    @Override
    public double getCosto() {
        return super.getCosto() + 20000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Seguro";
    }
}