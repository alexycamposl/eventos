package org.mastereventos.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.mastereventos.builder.CompraBuilder;
import org.mastereventos.decorator.EntradaBase;
import org.mastereventos.decorator.EntradaComponent;
import org.mastereventos.decorator.MerchDecorator;
import org.mastereventos.decorator.SeguroDecorator;
import org.mastereventos.decorator.VIPDecorator;
import org.mastereventos.model.Asiento;
import org.mastereventos.model.Compra;
import org.mastereventos.model.Entrada;
import org.mastereventos.model.Evento;
import org.mastereventos.model.Usuario;
import org.mastereventos.model.Zona;
import org.mastereventos.report.CSVReportGenerator;
import org.mastereventos.report.PDFReportGenerator;
import org.mastereventos.repository.CompraRepository;
import org.mastereventos.repository.EventoRepository;
import org.mastereventos.repository.ZonaRepository;
import org.mastereventos.strategy.PagoContext;
import org.mastereventos.strategy.PagoPSE;
import org.mastereventos.strategy.PagoPayPal;
import org.mastereventos.strategy.PagoStrategy;
import org.mastereventos.strategy.PagoTarjeta;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class UserDashboardController {

    @FXML
    private ListView<Evento> eventosListView;

    @FXML
    private TextArea detalleEventoArea;

    @FXML
    private ComboBox<String> ciudadComboBox;

    @FXML
    private ComboBox<String> categoriaComboBox;

    @FXML
    private ComboBox<Zona> zonaComboBox;

    @FXML
    private ComboBox<Asiento> asientoComboBox;

    @FXML
    private Label precioZonaLabel;

    @FXML
    private CheckBox vipCheckBox;

    @FXML
    private CheckBox seguroCheckBox;

    @FXML
    private CheckBox merchCheckBox;

    @FXML
    private TextArea resumenCompraArea;

    @FXML
    private ComboBox<String> metodoPagoComboBox;

    @FXML
    private ComboBox<String> estadoHistorialComboBox;

    @FXML
    private ListView<String> historialComprasListView;

    private Usuario usuarioActual;
    private Compra compraActual;
    private Asiento asientoCompraActual;

    private final CompraRepository compraRepository = new CompraRepository();
    private final EventoRepository eventoRepository = new EventoRepository();
    private final ZonaRepository zonaRepository = new ZonaRepository();
    private final CSVReportGenerator csvReportGenerator = new CSVReportGenerator();
    private final PDFReportGenerator pdfReportGenerator = new PDFReportGenerator();

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    @FXML
    private void initialize() {
        cargarFiltros();
        cargarEventos();
        cargarMetodosPago();
        cargarEstadosHistorial();

        eventosListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    mostrarDetalleEvento(seleccionado);
                    cargarZonasEvento(seleccionado);
                });

        zonaComboBox.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> cargarAsientosZona(seleccionado));

        vipCheckBox.selectedProperty()
                .addListener((observable, anterior, seleccionado) -> actualizarTotalEstimado());

        seguroCheckBox.selectedProperty()
                .addListener((observable, anterior, seleccionado) -> actualizarTotalEstimado());

        merchCheckBox.selectedProperty()
                .addListener((observable, anterior, seleccionado) -> actualizarTotalEstimado());
    }

    private void cargarFiltros() {
        ciudadComboBox.getItems().addAll("Todas", "Bogotá", "Medellín", "Cali");
        categoriaComboBox.getItems().addAll("Todas", "Concierto", "Teatro", "Conferencia");

        ciudadComboBox.setValue("Todas");
        categoriaComboBox.setValue("Todas");
    }

    private void cargarMetodosPago() {
        metodoPagoComboBox.getItems().addAll("Tarjeta", "PSE", "PayPal");
        metodoPagoComboBox.setValue("Tarjeta");
    }

    private void cargarEstadosHistorial() {
        estadoHistorialComboBox.getItems().addAll("Todas", "Creada", "Pagada", "Cancelada");
        estadoHistorialComboBox.setValue("Todas");
    }

    private void cargarEventos() {
        eventosListView.getItems().clear();
        eventosListView.getItems().addAll(eventoRepository.listarEventosPublicados());
    }

    @FXML
    private void handleFiltrar() {
        eventosListView.getItems().clear();
        detalleEventoArea.clear();
        zonaComboBox.getItems().clear();
        asientoComboBox.getItems().clear();
        precioZonaLabel.setText("Precio: $0");
        limpiarServicios();
        resumenCompraArea.clear();

        String ciudad = ciudadComboBox.getValue();
        String categoria = categoriaComboBox.getValue();

        eventosListView.getItems().addAll(eventoRepository.filtrarEventos(ciudad, categoria));
    }

    @FXML
    private void handleLimpiarFiltros() {
        ciudadComboBox.setValue("Todas");
        categoriaComboBox.setValue("Todas");
        detalleEventoArea.clear();
        zonaComboBox.getItems().clear();
        asientoComboBox.getItems().clear();
        precioZonaLabel.setText("Precio: $0");
        limpiarServicios();
        resumenCompraArea.clear();
        cargarEventos();
    }

    @FXML
    private void handleCrearCompra() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        Zona zona = zonaComboBox.getValue();
        Asiento asiento = asientoComboBox.getValue();

        if (evento == null || zona == null || asiento == null) {
            showAlert("Datos incompletos", "Selecciona un evento, una zona y un asiento.");
            return;
        }

        if (usuarioActual == null) {
            showAlert("Error", "No se encontró el usuario actual.");
            return;
        }

        EntradaComponent entradaDecorada = crearEntradaDecorada(evento, zona);

        Entrada entrada = new Entrada(
                "ENT-" + System.currentTimeMillis(),
                zona,
                asiento,
                entradaDecorada.getCosto(),
                "Activa"
        );

        Compra compra = new CompraBuilder()
                .setIdCompra("COM-" + System.currentTimeMillis())
                .setUsuario(usuarioActual)
                .setEvento(evento)
                .agregarEntrada(entrada)
                .build();

        compraActual = compra;
        asientoCompraActual = asiento;
        compraRepository.guardarCompra(compra);

        asiento.setEstado("Reservado");
        cargarAsientosZona(zona);
        actualizarHistorialCompras(compraRepository.listarComprasPorUsuario(usuarioActual));

        String resumen = "Compra creada correctamente\n"
                + "ID compra: " + compra.getIdCompra() + "\n"
                + "Usuario: " + usuarioActual.getNombre() + "\n"
                + "Evento: " + evento.getNombre() + "\n"
                + "Zona: " + zona.getNombre() + "\n"
                + "Asiento: " + asiento.toString() + "\n"
                + "Servicios: " + entradaDecorada.getDescripcion() + "\n"
                + "Total: $" + compra.getTotal() + "\n"
                + "Estado: " + compra.getEstado();

        resumenCompraArea.setText(resumen);
    }

    @FXML
    private void handleCancelarCompra() {
        if (compraActual == null) {
            showAlert("Sin compra", "Primero debes crear una compra.");
            return;
        }

        if (!"Creada".equalsIgnoreCase(compraActual.getEstado())) {
            showAlert("No se puede cancelar", "Solo puedes cancelar compras en estado Creada.");
            return;
        }

        compraActual.setEstado("Cancelada");

        if (asientoCompraActual != null) {
            asientoCompraActual.setEstado("Disponible");
        }

        Zona zona = zonaComboBox.getValue();
        if (zona != null) {
            cargarAsientosZona(zona);
        }

        String resumen = resumenCompraArea.getText()
                + "\n\nCompra cancelada correctamente"
                + "\nEstado actualizado: " + compraActual.getEstado()
                + "\nEl asiento fue liberado.";

        resumenCompraArea.setText(resumen);
        actualizarHistorialCompras(compraRepository.listarComprasPorUsuario(usuarioActual));
        showAlert("Compra cancelada", "La compra fue cancelada correctamente.");
    }

    @FXML
    private void handlePagarCompra() {
        if (compraActual == null) {
            showAlert("Sin compra", "Primero debes crear una compra.");
            return;
        }

        if (!"Creada".equalsIgnoreCase(compraActual.getEstado())) {
            showAlert("Compra no disponible", "La compra ya fue procesada o no esta en estado Creada.");
            return;
        }

        String metodo = metodoPagoComboBox.getValue();

        if (metodo == null || metodo.isBlank()) {
            showAlert("Metodo de pago", "Selecciona un metodo de pago.");
            return;
        }

        PagoStrategy estrategia = crearEstrategiaPago(metodo);

        PagoContext pagoContext = new PagoContext();
        pagoContext.setEstrategia(estrategia);

        boolean pagoExitoso = pagoContext.ejecutarPago(compraActual.getTotal());

        if (pagoExitoso) {
            compraActual.setEstado("Pagada");

            String comprobante = resumenCompraArea.getText()
                    + "\n\nPago realizado correctamente"
                    + "\nMetodo de pago: " + estrategia.getMetodoPago()
                    + "\nEstado actualizado: " + compraActual.getEstado()
                    + "\nComprobante: PAG-" + System.currentTimeMillis();

            resumenCompraArea.setText(comprobante);
            actualizarHistorialCompras(compraRepository.listarComprasPorUsuario(usuarioActual));
            showAlert("Pago exitoso", "La compra fue pagada correctamente.");
        } else {
            showAlert("Pago rechazado", "No se pudo procesar el pago.");
        }
    }

    @FXML
    private void handleFiltrarHistorial() {
        String estado = estadoHistorialComboBox.getValue();
        actualizarHistorialCompras(compraRepository.filtrarComprasPorEstado(usuarioActual, estado));
    }

    @FXML
    private void handleActualizarHistorial() {
        estadoHistorialComboBox.setValue("Todas");
        actualizarHistorialCompras(compraRepository.listarComprasPorUsuario(usuarioActual));
    }

    @FXML
    private void handleExportarCSV() {
        List<Compra> compras = compraRepository.listarComprasPorUsuario(usuarioActual);

        if (compras.isEmpty()) {
            showAlert("Sin compras", "No hay compras para exportar.");
            return;
        }

        File archivoDestino = seleccionarArchivoReporte(
                "Guardar reporte CSV",
                "compras_usuario_" + usuarioActual.getIdUsuario() + ".csv",
                "Archivos CSV",
                "*.csv"
        );

        if (archivoDestino == null) {
            return;
        }

        try {
            File archivo = csvReportGenerator.generarReporteComprasUsuario(compras, archivoDestino);
            showAlert("Reporte generado", "El reporte CSV fue generado en: " + archivo.getPath());
        } catch (IOException e) {
            showAlert("Error", "No se pudo generar el reporte CSV.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportarPDF() {
        List<Compra> compras = compraRepository.listarComprasPorUsuario(usuarioActual);

        if (compras.isEmpty()) {
            showAlert("Sin compras", "No hay compras para exportar.");
            return;
        }

        File archivoDestino = seleccionarArchivoReporte(
                "Guardar reporte PDF",
                "compras_usuario_" + usuarioActual.getIdUsuario() + ".pdf",
                "Archivos PDF",
                "*.pdf"
        );

        if (archivoDestino == null) {
            return;
        }

        try {
            File archivo = pdfReportGenerator.generarReporteComprasUsuario(compras, archivoDestino);
            showAlert("Reporte generado", "El reporte PDF fue generado en: " + archivo.getPath());
        } catch (IOException e) {
            showAlert("Error", "No se pudo generar el reporte PDF.");
            e.printStackTrace();
        }
    }

    private File seleccionarArchivoReporte(String titulo,
                                           String nombreSugerido,
                                           String descripcionExtension,
                                           String patronExtension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(titulo);
        fileChooser.setInitialFileName(nombreSugerido);
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(descripcionExtension, patronExtension)
        );

        return fileChooser.showSaveDialog(resumenCompraArea.getScene().getWindow());
    }

    private PagoStrategy crearEstrategiaPago(String metodo) {
        if ("PSE".equalsIgnoreCase(metodo)) {
            return new PagoPSE();
        }

        if ("PayPal".equalsIgnoreCase(metodo)) {
            return new PagoPayPal();
        }

        return new PagoTarjeta();
    }

    private void actualizarTotalEstimado() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        Zona zona = zonaComboBox.getValue();

        if (zona == null) {
            precioZonaLabel.setText("Precio: $0");
            return;
        }

        EntradaComponent entradaDecorada = crearEntradaDecorada(evento, zona);
        precioZonaLabel.setText("Total estimado: $" + entradaDecorada.getCosto());
    }

    private EntradaComponent crearEntradaDecorada(Evento evento, Zona zona) {
        String nombreEvento = evento != null ? evento.getNombre() : "Evento";

        EntradaComponent entradaDecorada = new EntradaBase(
                zona.getPrecioBase(),
                "Entrada " + nombreEvento + " - " + zona.getNombre()
        );

        if (vipCheckBox.isSelected()) {
            entradaDecorada = new VIPDecorator(entradaDecorada);
        }

        if (seguroCheckBox.isSelected()) {
            entradaDecorada = new SeguroDecorator(entradaDecorada);
        }

        if (merchCheckBox.isSelected()) {
            entradaDecorada = new MerchDecorator(entradaDecorada);
        }

        return entradaDecorada;
    }

    private void actualizarHistorialCompras(java.util.List<Compra> compras) {
        historialComprasListView.getItems().clear();

        for (Compra compra : compras) {
            historialComprasListView.getItems().add(formatearCompraHistorial(compra));
        }
    }

    private String formatearCompraHistorial(Compra compra) {
        String nombreEvento = compra.getEvento() != null ? compra.getEvento().getNombre() : "Sin evento";

        return compra.getIdCompra()
                + " | " + nombreEvento
                + " | Total: $" + compra.getTotal()
                + " | Estado: " + compra.getEstado();
    }

    private void mostrarDetalleEvento(Evento evento) {
        if (evento == null) {
            detalleEventoArea.clear();
            return;
        }

        String detalle = "Nombre: " + evento.getNombre() + "\n"
                + "Categoría: " + evento.getCategoria() + "\n"
                + "Descripción: " + evento.getDescripcion() + "\n"
                + "Ciudad: " + evento.getCiudad() + "\n"
                + "Fecha: " + evento.getFecha() + "\n"
                + "Estado: " + evento.getEstado();

        detalleEventoArea.setText(detalle);
    }

    private void cargarZonasEvento(Evento evento) {
        zonaComboBox.getItems().clear();
        asientoComboBox.getItems().clear();
        precioZonaLabel.setText("Precio: $0");
        limpiarServicios();
        resumenCompraArea.clear();

        if (evento == null) {
            return;
        }

        zonaComboBox.getItems().addAll(zonaRepository.listarZonasPorEvento(evento.getIdEvento()));
    }

    private void cargarAsientosZona(Zona zona) {
        asientoComboBox.getItems().clear();

        if (zona == null) {
            precioZonaLabel.setText("Precio: $0");
            return;
        }

        asientoComboBox.getItems().addAll(zonaRepository.listarAsientosDisponibles(zona));
        actualizarTotalEstimado();
    }

    private void limpiarServicios() {
        vipCheckBox.setSelected(false);
        seguroCheckBox.setSelected(false);
        merchCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
