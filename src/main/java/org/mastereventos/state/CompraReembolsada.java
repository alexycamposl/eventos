package org.mastereventos.state;

public class CompraReembolsada implements EstadoCompra {

    @Override
    public void manejarEstado() {

        System.out.println(
                "La compra fue reembolsada."
        );
    }

    @Override
    public String getNombreEstado() {

        return "Reembolsada";
    }
}
//actualizacion alex 4