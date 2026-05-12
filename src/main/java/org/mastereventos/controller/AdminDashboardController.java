package org.mastereventos.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mastereventos.model.*;
import org.mastereventos.report.CSVReportGenerator;
import org.mastereventos.repository.*;
import org.mastereventos.singleton.DataStore;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDashboardController {

    // ── USUARIOS ──────────────────────────────────────────────────────────────
    @FXML private ListView<Usuario> usuariosListView;
    @FXML private TextField uIdField;
    @FXML private TextField uNombreField;
    @FXML private TextField uCorreoField;
    @FXML private TextField uTelefonoField;
    @FXML private TextField uPasswordField;
    @FXML private ComboBox<String> uRolComboBox;

    // ── EVENTOS ───────────────────────────────────────────────────────────────
    @FXML private ListView<Evento> eventosAdminListView;
    @FXML private TextField eIdField;
    @FXML private TextField eNombreField;
    @FXML private TextField eCategoriaField;
    @FXML private TextField eCiudadField;
    @FXML private TextField eFechaField;
    @FXML private TextField eRecintoField;
    @FXML private TextField eAforoField;
    @FXML private TextArea eDescripcionArea;
    @FXML private ComboBox<String> eEstadoComboBox;

    // ── ZONAS ─────────────────────────────────────────────────────────────────
    @FXML private ComboBox<Evento> eventoZonaComboBox;
    @FXML private ListView<Zona> zonasListView;
    @FXML private TextField zIdField;
    @FXML private TextField zNombreField;
    @FXML private TextField zCapacidadField;
    @FXML private TextField zPrecioField;

    // ── ASIENTOS ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<Evento> eventoAsientoComboBox;
    @FXML private ComboBox<Zona> zonaAsientoComboBox;
    @FXML private ListView<Asiento> asientosListView;

    // ── COMPRAS ───────────────────────────────────────────────────────────────
    @FXML private ComboBox<String> compraEstadoFiltro;
    @FXML private ListView<Compra> comprasListView;
    @FXML private TextArea detalleCompraArea;

    // ── INCIDENCIAS ───────────────────────────────────────────────────────────
    @FXML private ListView<Incidencia> incidenciasListView;
    @FXML private ComboBox<String> tipoIncidenciaComboBox;
    @FXML private ComboBox<String> filtroTipoIncidencia;
    @FXML private TextField incidenciaEntidadField;
    @FXML private TextArea incidenciaDescripcionArea;

    // ── MÉTRICAS ──────────────────────────────────────────────────────────────
    @FXML private LineChart<String, Number> ventasPeriodoChart;
    @FXML private BarChart<String, Number> ocupacionZonaChart;
    @FXML private PieChart ingresoServiciosChart;
    @FXML private Label totalVentasLabel;
    @FXML private Label tasaCancelacionLabel;
    @FXML private Label topEventoLabel;
    @FXML private ComboBox<String> tipoReporteComboBox;

    private Usuario usuarioActual;

    private final EventoRepository eventoRepo = new EventoRepository();
    private final UsuarioRepository usuarioRepo = new UsuarioRepository();
    private final ZonaRepository zonaRepo = new ZonaRepository();
    private final CompraRepository compraRepo = new CompraRepository();
    private final IncidenciaRepository incidenciaRepo = new IncidenciaRepository();
    private final CSVReportGenerator csvReport = new CSVReportGenerator();

    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarGraficas();
        cargarResumenMetricas();
    }

    @FXML
    private void initialize() {
        cargarComboBoxes();
        cargarUsuarios();
        cargarEventosAdmin();
        cargarComprasAdmin();
        cargarIncidencias();

        usuariosListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarDatosUsuario(sel));
        eventosAdminListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarDatosEvento(sel));
        eventoZonaComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarZonas(sel));
        zonasListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarDatosZona(sel));
        eventoAsientoComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarZonasAsiento(sel));
        zonaAsientoComboBox.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> cargarAsientosZona(sel));
        comprasListView.getSelectionModel().selectedItemProperty()
                .addListener((obs, ant, sel) -> mostrarDetalleCompra(sel));
        configurarCeldasCompras();
    }

    private void configurarCeldasCompras() {
        comprasListView.setCellFactory(lv -> new ListCell<Compra>() {
            @Override
            protected void updateItem(Compra c, boolean empty) {
                super.updateItem(c, empty);
                if (empty || c == null) { setText(null); return; }
                String ev = c.getEvento() != null ? c.getEvento().getNombre() : "Sin evento";
                String us = c.getUsuario() != null ? c.getUsuario().getNombre() : "Sin usuario";
                setText(c.getIdCompra() + " | " + us + " | " + ev
                        + " | $" + c.getTotal() + " | " + c.getEstado());
            }
        });
    }

    private void cargarComboBoxes() {
        uRolComboBox.getItems().addAll("USUARIO", "ADMIN");
        uRolComboBox.setValue("USUARIO");

        eEstadoComboBox.getItems().addAll("Borrador", "Publicado", "Pausado", "Cancelado", "Finalizado");
        eEstadoComboBox.setValue("Borrador");

        compraEstadoFiltro.getItems().addAll("Todas", "Creada", "Pagada",
                "Confirmada", "Cancelada", "Reembolsada", "Incidencia");
        compraEstadoFiltro.setValue("Todas");

        tipoIncidenciaComboBox.getItems().addAll("Error de pago", "Doble compra",
                "Cancelacion masiva", "Asiento ocupado", "Otro");
        tipoIncidenciaComboBox.setValue("Error de pago");

        filtroTipoIncidencia.getItems().addAll("Todos", "Error de pago", "Doble compra",
                "Cancelacion masiva", "Asiento ocupado", "Otro");
        filtroTipoIncidencia.setValue("Todos");

        tipoReporteComboBox.getItems().addAll(
                "Ventas por periodo", "Ocupacion por zona",
                "Ingresos por servicios", "Tasa de cancelacion", "Top eventos");
        tipoReporteComboBox.setValue("Ventas por periodo");

        eventoZonaComboBox.getItems().setAll(eventoRepo.getEventos());
        eventoAsientoComboBox.getItems().setAll(eventoRepo.getEventos());
    }

    // ── CRUD USUARIOS ─────────────────────────────────────────────────────────

    private void cargarUsuarios() {
        usuariosListView.getItems().setAll(usuarioRepo.getUsuarios());
    }

    private void cargarDatosUsuario(Usuario u) {
        if (u == null) return;
        uIdField.setText(u.getIdUsuario());
        uNombreField.setText(u.getNombre());
        uCorreoField.setText(u.getCorreo());
        uTelefonoField.setText(u.getTelefono());
        uPasswordField.setText(u.getPassword());
        uRolComboBox.setValue(u.getRol().name());
    }

    @FXML
    private void handleNuevoUsuario() {
        uIdField.setText(usuarioRepo.generarNuevoId());
        uNombreField.clear();
        uCorreoField.clear();
        uTelefonoField.clear();
        uPasswordField.setText("1234");
        uRolComboBox.setValue("USUARIO");
    }

    @FXML
    private void handleGuardarUsuario() {
        String id = uIdField.getText().trim();
        String nombre = uNombreField.getText().trim();
        String correo = uCorreoField.getText().trim();
        if (id.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            showAlert("Error", "ID, nombre y correo son obligatorios.");
            return;
        }
        Rol rol = "ADMIN".equals(uRolComboBox.getValue()) ? Rol.ADMIN : Rol.USUARIO;
        Usuario u = new Usuario(id, nombre, correo,
                uTelefonoField.getText().trim(),
                uPasswordField.getText().trim(), rol);
        usuarioRepo.guardar(u);
        cargarUsuarios();
        showAlert("Guardado", "Usuario guardado correctamente.");
    }

    @FXML
    private void handleEliminarUsuario() {
        Usuario sel = usuariosListView.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Selecciona un usuario."); return; }
        if ("admin@mastereventos.com".equalsIgnoreCase(sel.getCorreo())) {
            showAlert("Error", "No se puede eliminar el administrador principal.");
            return;
        }
        usuarioRepo.eliminar(sel.getIdUsuario());
        cargarUsuarios();
        showAlert("Eliminado", "Usuario eliminado.");
    }

    // ── CRUD EVENTOS ──────────────────────────────────────────────────────────

    private void cargarEventosAdmin() {
        eventosAdminListView.getItems().setAll(eventoRepo.getEventos());
        eventoZonaComboBox.getItems().setAll(eventoRepo.getEventos());
        eventoAsientoComboBox.getItems().setAll(eventoRepo.getEventos());
    }

    private void cargarDatosEvento(Evento e) {
        if (e == null) return;
        eIdField.setText(e.getIdEvento());
        eNombreField.setText(e.getNombre());
        eCategoriaField.setText(e.getCategoria());
        eCiudadField.setText(e.getCiudad());
        eFechaField.setText(e.getFecha());
        eRecintoField.setText(e.getRecinto());
        eAforoField.setText(String.valueOf(e.getAforo()));
        eDescripcionArea.setText(e.getDescripcion());
        eEstadoComboBox.setValue(e.getEstado());
    }

    @FXML
    private void handleNuevoEvento() {
        eIdField.setText(eventoRepo.generarNuevoId());
        eNombreField.clear();
        eCategoriaField.clear();
        eCiudadField.clear();
        eFechaField.clear();
        eRecintoField.clear();
        eAforoField.setText("0");
        eDescripcionArea.clear();
        eEstadoComboBox.setValue("Borrador");
    }

    @FXML
    private void handleGuardarEvento() {
        String id = eIdField.getText().trim();
        String nombre = eNombreField.getText().trim();
        if (id.isEmpty() || nombre.isEmpty()) {
            showAlert("Error", "ID y nombre son obligatorios.");
            return;
        }
        int aforo = 0;
        try { aforo = Integer.parseInt(eAforoField.getText().trim()); }
        catch (NumberFormatException ignored) {}
        Evento e = new Evento(id, nombre, eCategoriaField.getText().trim(),
                eDescripcionArea.getText().trim(), eCiudadField.getText().trim(),
                eFechaField.getText().trim(), eEstadoComboBox.getValue(),
                eRecintoField.getText().trim(), aforo,
                "Cancelacion hasta 48h antes.", "Reembolso 80% con 72h.");
        eventoRepo.guardar(e);
        cargarEventosAdmin();
        showAlert("Guardado", "Evento guardado correctamente.");
    }

    @FXML
    private void handleEliminarEvento() {
        Evento sel = eventosAdminListView.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Selecciona un evento."); return; }
        eventoRepo.eliminar(sel.getIdEvento());
        cargarEventosAdmin();
        showAlert("Eliminado", "Evento eliminado.");
    }

    @FXML
    private void handlePublicarEvento() {
        cambiarEstadoEvento("Publicado");
    }

    @FXML
    private void handlePausarEvento() {
        cambiarEstadoEvento("Pausado");
    }

    @FXML
    private void handleCancelarEvento() {
        cambiarEstadoEvento("Cancelado");
    }

    @FXML
    private void handleFinalizarEvento() {
        cambiarEstadoEvento("Finalizado");
    }

    private void cambiarEstadoEvento(String nuevoEstado) {
        Evento sel = eventosAdminListView.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Selecciona un evento."); return; }
        eventoRepo.cambiarEstado(sel.getIdEvento(), nuevoEstado);
        cargarEventosAdmin();
        showAlert("Estado actualizado", sel.getNombre() + " → " + nuevoEstado);
    }

    // ── CRUD ZONAS ────────────────────────────────────────────────────────────

    private void cargarZonas(Evento evento) {
        zonasListView.getItems().clear();
        if (evento == null) return;
        zonasListView.getItems().setAll(zonaRepo.listarZonasPorEvento(evento.getIdEvento()));
    }

    private void cargarDatosZona(Zona z) {
        if (z == null) return;
        zIdField.setText(z.getIdZona());
        zNombreField.setText(z.getNombre());
        zCapacidadField.setText(String.valueOf(z.getCapacidad()));
        zPrecioField.setText(String.valueOf(z.getPrecioBase()));
    }

    @FXML
    private void handleNuevaZona() {
        zIdField.setText(zonaRepo.generarNuevoIdZona());
        zNombreField.clear();
        zCapacidadField.setText("100");
        zPrecioField.setText("0");
    }

    @FXML
    private void handleGuardarZona() {
        Evento evento = eventoZonaComboBox.getValue();
        if (evento == null) { showAlert("Error", "Selecciona un evento."); return; }
        String id = zIdField.getText().trim();
        String nombre = zNombreField.getText().trim();
        if (id.isEmpty() || nombre.isEmpty()) {
            showAlert("Error", "ID y nombre de zona son obligatorios.");
            return;
        }
        int cap = 0;
        double precio = 0;
        try {
            cap = Integer.parseInt(zCapacidadField.getText().trim());
            precio = Double.parseDouble(zPrecioField.getText().trim());
        } catch (NumberFormatException ignored) {}

        Zona existente = zonaRepo.buscarZona(evento.getIdEvento(), id);
        if (existente != null) {
            existente.setNombre(nombre);
            existente.setCapacidad(cap);
            existente.setPrecioBase(precio);
        } else {
            Zona nueva = new Zona(id, nombre, cap, precio);
            zonaRepo.agregarZona(evento.getIdEvento(), nueva);
        }
        cargarZonas(evento);
        showAlert("Guardado", "Zona guardada.");
    }

    @FXML
    private void handleEliminarZona() {
        Evento evento = eventoZonaComboBox.getValue();
        Zona zona = zonasListView.getSelectionModel().getSelectedItem();
        if (evento == null || zona == null) {
            showAlert("Error", "Selecciona evento y zona.");
            return;
        }
        zonaRepo.eliminarZona(evento.getIdEvento(), zona.getIdZona());
        cargarZonas(evento);
        showAlert("Eliminada", "Zona eliminada.");
    }

    // ── GESTIÓN ASIENTOS ──────────────────────────────────────────────────────

    private void cargarZonasAsiento(Evento evento) {
        zonaAsientoComboBox.getItems().clear();
        asientosListView.getItems().clear();
        if (evento == null) return;
        zonaAsientoComboBox.getItems().setAll(
                zonaRepo.listarZonasPorEvento(evento.getIdEvento()));
    }

    private void cargarAsientosZona(Zona zona) {
        asientosListView.getItems().clear();
        if (zona == null) return;
        asientosListView.getItems().setAll(zonaRepo.listarTodosAsientos(zona));
    }

    @FXML
    private void handleHabilitarAsiento() {
        cambiarEstadoAsiento("Disponible");
    }

    @FXML
    private void handleBloquearAsiento() {
        cambiarEstadoAsiento("Bloqueado");
    }

    @FXML
    private void handleLiberarAsiento() {
        cambiarEstadoAsiento("Disponible");
    }

    private void cambiarEstadoAsiento(String estado) {
        Zona zona = zonaAsientoComboBox.getValue();
        Asiento asiento = asientosListView.getSelectionModel().getSelectedItem();
        if (zona == null || asiento == null) {
            showAlert("Error", "Selecciona zona y asiento.");
            return;
        }
        zonaRepo.cambiarEstadoAsiento(zona, asiento.getIdAsiento(), estado);
        cargarAsientosZona(zona);
        showAlert("Actualizado", "Asiento " + asiento.getIdAsiento() + " → " + estado);
    }

    @FXML
    private void handleAgregarAsiento() {
        Evento evento = eventoAsientoComboBox.getValue();
        Zona zona = zonaAsientoComboBox.getValue();
        if (evento == null || zona == null) {
            showAlert("Error", "Selecciona evento y zona.");
            return;
        }
        String id = zonaRepo.generarNuevoIdAsiento(zona);
        zona.agregarAsiento(new Asiento(id, "A", zona.getAsientos().size() + 1, "Disponible"));
        cargarAsientosZona(zona);
        showAlert("Agregado", "Asiento " + id + " agregado.");
    }

    // ── GESTIÓN COMPRAS ───────────────────────────────────────────────────────

    private void cargarComprasAdmin() {
        comprasListView.getItems().setAll(compraRepo.listarTodas());
    }

    private void mostrarDetalleCompra(Compra c) {
        if (c == null) { detalleCompraArea.clear(); return; }
        String usuario = c.getUsuario() != null ? c.getUsuario().getNombre() : "N/A";
        String evento = c.getEvento() != null ? c.getEvento().getNombre() : "N/A";
        String servicios = c.getServiciosAdicionales().isEmpty()
                ? "Ninguno" : String.join(", ", c.getServiciosAdicionales());
        detalleCompraArea.setText(
                "ID: " + c.getIdCompra()
                + "\nUsuario: " + usuario
                + "\nEvento: " + evento
                + "\nTotal: $" + c.getTotal()
                + "\nEstado: " + c.getEstado()
                + "\nFecha: " + c.getFechaCreacion()
                + "\nServicios: " + servicios
                + "\nEntradas: " + c.getEntradas().size());
    }

    @FXML
    private void handleFiltrarCompras() {
        String estado = compraEstadoFiltro.getValue();
        comprasListView.getItems().setAll(compraRepo.filtrarComprasPorEstadoAdmin(estado));
    }

    @FXML
    private void handleActualizarCompras() {
        compraEstadoFiltro.setValue("Todas");
        cargarComprasAdmin();
    }

    @FXML
    private void handleCancelarCompraAdmin() {
        Compra sel = comprasListView.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Selecciona una compra."); return; }
        if ("Cancelada".equalsIgnoreCase(sel.getEstado())
                || "Reembolsada".equalsIgnoreCase(sel.getEstado())) {
            showAlert("Error", "La compra ya esta cancelada o reembolsada.");
            return;
        }
        sel.setEstado("Cancelada");
        cargarComprasAdmin();
        mostrarDetalleCompra(sel);
        registrarIncidenciaAutomatica("Cancelacion masiva",
                "Cancelacion administrativa de compra " + sel.getIdCompra(), sel.getIdCompra());
        showAlert("Cancelada", "Compra cancelada.");
    }

    @FXML
    private void handleReembolsarCompra() {
        Compra sel = comprasListView.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Selecciona una compra."); return; }
        if (!"Pagada".equalsIgnoreCase(sel.getEstado())
                && !"Confirmada".equalsIgnoreCase(sel.getEstado())) {
            showAlert("Error", "Solo se pueden reembolsar compras Pagadas o Confirmadas.");
            return;
        }
        sel.setEstado("Reembolsada");
        for (Entrada e : sel.getEntradas()) e.setEstado("Anulada");
        cargarComprasAdmin();
        mostrarDetalleCompra(sel);
        showAlert("Reembolsada", "Compra reembolsada. Entradas anuladas.");
    }

    @FXML
    private void handleConfirmarCompraAdmin() {
        Compra sel = comprasListView.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Error", "Selecciona una compra."); return; }
        if (!"Pagada".equalsIgnoreCase(sel.getEstado())) {
            showAlert("Error", "Solo se pueden confirmar compras Pagadas.");
            return;
        }
        sel.setEstado("Confirmada");
        cargarComprasAdmin();
        mostrarDetalleCompra(sel);
        showAlert("Confirmada", "Compra confirmada.");
    }

    // ── GESTIÓN INCIDENCIAS ───────────────────────────────────────────────────

    private void cargarIncidencias() {
        incidenciasListView.getItems().setAll(incidenciaRepo.listarTodas());
    }

    @FXML
    private void handleRegistrarIncidencia() {
        String tipo = tipoIncidenciaComboBox.getValue();
        String entidad = incidenciaEntidadField.getText().trim();
        String descripcion = incidenciaDescripcionArea.getText().trim();
        if (entidad.isEmpty() || descripcion.isEmpty()) {
            showAlert("Error", "Entidad afectada y descripcion son obligatorias.");
            return;
        }
        registrarIncidenciaAutomatica(tipo, descripcion, entidad);
        incidenciaEntidadField.clear();
        incidenciaDescripcionArea.clear();
        showAlert("Registrada", "Incidencia registrada.");
    }

    private void registrarIncidenciaAutomatica(String tipo, String descripcion, String entidad) {
        Incidencia inc = new Incidencia(
                incidenciaRepo.generarNuevoId(),
                tipo, descripcion,
                LocalDate.now().toString(),
                entidad);
        incidenciaRepo.guardar(inc);
        cargarIncidencias();
    }

    @FXML
    private void handleFiltrarIncidencias() {
        String tipo = filtroTipoIncidencia.getValue();
        if ("Todos".equalsIgnoreCase(tipo)) {
            incidenciasListView.getItems().setAll(incidenciaRepo.listarTodas());
        } else {
            incidenciasListView.getItems().setAll(incidenciaRepo.filtrarPorTipo(tipo));
        }
    }

    // ── MÉTRICAS Y GRÁFICAS ───────────────────────────────────────────────────

    private void cargarGraficas() {
        if (ventasPeriodoChart != null) cargarLineChart();
        if (ocupacionZonaChart != null) cargarBarChart();
        if (ingresoServiciosChart != null) cargarPieChart();
    }

    private void cargarLineChart() {
        ventasPeriodoChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ventas 2026");
        List<Compra> compras = compraRepo.listarTodas();
        java.util.Map<String, Integer> porMes = new java.util.LinkedHashMap<>();
        porMes.put("2026-01", 0); porMes.put("2026-02", 0); porMes.put("2026-03", 0);
        porMes.put("2026-04", 0); porMes.put("2026-05", 0); porMes.put("2026-06", 0);
        for (Compra c : compras) {
            if (c.getFechaCreacion() != null && c.getFechaCreacion().length() >= 7) {
                String mes = c.getFechaCreacion().substring(0, 7);
                porMes.merge(mes, 1, Integer::sum);
            }
        }
        for (Map.Entry<String, Integer> entry : porMes.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        ventasPeriodoChart.getData().add(series);
    }

    private void cargarBarChart() {
        ocupacionZonaChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Asientos vendidos");
        Map<String, List<Zona>> zonas = DataStore.getInstancia().getZonasPorEvento();
        for (Map.Entry<String, List<Zona>> entry : zonas.entrySet()) {
            for (Zona z : entry.getValue()) {
                long vendidos = z.getAsientos().stream()
                        .filter(a -> "Vendido".equalsIgnoreCase(a.getEstado())).count();
                series.getData().add(new XYChart.Data<>(z.getNombre() + "\n(" + entry.getKey() + ")", vendidos));
            }
        }
        ocupacionZonaChart.getData().add(series);
    }

    private void cargarPieChart() {
        ingresoServiciosChart.getData().clear();
        List<Compra> compras = compraRepo.listarTodas();
        String[] servicios = {"VIP", "Seguro", "Merchandising", "Parqueadero", "Acceso Preferencial"};
        for (String s : servicios) {
            double ingreso = compraRepo.sumarIngresosPorServicio(s);
            if (ingreso > 0) {
                ingresoServiciosChart.getData().add(new PieChart.Data(s + " $" + (long) ingreso, ingreso));
            }
        }
    }

    private void cargarResumenMetricas() {
        if (totalVentasLabel == null) return;
        List<Compra> compras = compraRepo.listarTodas();
        long total = compras.size();
        long canceladas = compraRepo.contarPorEstado("Cancelada")
                + compraRepo.contarPorEstado("Reembolsada");
        double tasa = total > 0 ? (canceladas * 100.0 / total) : 0;

        totalVentasLabel.setText("Total compras: " + total);
        tasaCancelacionLabel.setText(String.format("Tasa cancelacion: %.1f%%", tasa));

        java.util.Map<String, Integer> conteo = new java.util.HashMap<>();
        for (Compra c : compras) {
            if (c.getEvento() != null)
                conteo.merge(c.getEvento().getNombre(), 1, Integer::sum);
        }
        String top = conteo.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + " (" + e.getValue() + " ventas)")
                .orElse("Sin datos");
        topEventoLabel.setText("Top evento: " + top);
    }

    @FXML
    private void handleActualizarGraficas() {
        cargarGraficas();
        cargarResumenMetricas();
        showAlert("Actualizado", "Graficas actualizadas.");
    }

    @FXML
    private void handleGenerarReporte() {
        String tipo = tipoReporteComboBox.getValue();
        if (tipo == null) return;

        String ext = "*.csv";
        String nombre = "reporte_admin.csv";
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar reporte");
        fc.setInitialFileName(nombre);
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", ext));
        File dest = fc.showSaveDialog(comprasListView.getScene().getWindow());
        if (dest == null) return;

        try {
            List<Compra> compras = compraRepo.listarTodas();
            switch (tipo) {
                case "Ventas por periodo" -> csvReport.generarReporteVentasPorPeriodo(compras, dest);
                case "Ocupacion por zona" -> csvReport.generarReporteOcupacionPorZona(
                        DataStore.getInstancia().getZonasPorEvento(), dest);
                case "Ingresos por servicios" -> csvReport.generarReporteIngresosServicios(compras, dest);
                case "Tasa de cancelacion" -> csvReport.generarReporteTasaCancelacion(compras, dest);
                case "Top eventos" -> csvReport.generarReporteTopEventos(compras, dest);
            }
            showAlert("Reporte generado", "Guardado en: " + dest.getPath());
        } catch (IOException e) {
            showAlert("Error", "No se pudo generar el reporte.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/org/mastereventos/ui/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            Stage stage = (Stage) eventosAdminListView.getScene().getWindow();
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
