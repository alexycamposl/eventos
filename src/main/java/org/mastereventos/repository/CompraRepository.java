package org.mastereventos.repository;

import java.util.ArrayList;
import java.util.List;

import org.mastereventos.model.Compra;
import org.mastereventos.model.Usuario;

public class CompraRepository {

    private final List<Compra> compras;

    public CompraRepository() {
        compras = new ArrayList<>();
    }

    public void guardarCompra(Compra compra) {
        compras.add(compra);
    }

    public List<Compra> listarComprasPorUsuario(Usuario usuario) {
        List<Compra> resultado = new ArrayList<>();

        if (usuario == null) {
            return resultado;
        }

        for (Compra compra : compras) {
            if (compra.getUsuario() != null
                    && compra.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                resultado.add(compra);
            }
        }

        return resultado;
    }

    public List<Compra> filtrarComprasPorEstado(Usuario usuario, String estado) {
        List<Compra> resultado = new ArrayList<>();

        for (Compra compra : listarComprasPorUsuario(usuario)) {
            boolean coincideEstado = estado == null
                    || estado.isBlank()
                    || "Todas".equalsIgnoreCase(estado)
                    || compra.getEstado().equalsIgnoreCase(estado);

            if (coincideEstado) {
                resultado.add(compra);
            }
        }

        return resultado;
    }
}
