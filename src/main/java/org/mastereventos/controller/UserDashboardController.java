package org.mastereventos.controller;

import org.mastereventos.builder.CompraBuilder;
import org.mastereventos.decorator.EntradaBase;
import org.mastereventos.decorator.EntradaComponent;
import org.mastereventos.decorator.MerchDecorator;
import org.mastereventos.decorator.SeguroDecorator;
import org.mastereventos.decorator.VIPDecorator;
import org.mastereventos.model.*;
import org.mastereventos.repository.EventoRepository;
import org.mastereventos.repository.ZonaRepository;
import org.mastereventos.repository.CompraRepository;
import org.mastereventos.repository.IncidenciaRepository;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

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

    private Usuario usuarioActual;

    private final EventoRepository eventoRepository = new EventoRepository();
    private final ZonaRepository zonaRepository = new ZonaRepository();
    private final CompraRepository compraRepository = new CompraRepository();
    private final IncidenciaRepository incidenciaRepository = new IncidenciaRepository();

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    @FXML
    private void initialize() {
        cargarFiltros();
        cargarEventos();

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

        if (!asiento.estaDisponible()) {

            Incidencia incidencia = new Incidencia(
                    "INC-" + System.currentTimeMillis(),
                    "Asiento ocupado",
                    "Intento de compra de asiento no disponible",
                    asiento.getIdAsiento()
            );

            incidenciaRepository.registrarIncidencia(incidencia);

            showAlert(
                    "Asiento no disponible",
                    "El asiento seleccionado ya no está disponible."
            );

            return;
        }

        EntradaComponent entradaDecorada = crearEntradaDecorada(evento, zona);

        Entrada entrada = new Entrada(
                "ENT-" + System.currentTimeMillis(),
                zona,
                asiento,
                entradaDecorada.getCosto()
        );

        Compra compra = new CompraBuilder()
                .setIdCompra("COM-" + System.currentTimeMillis())
                .setUsuario(usuarioActual)
                .setEvento(evento)
                .agregarEntrada(entrada)
                .build();
        compraRepository.guardarCompra(compra);

        asiento.setEstado(EstadoAsiento.RESERVADO);
        cargarAsientosZona(zona);

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
