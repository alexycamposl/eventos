package org.mastereventos.repository;

import org.mastereventos.model.Compra;
import org.mastereventos.model.Usuario;
import org.mastereventos.singleton.DataStore;

import java.util.ArrayList;
import java.util.List;

public class CompraRepository {

    private final List<Compra> compras = DataStore.getInstancia().getCompras();

    public void guardarCompra(Compra compra) {
        compras.add(compra);
    }

    public List<Compra> listarTodas() {
        return new ArrayList<>(compras);
    }

    public List<Compra> listarComprasPorUsuario(Usuario usuario) {
        List<Compra> resultado = new ArrayList<>();
        if (usuario == null) return resultado;
        for (Compra c : compras) {
            if (c.getUsuario() != null
                    && c.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Compra> filtrarComprasPorEstado(Usuario usuario, String estado) {
        List<Compra> resultado = new ArrayList<>();
        for (Compra c : listarComprasPorUsuario(usuario)) {
            boolean coincide = estado == null || estado.isBlank()
                    || "Todas".equalsIgnoreCase(estado)
                    || c.getEstado().equalsIgnoreCase(estado);
            if (coincide) resultado.add(c);
        }
        return resultado;
    }

    public List<Compra> filtrarComprasPorEstadoAdmin(String estado) {
        List<Compra> resultado = new ArrayList<>();
        for (Compra c : compras) {
            boolean coincide = estado == null || estado.isBlank()
                    || "Todas".equalsIgnoreCase(estado)
                    || c.getEstado().equalsIgnoreCase(estado);
            if (coincide) resultado.add(c);
        }
        return resultado;
    }

    public List<Compra> filtrarPorEvento(String nombreEvento) {
        List<Compra> resultado = new ArrayList<>();
        for (Compra c : compras) {
            if (c.getEvento() != null
                    && c.getEvento().getNombre().equalsIgnoreCase(nombreEvento)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public List<Compra> filtrarPorFecha(String fecha) {
        List<Compra> resultado = new ArrayList<>();
        if (fecha == null || fecha.isBlank()) return new ArrayList<>(compras);
        for (Compra c : compras) {
            if (c.getFechaCreacion() != null && c.getFechaCreacion().startsWith(fecha)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public Compra buscarPorId(String idCompra) {
        for (Compra c : compras) {
            if (c.getIdCompra().equals(idCompra)) return c;
        }
        return null;
    }

    public long contarPorEstado(String estado) {
        return compras.stream()
                .filter(c -> c.getEstado().equalsIgnoreCase(estado))
                .count();
    }

    public double sumarIngresosPorServicio(String servicio) {
        double total = 0;
        for (Compra c : compras) {
            if (c.getServiciosAdicionales().contains(servicio)) {
                if ("VIP".equals(servicio)) total += 150000;
                else if ("Seguro".equals(servicio)) total += 20000;
                else if ("Merchandising".equals(servicio)) total += 30000;
                else if ("Parqueadero".equals(servicio)) total += 30000;
                else if ("Acceso Preferencial".equals(servicio)) total += 50000;
            }
        }
        return total;
    }
}
