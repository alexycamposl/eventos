package org.mastereventos.repository;

import org.mastereventos.model.Asiento;
import org.mastereventos.model.Zona;
import org.mastereventos.singleton.DataStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZonaRepository {

    private final Map<String, List<Zona>> zonasPorEvento =
            DataStore.getInstancia().getZonasPorEvento();

    public List<Zona> listarZonasPorEvento(String idEvento) {
        return zonasPorEvento.getOrDefault(idEvento, new ArrayList<>());
    }

    public List<Asiento> listarAsientosDisponibles(Zona zona) {
        List<Asiento> disponibles = new ArrayList<>();
        if (zona == null) return disponibles;
        for (Asiento a : zona.getAsientos()) {
            if ("Disponible".equalsIgnoreCase(a.getEstado())) disponibles.add(a);
        }
        return disponibles;
    }

    public List<Asiento> listarTodosAsientos(Zona zona) {
        return zona != null ? zona.getAsientos() : new ArrayList<>();
    }

    public void agregarZona(String idEvento, Zona zona) {
        zonasPorEvento.computeIfAbsent(idEvento, k -> new ArrayList<>()).add(zona);
    }

    public void eliminarZona(String idEvento, String idZona) {
        List<Zona> zonas = zonasPorEvento.get(idEvento);
        if (zonas != null) zonas.removeIf(z -> z.getIdZona().equals(idZona));
    }

    public Zona buscarZona(String idEvento, String idZona) {
        for (Zona z : listarZonasPorEvento(idEvento)) {
            if (z.getIdZona().equals(idZona)) return z;
        }
        return null;
    }

    public void cambiarEstadoAsiento(Zona zona, String idAsiento, String nuevoEstado) {
        if (zona == null) return;
        for (Asiento a : zona.getAsientos()) {
            if (a.getIdAsiento().equals(idAsiento)) {
                a.setEstado(nuevoEstado);
                return;
            }
        }
    }

    public String generarNuevoIdZona() {
        int max = 0;
        for (List<Zona> zonas : zonasPorEvento.values()) {
            for (Zona z : zonas) {
                try {
                    int n = Integer.parseInt(z.getIdZona().replace("Z", ""));
                    if (n > max) max = n;
                } catch (NumberFormatException ignored) {}
            }
        }
        return "Z" + String.format("%03d", max + 1);
    }

    public String generarNuevoIdAsiento(Zona zona) {
        int max = 0;
        if (zona != null) {
            for (Asiento a : zona.getAsientos()) {
                try {
                    int n = Integer.parseInt(a.getIdAsiento().replace("A", ""));
                    if (n > max) max = n;
                } catch (NumberFormatException ignored) {}
            }
        }
        // Also check global max
        for (List<Zona> zonas : zonasPorEvento.values()) {
            for (Zona z : zonas) {
                for (Asiento a : z.getAsientos()) {
                    try {
                        int n = Integer.parseInt(a.getIdAsiento().replace("A", ""));
                        if (n > max) max = n;
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return "A" + String.format("%03d", max + 1);
    }
}
