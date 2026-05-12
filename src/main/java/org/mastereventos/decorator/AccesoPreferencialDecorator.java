package org.mastereventos.decorator;

public class AccesoPreferencialDecorator extends EntradaDecorator {

    public AccesoPreferencialDecorator(EntradaComponent entradaDecorada) {
        super(entradaDecorada);
    }

    @Override
    public double getCosto() {
        return super.getCosto() + 50000;
    }

    @Override
    public String getDescripcion() {
        return super.getDescripcion() + " + Acceso Preferencial";
    }
}
