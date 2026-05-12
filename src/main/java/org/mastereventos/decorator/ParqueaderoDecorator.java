package org.mastereventos.decorator;

public class ParqueaderoDecorator extends EntradaDecorator {

    public ParqueaderoDecorator(EntradaComponent entradaDecorada) {
        super(entradaDecorada);
    }

    @Override
    public double getCosto() {
        return super.getCosto() + 30000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Parqueadero";
    }
}
