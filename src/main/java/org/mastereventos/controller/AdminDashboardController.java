package org.mastereventos.controller;

import org.mastereventos.model.Evento;
import org.mastereventos.repository.EventoRepository;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class AdminDashboardController {

    @FXML
    private ListView<Evento> eventosAdminListView;

    @FXML
    private TextArea detalleEventoAdminArea;

    private final EventoRepository eventoRepository = new EventoRepository();

    @FXML
    private void initialize() {
        cargarEventos();

        eventosAdminListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> mostrarDetalleEvento(seleccionado));
    }

    private void cargarEventos() {
        eventosAdminListView.getItems().clear();
        eventosAdminListView.getItems().addAll(eventoRepository.getEventos());
    }

    private void mostrarDetalleEvento(Evento evento) {
        if (evento == null) {
            detalleEventoAdminArea.clear();
            return;
        }

        String detalle = "ID: " + evento.getIdEvento() + "\n"
                + "Nombre: " + evento.getNombre() + "\n"
                + "Categoria: " + evento.getCategoria() + "\n"
                + "Descripcion: " + evento.getDescripcion() + "\n"
                + "Ciudad: " + evento.getCiudad() + "\n"
                + "Fecha: " + evento.getFecha() + "\n"
                + "Estado: " + evento.getEstado();

        detalleEventoAdminArea.setText(detalle);
    }
}
