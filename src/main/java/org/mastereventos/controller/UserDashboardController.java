package org.mastereventos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.mastereventos.decorator.*;
import org.mastereventos.builder.CompraBuilder;
import org.mastereventos.model.*;
import org.mastereventos.report.CSVReportGenerator;
import org.mastereventos.report.PDFReportGenerator;
import org.mastereventos.repository.*;
import org.mastereventos.strategy.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDashboardController {

    // Perfil
    @FXML private Label bienvenidaLabel;
    @FXML private TextField editNombreField;
    @FXML private TextField editCorreoField;
    @FXML private TextField editTelefonoField;

    // Filtros eventos
    @FXML private ComboBox<String> ciudadComboBox;
    @FXML private ComboBox<String> categoriaComboBox;
    @FXML private ListView<Evento> eventosListView;
    @FXML private TextArea detalleEventoArea;

    // Selección entrada
    @FXML private ComboBox<Zona> zonaComboBox;
    @FXML private ComboBox<Asiento> asientoComboBox;
    @FXML private Label precioZonaLabel;

    // Servicios
    @FXML private CheckBox vipCheckBox;
    @FXML private CheckBox seguroCheckBox;
    @FXML private CheckBox merchCheckBox;
    @FXML private CheckBox parqueaderoCheckBox;
    @FXML private CheckBox accesoPreferencialCheckBox;

    // Resumen y pago
    @FXML private TextArea resumenCompraArea;
    @FXML private ComboBox<String> metodoPagoComboBox;

    // Historial
    @FXML private ComboBox<String> estadoHistorialComboBox;
    @FXML private ComboBox<String> eventoHistorialComboBox;
    @FXML private TextField fechaHistorialField;
    @FXML private ListView<Compra> historialComprasListView;

    private Usuario usuarioActual;
    private Compra compraActual;
    private Asiento asientoCompraActual;

    private final CompraRepository compraRepository = new CompraRepository();
    private final EventoRepository eventoRepository = new EventoRepository();
    private final ZonaRepository zonaRepository = new ZonaRepository();
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final CSVReportGenerator csvReport = new CSVReportGenerator();
    private final PDFReportGenerator pdfReport = new PDFReportGenerator();


    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioActual = usuario;

        bienvenidaLabel.setText("Bienvenido, " + usuario.getNombre());
        editNombreField.setText(usuario.getNombre());
        editCorreoField.setText(usuario.getCorreo());
        editTelefonoField.setText(usuario.getTelefono());

        editCorreoField.setEditable(false);
    }

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        bienvenidaLabel.setText("Bienvenido, " + usuario.getNombre());
        editNombreField.setText(usuario.getNombre());
        editCorreoField.setText(usuario.getCorreo());
        editTelefonoField.setText(usuario.getTelefono());
        actualizarHistorial(compraRepository.listarComprasPorUsuario(usuario));
        cargarEventosHistorial();
    }

    @FXML
    private void initialize() {
        cargarFiltros();
        cargarEventos();
        cargarMetodosPago();
        cargarEstadosHistorial();
        configurarCeldasHistorial();

        eventosListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> {
                    mostrarDetalleEvento(sel);
                    cargarZonasEvento(sel);
                });

        zonaComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarAsientosZona(sel));

        vipCheckBox.selectedProperty().addListener((o, a, s) -> actualizarTotal());
        seguroCheckBox.selectedProperty().addListener((o, a, s) -> actualizarTotal());
        merchCheckBox.selectedProperty().addListener((o, a, s) -> actualizarTotal());
        parqueaderoCheckBox.selectedProperty().addListener((o, a, s) -> actualizarTotal());
        accesoPreferencialCheckBox.selectedProperty().addListener((o, a, s) -> actualizarTotal());
    }

    private void configurarCeldasHistorial() {
        historialComprasListView.setCellFactory(lv -> new ListCell<Compra>() {
            @Override
            protected void updateItem(Compra c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) {
                    setText(null);
                } else {
                    String ev = c.getEvento() != null ? c.getEvento().getNombre() : "Sin evento";
                    String serv = c.getServiciosAdicionales().isEmpty()
                            ? "Sin servicios"
                            : String.join(", ", c.getServiciosAdicionales());
                    setText(c.getIdCompra() + " | " + ev
                            + " | $" + c.getTotal()
                            + " | " + c.getEstado()
                            + " | " + c.getFechaCreacion()
                            + " | " + serv);
                }
            }
        });
    }

    private void cargarFiltros() {
        ciudadComboBox.getItems().addAll("Todas", "Bogota", "Medellin", "Cali");
        categoriaComboBox.getItems().addAll("Todas", "Concierto", "Teatro", "Conferencia");
        ciudadComboBox.setValue("Todas");
        categoriaComboBox.setValue("Todas");
    }

    private void cargarMetodosPago() {
        metodoPagoComboBox.getItems().addAll("Tarjeta", "PSE", "PayPal");
        metodoPagoComboBox.setValue("Tarjeta");
    }

    private void cargarEstadosHistorial() {
        estadoHistorialComboBox.getItems().addAll("Todas", "Creada", "Pagada",
                "Confirmada", "Cancelada", "Reembolsada", "Incidencia");
        estadoHistorialComboBox.setValue("Todas");
    }

    private void cargarEventosHistorial() {
        eventoHistorialComboBox.getItems().clear();
        eventoHistorialComboBox.getItems().add("Todos");
        for (Evento e : eventoRepository.getEventos()) {
            eventoHistorialComboBox.getItems().add(e.getNombre());
        }
        eventoHistorialComboBox.setValue("Todos");
    }

    private void cargarEventos() {
        eventosListView.getItems().setAll(eventoRepository.listarEventosPublicados());
    }

    @FXML
    private void handleGuardarPerfil() {
        if (usuarioActual == null) return;
        String nombre = editNombreField.getText().trim();
        String correo = editCorreoField.getText().trim();
        String telefono = editTelefonoField.getText().trim();
        if (nombre.isEmpty() || correo.isEmpty()) {
            showAlert("Error", "Nombre y correo son obligatorios.");
            return;
        }
        usuarioActual.setNombre(nombre);
        usuarioActual.setCorreo(correo);
        usuarioActual.setTelefono(telefono);
        usuarioRepository.guardar(usuarioActual);
        bienvenidaLabel.setText("Bienvenido, " + nombre);
        showAlert("Perfil actualizado", "Datos guardados correctamente.");
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
        eventosListView.getItems().addAll(
                eventoRepository.filtrarEventos(
                        ciudadComboBox.getValue(), categoriaComboBox.getValue()));
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
            showAlert("Datos incompletos", "Selecciona evento, zona y asiento.");
            return;
        }
        if (usuarioActual == null) {
            showAlert("Error", "Usuario no encontrado.");
            return;
        }

        EntradaComponent entradaDecorada = crearEntradaDecorada(evento, zona);
        Entrada entrada = new Entrada("ENT-" + System.currentTimeMillis(),
                zona, asiento, entradaDecorada.getCosto(), "Activa");

        List<String> servicios = obtenerServiciosSeleccionados();
        Compra compra = new CompraBuilder()
                .setIdCompra("COM-" + System.currentTimeMillis())
                .setUsuario(usuarioActual)
                .setEvento(evento)
                .agregarEntrada(entrada)
                .build();
        compra.setServiciosAdicionales(servicios);
        compra.setTotal(entradaDecorada.getCosto());

        compraActual = compra;
        asientoCompraActual = asiento;
        compraRepository.guardarCompra(compra);

        asiento.setEstado("Reservado");
        cargarAsientosZona(zona);
        actualizarHistorial(compraRepository.listarComprasPorUsuario(usuarioActual));

        resumenCompraArea.setText(
                "Compra creada\nID: " + compra.getIdCompra()
                + "\nUsuario: " + usuarioActual.getNombre()
                + "\nEvento: " + evento.getNombre()
                + "\nZona: " + zona.getNombre()
                + "\nAsiento: " + asiento
                + "\nServicios: " + entradaDecorada.getDescripcion()
                + "\nTotal: $" + compra.getTotal()
                + "\nEstado: " + compra.getEstado());
    }

    @FXML
    private void handleModificarCompra() {
        if (compraActual == null) {
            showAlert("Sin compra", "Primero selecciona una compra del historial para modificar.");
            return;
        }
        if (!"Creada".equalsIgnoreCase(compraActual.getEstado())) {
            showAlert("No modificable", "Solo se pueden modificar compras en estado Creada.");
            return;
        }
        if (compraActual.getEntradas().isEmpty()) {
            showAlert("Error", "La compra no tiene entradas.");
            return;
        }

        Entrada entrada = compraActual.getEntradas().get(0);
        Zona zona = entrada.getZona();
        EntradaComponent base = new EntradaBase(zona.getPrecioBase(),
                "Entrada " + compraActual.getEvento().getNombre());

        EntradaComponent decorada = base;
        if (vipCheckBox.isSelected()) decorada = new VIPDecorator(decorada);
        if (seguroCheckBox.isSelected()) decorada = new SeguroDecorator(decorada);
        if (merchCheckBox.isSelected()) decorada = new MerchDecorator(decorada);
        if (parqueaderoCheckBox.isSelected()) decorada = new ParqueaderoDecorator(decorada);
        if (accesoPreferencialCheckBox.isSelected()) decorada = new AccesoPreferencialDecorator(decorada);

        compraActual.setTotal(decorada.getCosto());
        compraActual.setServiciosAdicionales(obtenerServiciosSeleccionados());

        actualizarHistorial(compraRepository.listarComprasPorUsuario(usuarioActual));
        resumenCompraArea.setText(
                "Compra modificada\nID: " + compraActual.getIdCompra()
                + "\nServicios: " + decorada.getDescripcion()
                + "\nNuevo total: $" + compraActual.getTotal()
                + "\nEstado: " + compraActual.getEstado());
        showAlert("Modificado", "Compra actualizada correctamente.");
    }

    @FXML
    private void handleCargarCompraParaModificar() {
        Compra seleccionada = historialComprasListView.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            showAlert("Sin seleccion", "Selecciona una compra del historial.");
            return;
        }
        if (!"Creada".equalsIgnoreCase(seleccionada.getEstado())) {
            showAlert("No modificable", "Solo se pueden modificar compras en estado Creada.");
            return;
        }
        compraActual = seleccionada;
        limpiarServicios();
        for (String s : seleccionada.getServiciosAdicionales()) {
            switch (s) {
                case "VIP" -> vipCheckBox.setSelected(true);
                case "Seguro" -> seguroCheckBox.setSelected(true);
                case "Merchandising" -> merchCheckBox.setSelected(true);
                case "Parqueadero" -> parqueaderoCheckBox.setSelected(true);
                case "Acceso Preferencial" -> accesoPreferencialCheckBox.setSelected(true);
            }
        }
        resumenCompraArea.setText("Compra cargada para modificar: " + seleccionada.getIdCompra()
                + "\nCambia los servicios y pulsa Modificar.");
    }

    @FXML
    private void handleCancelarCompra() {
        if (compraActual == null) {
            showAlert("Sin compra", "Primero crea o carga una compra.");
            return;
        }
        if (!"Creada".equalsIgnoreCase(compraActual.getEstado())) {
            showAlert("No cancelable", "Solo puedes cancelar compras en estado Creada.");
            return;
        }
        compraActual.setEstado("Cancelada");
        if (asientoCompraActual != null) asientoCompraActual.setEstado("Disponible");
        Zona zona = zonaComboBox.getValue();
        if (zona != null) cargarAsientosZona(zona);
        actualizarHistorial(compraRepository.listarComprasPorUsuario(usuarioActual));
        resumenCompraArea.setText(resumenCompraArea.getText()
                + "\n\nCompra cancelada. Asiento liberado.");
        showAlert("Cancelada", "Compra cancelada correctamente.");
    }

    @FXML
    private void handlePagarCompra() {
        if (compraActual == null) {
            showAlert("Sin compra", "Primero crea una compra.");
            return;
        }
        if (!"Creada".equalsIgnoreCase(compraActual.getEstado())) {
            showAlert("No disponible", "La compra ya fue procesada.");
            return;
        }
        String metodo = metodoPagoComboBox.getValue();
        if (metodo == null || metodo.isBlank()) {
            showAlert("Metodo", "Selecciona un metodo de pago.");
            return;
        }
        PagoStrategy estrategia = crearEstrategia(metodo);
        PagoContext ctx = new PagoContext();
        ctx.setEstrategia(estrategia);
        boolean ok = ctx.ejecutarPago(compraActual.getTotal());
        if (ok) {
            compraActual.setEstado("Pagada");
            if (asientoCompraActual != null) asientoCompraActual.setEstado("Vendido");
            actualizarHistorial(compraRepository.listarComprasPorUsuario(usuarioActual));
            resumenCompraArea.setText(resumenCompraArea.getText()
                    + "\n\nPago realizado"
                    + "\nMetodo: " + estrategia.getMetodoPago()
                    + "\nEstado: Pagada"
                    + "\nComprobante: PAG-" + System.currentTimeMillis());
            showAlert("Pago exitoso", "Pago procesado correctamente.");
        } else {
            showAlert("Pago rechazado", "No se pudo procesar el pago.");
        }
    }

    @FXML
    private void handleFiltrarHistorial() {
        if (usuarioActual == null) return;
        String estado = estadoHistorialComboBox.getValue();
        String eventoNombre = eventoHistorialComboBox.getValue();
        String fecha = fechaHistorialField.getText().trim();

        List<Compra> base = compraRepository.listarComprasPorUsuario(usuarioActual);
        List<Compra> filtradas = new ArrayList<>();
        for (Compra c : base) {
            boolean coincideEstado = estado == null || "Todas".equalsIgnoreCase(estado)
                    || c.getEstado().equalsIgnoreCase(estado);
            boolean coincideEvento = eventoNombre == null || "Todos".equalsIgnoreCase(eventoNombre)
                    || (c.getEvento() != null && c.getEvento().getNombre().equalsIgnoreCase(eventoNombre));
            boolean coincideFecha = fecha.isEmpty()
                    || (c.getFechaCreacion() != null && c.getFechaCreacion().startsWith(fecha));
            if (coincideEstado && coincideEvento && coincideFecha) filtradas.add(c);
        }
        actualizarHistorial(filtradas);
    }

    @FXML
    private void handleActualizarHistorial() {
        estadoHistorialComboBox.setValue("Todas");
        if (eventoHistorialComboBox.getItems().contains("Todos"))
            eventoHistorialComboBox.setValue("Todos");
        fechaHistorialField.clear();
        if (usuarioActual != null)
            actualizarHistorial(compraRepository.listarComprasPorUsuario(usuarioActual));
    }

    @FXML
    private void handleExportarCSV() {
        if (usuarioActual == null) return;
        List<Compra> compras = compraRepository.listarComprasPorUsuario(usuarioActual);
        if (compras.isEmpty()) { showAlert("Sin compras", "No hay compras para exportar."); return; }
        File dest = elegirArchivo("Guardar CSV",
                "compras_" + usuarioActual.getIdUsuario() + ".csv", "CSV", "*.csv");
        if (dest == null) return;
        try {
            csvReport.generarReporteComprasUsuario(compras, dest);
            showAlert("Exportado", "CSV generado en: " + dest.getPath());
        } catch (IOException e) {
            showAlert("Error", "No se pudo generar el CSV.");
        }
    }

    @FXML
    private void handleExportarPDF() {
        if (usuarioActual == null) return;
        List<Compra> compras = compraRepository.listarComprasPorUsuario(usuarioActual);
        if (compras.isEmpty()) { showAlert("Sin compras", "No hay compras para exportar."); return; }
        File dest = elegirArchivo("Guardar PDF",
                "compras_" + usuarioActual.getIdUsuario() + ".pdf", "PDF", "*.pdf");
        if (dest == null) return;
        try {
            pdfReport.generarReporteComprasUsuario(compras, dest);
            showAlert("Exportado", "PDF generado en: " + dest.getPath());
        } catch (IOException e) {
            showAlert("Error", "No se pudo generar el PDF.");
        }
    }

    private File elegirArchivo(String titulo, String nombre, String desc, String ext) {
        FileChooser fc = new FileChooser();
        fc.setTitle(titulo);
        fc.setInitialFileName(nombre);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(desc, ext));
        return fc.showSaveDialog(resumenCompraArea.getScene().getWindow());
    }

    private EntradaComponent crearEntradaDecorada(Evento evento, Zona zona) {
        EntradaComponent e = new EntradaBase(zona.getPrecioBase(),
                "Entrada " + (evento != null ? evento.getNombre() : "") + " - " + zona.getNombre());
        if (vipCheckBox.isSelected()) e = new VIPDecorator(e);
        if (seguroCheckBox.isSelected()) e = new SeguroDecorator(e);
        if (merchCheckBox.isSelected()) e = new MerchDecorator(e);
        if (parqueaderoCheckBox.isSelected()) e = new ParqueaderoDecorator(e);
        if (accesoPreferencialCheckBox.isSelected()) e = new AccesoPreferencialDecorator(e);
        return e;
    }

    private List<String> obtenerServiciosSeleccionados() {
        List<String> servicios = new ArrayList<>();
        if (vipCheckBox.isSelected()) servicios.add("VIP");
        if (seguroCheckBox.isSelected()) servicios.add("Seguro");
        if (merchCheckBox.isSelected()) servicios.add("Merchandising");
        if (parqueaderoCheckBox.isSelected()) servicios.add("Parqueadero");
        if (accesoPreferencialCheckBox.isSelected()) servicios.add("Acceso Preferencial");
        return servicios;
    }

    private PagoStrategy crearEstrategia(String metodo) {
        if ("PSE".equalsIgnoreCase(metodo)) return new PagoPSE();
        if ("PayPal".equalsIgnoreCase(metodo)) return new PagoPayPal();
        return new PagoTarjeta();
    }

    private void actualizarTotal() {
        Evento evento = eventosListView.getSelectionModel().getSelectedItem();
        Zona zona = zonaComboBox.getValue();
        if (zona == null) { precioZonaLabel.setText("Precio: $0"); return; }
        EntradaComponent e = crearEntradaDecorada(evento, zona);
        precioZonaLabel.setText("Total: $" + e.getCosto());
    }

    private void actualizarHistorial(List<Compra> compras) {
        historialComprasListView.getItems().setAll(compras);
    }

    private void mostrarDetalleEvento(Evento evento) {
        if (evento == null) { detalleEventoArea.clear(); return; }
        detalleEventoArea.setText(
                "Nombre: " + evento.getNombre()
                + "\nCategoria: " + evento.getCategoria()
                + "\nDescripcion: " + evento.getDescripcion()
                + "\nCiudad: " + evento.getCiudad()
                + "\nFecha: " + evento.getFecha()
                + "\nRecinto: " + evento.getRecinto()
                + "\nAforo: " + evento.getAforo()
                + "\nEstado: " + evento.getEstado()
                + "\nPolitica cancelacion: " + evento.getPoliticaCancelacion()
                + "\nPolitica reembolso: " + evento.getPoliticaReembolso());
    }

    private void cargarZonasEvento(Evento evento) {
        zonaComboBox.getItems().clear();
        asientoComboBox.getItems().clear();
        precioZonaLabel.setText("Precio: $0");
        limpiarServicios();
        resumenCompraArea.clear();
        if (evento == null) return;
        zonaComboBox.getItems().addAll(
                zonaRepository.listarZonasPorEvento(evento.getIdEvento()));
    }

    private void cargarAsientosZona(Zona zona) {
        asientoComboBox.getItems().clear();
        if (zona == null) { precioZonaLabel.setText("Precio: $0"); return; }
        asientoComboBox.getItems().addAll(zonaRepository.listarAsientosDisponibles(zona));
        actualizarTotal();
    }

    private void limpiarServicios() {
        vipCheckBox.setSelected(false);
        seguroCheckBox.setSelected(false);
        merchCheckBox.setSelected(false);
        parqueaderoCheckBox.setSelected(false);
        accesoPreferencialCheckBox.setSelected(false);
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/org/mastereventos/ui/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            Stage stage = (Stage) bienvenidaLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("MasterEventos - Login");
        } catch (Exception e) {
            showAlert("Error", "No se pudo cerrar sesion.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
