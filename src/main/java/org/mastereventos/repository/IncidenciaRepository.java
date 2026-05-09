package org.mastereventos.repository;

import org.mastereventos.model.Incidencia;
import org.mastereventos.singleton.DataStore;

import java.util.ArrayList;
import java.util.List;

public class IncidenciaRepository {

    private final List<Incidencia> incidencias = DataStore.getInstancia().getIncidencias();

    public void guardar(Incidencia incidencia) {
        incidencias.add(incidencia);
    }

    public List<Incidencia> listarTodas() {
        return new ArrayList<>(incidencias);
    }

    public List<Incidencia> filtrarPorTipo(String tipo) {
        List<Incidencia> resultado = new ArrayList<>();
        for (Incidencia i : incidencias) {
            boolean coincide = tipo == null || tipo.isBlank()
                    || "Todos".equalsIgnoreCase(tipo)
                    || i.getTipo().equalsIgnoreCase(tipo);
            if (coincide) resultado.add(i);
        }
        return resultado;
    }

    public List<Incidencia> filtrarPorFecha(String fechaInicio) {
        List<Incidencia> resultado = new ArrayList<>();
        for (Incidencia i : incidencias) {
            if (fechaInicio == null || fechaInicio.isBlank()
                    || i.getFecha().compareTo(fechaInicio) >= 0) {
                resultado.add(i);
            }
        }
        return resultado;
    }

    public String generarNuevoId() {
        return "INC" + String.format("%03d", incidencias.size() + 1);
    }
}
