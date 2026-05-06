package org.mastereventos.decorator;

public abstract class EntradaDecorator implements EntradaComponent {

    protected EntradaComponent entradaDecorada;

    public EntradaDecorator(EntradaComponent entradaDecorada) {
        this.entradaDecorada = entradaDecorada;
    }

    @Override
    public double getCosto() {
        return entradaDecorada.getCosto();
    }

    @Override
    public String getDescripcion() {
        return entradaDecorada.getDescripcion();
    }
}