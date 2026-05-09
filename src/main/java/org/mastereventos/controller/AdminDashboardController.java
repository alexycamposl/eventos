package org.mastereventos.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.mastereventos.model.Compra;
import org.mastereventos.model.EstadoEvento;
import org.mastereventos.model.Evento;
import org.mastereventos.model.Incidencia;
import org.mastereventos.repository.CompraRepository;
import org.mastereventos.repository.EventoRepository;
import org.mastereventos.repository.IncidenciaRepository;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

// actualizacion admin alex

public class AdminDashboardController {

    @FXML
    private ListView<Evento> eventosListView;

    @FXML
    private ListView<Compra> comprasListView;

    @FXML
    private ListView<Incidencia> incidenciasListView;

    @FXML
    private PieChart ventasChart;

    @FXML
    private TextField nombreEventoField;

    @FXML
    private TextField categoriaEventoField;

    @FXML
    private TextField ciudadEventoField;

    @FXML
    private TextField fechaEventoField;

    @FXML
    private TextArea descripcionEventoArea;

    private final EventoRepository eventoRepository =
            EventoRepository.getInstancia();

    private final CompraRepository compraRepository =
            CompraRepository.getInstancia();

    private final IncidenciaRepository incidenciaRepository =
            IncidenciaRepository.getInstancia();

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
                    setOnMouseClicked(event -> {

                        Evento seleccionado =
                                getItem();

                        if (seleccionado != null) {

                            nombreEventoField.setText(
                                    seleccionado.getNombre()
                            );

                            categoriaEventoField.setText(
                                    seleccionado.getCategoria()
                            );

                            ciudadEventoField.setText(
                                    seleccionado.getCiudad()
                            );

                            fechaEventoField.setText(
                                    seleccionado.getFecha()
                            );

                            descripcionEventoArea.setText(
                                    seleccionado.getDescripcion()
                            );
                        }
                    });
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

    @FXML
    private void handleCrearEvento() {

        String nombre =
                nombreEventoField.getText();

        String categoria =
                categoriaEventoField.getText();

        String ciudad =
                ciudadEventoField.getText();

        String fecha =
                fechaEventoField.getText();

        String descripcion =
                descripcionEventoArea.getText();

        if (
                nombre.isBlank()
                        ||
                        categoria.isBlank()
                        ||
                        ciudad.isBlank()
                        ||
                        fecha.isBlank()
                        ||
                        descripcion.isBlank()
        ) {

            showAlert(
                    "Error",
                    "Completa todos los campos."
            );

            return;
        }

        Evento nuevoEvento =
                new Evento(
                        "E" + (eventoRepository.listarEventos().size() + 1),
                        nombre,
                        categoria,
                        descripcion,
                        ciudad,
                        fecha,
                        EstadoEvento.PUBLICADO
                );

        eventoRepository
                .listarEventos()
                .add(nuevoEvento);

        cargarEventos();

        limpiarFormularioEvento();

        showAlert(
                "Evento creado",
                "El evento fue creado correctamente."
        );
    }

    private void limpiarFormularioEvento() {

        nombreEventoField.clear();

        categoriaEventoField.clear();

        ciudadEventoField.clear();

        fechaEventoField.clear();

        descripcionEventoArea.clear();
    }

    @FXML
    private void handleEditarEvento() {

        Evento evento =
                eventosListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (evento == null) {

            showAlert(
                    "Error",
                    "Selecciona un evento."
            );

            return;
        }

        evento.setNombre(
                nombreEventoField.getText()
        );

        evento.setCategoria(
                categoriaEventoField.getText()
        );

        evento.setCiudad(
                ciudadEventoField.getText()
        );

        evento.setFecha(
                fechaEventoField.getText()
        );

        evento.setDescripcion(
                descripcionEventoArea.getText()
        );

        eventosListView.refresh();

        showAlert(
                "Evento actualizado",
                "Los cambios fueron guardados."
        );
    }

    @FXML
    private void handleEliminarEvento() {

        Evento evento =
                eventosListView
                        .getSelectionModel()
                        .getSelectedItem();

        if (evento == null) {

            showAlert(
                    "Error",
                    "Selecciona un evento."
            );

            return;
        }

        eventoRepository
                .listarEventos()
                .remove(evento);

        cargarEventos();

        limpiarFormularioEvento();

        showAlert(
                "Evento eliminado",
                "El evento fue eliminado correctamente."
        );
    }

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
    @FXML
    private void handleCerrarSesion() {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/org/mastereventos/ui/LoginView.fxml"
                            )
                    );

            Scene scene =
                    new Scene(loader.load(), 600, 400);

            Stage stage =
                    (Stage) eventosListView
                            .getScene()
                            .getWindow();

            stage.setScene(scene);

            stage.setTitle("MasterEventos - Login");

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            showAlert(
                    "Error",
                    "No se pudo cerrar sesión."
            );
        }
    }
}