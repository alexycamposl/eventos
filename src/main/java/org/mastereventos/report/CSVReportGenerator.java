package org.mastereventos.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.mastereventos.model.Compra;

public class CSVReportGenerator {

    public File generarReporteComprasUsuario(List<Compra> compras, String nombreArchivo) throws IOException {
        File carpetaReportes = new File("reportes");

        if (!carpetaReportes.exists()) {
            carpetaReportes.mkdirs();
        }

        File archivo = new File(carpetaReportes, nombreArchivo);

        return generarReporteComprasUsuario(compras, archivo);
    }

    public File generarReporteComprasUsuario(List<Compra> compras, File archivo) throws IOException {
        try (FileWriter writer = new FileWriter(archivo)) {
            writer.write("ID Compra;Usuario;Evento;Total;Estado\n");

            for (Compra compra : compras) {
                String usuario = compra.getUsuario() != null
                        ? compra.getUsuario().getNombre()
                        : "Sin usuario";

                String evento = compra.getEvento() != null
                        ? compra.getEvento().getNombre()
                        : "Sin evento";

                writer.write(compra.getIdCompra() + ";"
                        + limpiarCampo(usuario) + ";"
                        + limpiarCampo(evento) + ";"
                        + compra.getTotal() + ";"
                        + compra.getEstado() + "\n");
            }
        }

        return archivo;
    }

    private String limpiarCampo(String valor) {
        if (valor == null) {
            return "";
        }

        return valor.replace(";", ",").replace("\n", " ");
    }
}
