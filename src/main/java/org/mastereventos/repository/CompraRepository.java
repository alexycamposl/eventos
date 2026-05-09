package org.mastereventos.repository;

import org.mastereventos.model.Compra;

import java.util.ArrayList;
import java.util.List;

public class CompraRepository {

    private static CompraRepository instancia;

    private static final List<Compra> compras =
            new ArrayList<>();

    // CONSTRUCTOR PRIVADO

    private CompraRepository() {
    }

    // SINGLETON

    public static CompraRepository getInstancia() {

        if (instancia == null) {

            instancia =
                    new CompraRepository();
        }

        return instancia;
    }

    // GUARDAR

    public void guardarCompra(Compra compra) {

        compras.add(compra);
    }

    // LISTAR TODAS

    public List<Compra> listarCompras() {

        return compras;
    }

    // LISTAR POR USUARIO

    public List<Compra> listarComprasUsuario(
            String idUsuario) {

        List<Compra> resultado =
                new ArrayList<>();

        for (Compra compra : compras) {

            if (
                    compra.getUsuario()
                            .getIdUsuario()
                            .equals(idUsuario)
            ) {

                resultado.add(compra);
            }
        }

        return resultado;
    }
}