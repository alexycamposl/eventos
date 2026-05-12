package org.mastereventos.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mastereventos.model.Compra;
import org.mastereventos.model.Zona;

public class CSVReportGenerator {

    public File generarReporteComprasUsuario(List<Compra> compras, String nombreArchivo)
            throws IOException {
        File carpeta = new File("reportes");
        if (!carpeta.exists()) carpeta.mkdirs();
        return generarReporteComprasUsuario(compras, new File(carpeta, nombreArchivo));
    }

    public File generarReporteComprasUsuario(List<Compra> compras, File archivo)
            throws IOException {
        try (FileWriter w = new FileWriter(archivo)) {
            w.write("ID Compra;Usuario;Evento;Total;Estado;Fecha;Servicios\n");
            for (Compra c : compras) {
                String usuario = c.getUsuario() != null ? c.getUsuario().getNombre() : "";
                String evento = c.getEvento() != null ? c.getEvento().getNombre() : "";
                String servicios = String.join(" + ", c.getServiciosAdicionales());
                w.write(limpiar(c.getIdCompra()) + ";"
                        + limpiar(usuario) + ";"
                        + limpiar(evento) + ";"
                        + c.getTotal() + ";"
                        + limpiar(c.getEstado()) + ";"
                        + limpiar(c.getFechaCreacion()) + ";"
                        + limpiar(servicios) + "\n");
            }
        }
        return archivo;
    }

    public File generarReporteVentasPorPeriodo(List<Compra> compras, File archivo)
            throws IOException {
        Map<String, Integer> ventasPorFecha = new HashMap<>();
        for (Compra c : compras) {
            if (!"Cancelada".equalsIgnoreCase(c.getEstado())
                    && !"Reembolsada".equalsIgnoreCase(c.getEstado())) {
                String fecha = c.getFechaCreacion() != null ? c.getFechaCreacion() : "Sin fecha";
                ventasPorFecha.merge(fecha, 1, Integer::sum);
            }
        }
        try (FileWriter w = new FileWriter(archivo)) {
            w.write("Fecha;Numero de Ventas\n");
            for (Map.Entry<String, Integer> entry : ventasPorFecha.entrySet()) {
                w.write(limpiar(entry.getKey()) + ";" + entry.getValue() + "\n");
            }
        }
        return archivo;
    }

    public File generarReporteOcupacionPorZona(Map<String, List<Zona>> zonasPorEvento,
                                                File archivo) throws IOException {
        try (FileWriter w = new FileWriter(archivo)) {
            w.write("Evento;Zona;Capacidad;Vendidos;Disponibles;Bloqueados\n");
            for (Map.Entry<String, List<Zona>> entry : zonasPorEvento.entrySet()) {
                String idEvento = entry.getKey();
                for (Zona z : entry.getValue()) {
                    long vendidos = z.getAsientos().stream()
                            .filter(a -> "Vendido".equalsIgnoreCase(a.getEstado())).count();
                    long disponibles = z.getAsientos().stream()
                            .filter(a -> "Disponible".equalsIgnoreCase(a.getEstado())).count();
                    long bloqueados = z.getAsientos().stream()
                            .filter(a -> "Bloqueado".equalsIgnoreCase(a.getEstado())).count();
                    w.write(idEvento + ";" + limpiar(z.getNombre()) + ";"
                            + z.getCapacidad() + ";" + vendidos + ";"
                            + disponibles + ";" + bloqueados + "\n");
                }
            }
        }
        return archivo;
    }

    public File generarReporteIngresosServicios(List<Compra> compras, File archivo)
            throws IOException {
        List<String> servicios = Arrays.asList("VIP", "Seguro", "Merchandising",
                "Parqueadero", "Acceso Preferencial");
        Map<String, Integer> conteo = new HashMap<>();
        for (String s : servicios) conteo.put(s, 0);
        for (Compra c : compras) {
            for (String s : c.getServiciosAdicionales()) {
                conteo.merge(s, 1, Integer::sum);
            }
        }
        try (FileWriter w = new FileWriter(archivo)) {
            w.write("Servicio;Cantidad;Ingreso Estimado\n");
            Map<String, Double> precios = Map.of("VIP", 150000.0, "Seguro", 20000.0,
                    "Merchandising", 30000.0, "Parqueadero", 30000.0,
                    "Acceso Preferencial", 50000.0);
            for (String s : servicios) {
                int cantidad = conteo.getOrDefault(s, 0);
                double precio = precios.getOrDefault(s, 0.0);
                w.write(limpiar(s) + ";" + cantidad + ";" + (cantidad * precio) + "\n");
            }
        }
        return archivo;
    }

    public File generarReporteTasaCancelacion(List<Compra> compras, File archivo)
            throws IOException {
        long total = compras.size();
        long canceladas = compras.stream()
                .filter(c -> "Cancelada".equalsIgnoreCase(c.getEstado())
                        || "Reembolsada".equalsIgnoreCase(c.getEstado())).count();
        double tasa = total > 0 ? (canceladas * 100.0 / total) : 0;
        try (FileWriter w = new FileWriter(archivo)) {
            w.write("Total Compras;Canceladas/Reembolsadas;Tasa Cancelacion (%)\n");
            w.write(total + ";" + canceladas + ";" + String.format("%.2f", tasa) + "\n");
        }
        return archivo;
    }

    public File generarReporteTopEventos(List<Compra> compras, File archivo)
            throws IOException {
        Map<String, Integer> conteoPorEvento = new HashMap<>();
        for (Compra c : compras) {
            if (c.getEvento() != null) {
                conteoPorEvento.merge(c.getEvento().getNombre(), 1, Integer::sum);
            }
        }
        try (FileWriter w = new FileWriter(archivo)) {
            w.write("Evento;Numero de Compras\n");
            conteoPorEvento.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> {
                        try {
                            w.write(limpiar(entry.getKey()) + ";" + entry.getValue() + "\n");
                        } catch (IOException ignored) {}
                    });
        }
        return archivo;
    }

    private String limpiar(String valor) {
        if (valor == null) return "";
        return valor.replace(";", ",").replace("\n", " ");
    }
}
