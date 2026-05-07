package org.mastereventos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import org.mastereventos.model.Compra;
import org.mastereventos.model.EstadoEvento;
import org.mastereventos.model.Evento;
import org.mastereventos.model.Incidencia;
import org.mastereventos.repository.CompraRepository;
import org.mastereventos.repository.EventoRepository;
import org.mastereventos.repository.IncidenciaRepository;

public class AdminDashboardController {

    @FXML
    private ListView<Evento> eventosListView;

    @FXML
    private ListView<Compra> comprasListView;

    @FXML
    private ListView<Incidencia> incidenciasListView;

    @FXML
    private PieChart ventasChart;

    private final EventoRepository eventoRepository =
            new EventoRepository();

    private final CompraRepository compraRepository =
            new CompraRepository();

    private final IncidenciaRepository incidenciaRepository =
            new IncidenciaRepository();

    @FXML
    private void initialize() {

        configurarEventos();
        configurarCompras();
        configurarIncidencias();

        cargarEventos();
        cargarCompras();
        cargarIncidencias();

        cargarMetricas();
    }

    // =========================
    // EVENTOS
    // =========================

    private void configurarEventos() {

        eventosListView.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(
                    Evento evento,
                    boolean empty) {

                super.updateItem(evento, empty);

                if (empty || evento == null) {

                    setText(null);

                } else {

                    setText(
                            evento.getNombre()
                                    + " | "
                                    + evento.getCiudad()
                                    + " | "
                                    + evento.getEstado()
                    );
                }
            }
        });
    }

    private void cargarEventos() {

        eventosListView.getItems().clear();

        eventosListView.getItems()
                .addAll(eventoRepository.listarEventos());
    }

    @FXML
    private void handlePublicarEvento() {

        Evento evento =
                eventosListView.getSelectionModel().getSelectedItem();

        if (evento == null) {

            showAlert(
                    "Error",
                    "Selecciona un evento."
            );

            return;
        }

        evento.setEstado(EstadoEvento.PUBLICADO);

        eventosListView.refresh();

        showAlert(
                "Evento publicado",
                "El evento fue publicado."
        );
    }

    @FXML
    private void handlePausarEvento() {

        Evento evento =
                eventosListView.getSelectionModel().getSelectedItem();

        if (evento == null) {

            showAlert(
                    "Error",
                    "Selecciona un evento."
            );

            return;
        }

        evento.setEstado(EstadoEvento.PAUSADO);

        eventosListView.refresh();

        showAlert(
                "Evento pausado",
                "El evento fue pausado."
        );
    }

    @FXML
    private void handleCancelarEvento() {

        Evento evento =
                eventosListView.getSelectionModel().getSelectedItem();

        if (evento == null) {

            showAlert(
                    "Error",
                    "Selecciona un evento."
            );

            return;
        }

        evento.setEstado(EstadoEvento.CANCELADO);

        eventosListView.refresh();

        showAlert(
                "Evento cancelado",
                "El evento fue cancelado."
        );
    }

    // =========================
    // COMPRAS
    // =========================

    private void configurarCompras() {

        comprasListView.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(
                    Compra compra,
                    boolean empty) {

                super.updateItem(compra, empty);

                if (empty || compra == null) {

                    setText(null);

                } else {

                    setText(
                            compra.getIdCompra()
                                    + " | "
                                    + compra.getEvento().getNombre()
                                    + " | $"
                                    + compra.getTotal()
                                    + " | "
                                    + compra.getEstado()
                    );
                }
            }
        });
    }

    private void cargarCompras() {

        comprasListView.getItems().clear();

        comprasListView.getItems()
                .addAll(compraRepository.listarCompras());
    }

    // =========================
    // INCIDENCIAS
    // =========================

    private void configurarIncidencias() {

        incidenciasListView.setCellFactory(param -> new ListCell<>() {

            @Override
            protected void updateItem(
                    Incidencia incidencia,
                    boolean empty) {

                super.updateItem(incidencia, empty);

                if (empty || incidencia == null) {

                    setText(null);

                } else {

                    setText(
                            incidencia.getTipo()
                                    + " | "
                                    + incidencia.getDescripcion()
                    );
                }
            }
        });
    }

    private void cargarIncidencias() {

        incidenciasListView.getItems().clear();

        incidenciasListView.getItems()
                .addAll(incidenciaRepository.listarIncidencias());
    }

    // =========================
    // MÉTRICAS
    // =========================

    private void cargarMetricas() {

        int compras =
                compraRepository.listarCompras().size();

        int incidencias =
                incidenciaRepository.listarIncidencias().size();

        int eventos =
                eventoRepository.listarEventos().size();

        ventasChart.setData(
                FXCollections.observableArrayList(

                        new PieChart.Data(
                                "Compras",
                                compras
                        ),

                        new PieChart.Data(
                                "Eventos",
                                eventos
                        ),

                        new PieChart.Data(
                                "Incidencias",
                                incidencias
                        )
                )
        );
    }

    // =========================
    // ALERTAS
    // =========================

    private void showAlert(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}