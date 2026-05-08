package org.mastereventos.repository;

import org.mastereventos.model.Incidencia;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IncidenciaRepository {

    private static final List<Incidencia> incidencias =
            new ArrayList<>();

    // REGISTRAR

    public void registrarIncidencia(
            Incidencia incidencia) {

        incidencias.add(incidencia);
    }

    // LISTAR TODAS

    public List<Incidencia> listarIncidencias() {
        return incidencias;
    }

    // FILTRAR POR TIPO

    public List<Incidencia> buscarPorTipo(
            String tipo) {

        List<Incidencia> resultado =
                new ArrayList<>();

        for (Incidencia incidencia : incidencias) {

            if (
                    incidencia.getTipo()
                            .equalsIgnoreCase(tipo)
            ) {

                resultado.add(incidencia);
            }
        }

        return resultado;
    }

    // FILTRAR POR FECHA

    public List<Incidencia> buscarPorFecha(
            LocalDateTime inicio,
            LocalDateTime fin) {

        List<Incidencia> resultado =
                new ArrayList<>();

        for (Incidencia incidencia : incidencias) {

            if (
                    incidencia.getFecha().isAfter(inicio)
                            &&
                            incidencia.getFecha().isBefore(fin)
            ) {

                resultado.add(incidencia);
            }
        }

        return resultado;
    }
}