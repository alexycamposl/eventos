package org.mastereventos.facade;

import org.mastereventos.decorator.EntradaComponent;
import org.mastereventos.strategy.PagoContext;
import org.mastereventos.strategy.PagoStrategy;

public class CompraFacade {

    public boolean realizarCompra(String usuario,
                                  EntradaComponent entrada,
                                  PagoStrategy metodoPago) {

        System.out.println("Validando usuario: " + usuario);

        System.out.println("Entrada seleccionada: " + entrada.getDescripcion());

        System.out.println("Total a pagar: $" + entrada.getCosto());

        PagoContext pagoContext = new PagoContext();
        pagoContext.setEstrategia(metodoPago);

        boolean pagoExitoso = pagoContext.ejecutarPago(entrada.getCosto());

        if (pagoExitoso) {
            System.out.println("Compra realizada exitosamente.");
            return true;
        }

        System.out.println("Error en el pago.");
        return false;
    }
}