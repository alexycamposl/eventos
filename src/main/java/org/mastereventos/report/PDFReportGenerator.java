package org.mastereventos.report;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.mastereventos.model.Compra;

public class PDFReportGenerator {

    public File generarReporteComprasUsuario(List<Compra> compras, String nombreArchivo) throws IOException {
        File carpetaReportes = new File("reportes");

        if (!carpetaReportes.exists()) {
            carpetaReportes.mkdirs();
        }

        File archivo = new File(carpetaReportes, nombreArchivo);

        return generarReporteComprasUsuario(compras, archivo);
    }

    public File generarReporteComprasUsuario(List<Compra> compras, File archivo) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                content.newLineAtOffset(50, 750);
                content.showText("Reporte de compras del usuario");
                content.endText();

                float y = 710;

                for (Compra compra : compras) {
                    if (y < 80) {
                        content.close();

                        page = new PDPage();
                        document.addPage(page);

                        y = 750;
                    }

                    String usuario = compra.getUsuario() != null
                            ? compra.getUsuario().getNombre()
                            : "Sin usuario";

                    String evento = compra.getEvento() != null
                            ? compra.getEvento().getNombre()
                            : "Sin evento";

                    escribirLinea(content, "ID Compra: " + compra.getIdCompra(), 50, y);
                    y -= 18;

                    escribirLinea(content, "Usuario: " + usuario, 50, y);
                    y -= 18;

                    escribirLinea(content, "Evento: " + evento, 50, y);
                    y -= 18;

                    escribirLinea(content, "Total: $" + compra.getTotal(), 50, y);
                    y -= 18;

                    escribirLinea(content, "Estado: " + compra.getEstado(), 50, y);
                    y -= 28;
                }
            }

            document.save(archivo);
        }

        return archivo;
    }

    private void escribirLinea(PDPageContentStream content, String texto, float x, float y) throws IOException {
        content.beginText();
        content.setFont(PDType1Font.HELVETICA, 11);
        content.newLineAtOffset(x, y);
        content.showText(limpiarTexto(texto));
        content.endText();
    }

    private String limpiarTexto(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("Á", "A")
                .replace("É", "E")
                .replace("Í", "I")
                .replace("Ó", "O")
                .replace("Ú", "U")
                .replace("ñ", "n")
                .replace("Ñ", "N");
    }
}
