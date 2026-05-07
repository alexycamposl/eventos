package org.mastereventos.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mastereventos.model.Asiento;
import org.mastereventos.model.EstadoAsiento;
import org.mastereventos.model.Zona;

public class ZonaRepository {

    private final Map<String, List<Zona>> zonasPorEvento;

    public ZonaRepository() {
        zonasPorEvento = new HashMap<>();
        inicializarDatos();
    }

    private void inicializarDatos() {

        // EVENTO 1
        List<Zona> zonasE001 = new ArrayList<>();

        Zona vip = new Zona("Z001", "VIP", 50, 250000);

        Asiento a1 = new Asiento("A001", "A", 1);
        Asiento a2 = new Asiento("A002", "A", 2);
        Asiento a3 = new Asiento("A003", "A", 3);

        a3.setEstado(EstadoAsiento.BLOQUEADO);

        vip.agregarAsiento(a1);
        vip.agregarAsiento(a2);
        vip.agregarAsiento(a3);

        Zona general = new Zona("Z002", "General", 200, 90000);

        general.agregarAsiento(new Asiento("A004", "B", 1));
        general.agregarAsiento(new Asiento("A005", "B", 2));

        zonasE001.add(vip);
        zonasE001.add(general);

        // EVENTO 2
        List<Zona> zonasE002 = new ArrayList<>();

        Zona preferencial = new Zona("Z003", "Preferencial", 80, 120000);

        preferencial.agregarAsiento(new Asiento("A006", "C", 1));
        preferencial.agregarAsiento(new Asiento("A007", "C", 2));

        Zona platea = new Zona("Z004", "Platea", 150, 70000);

        Asiento a8 = new Asiento("A008", "D", 1);
        Asiento a9 = new Asiento("A009", "D", 2);

        a9.setEstado(EstadoAsiento.RESERVADO);

        platea.agregarAsiento(a8);
        platea.agregarAsiento(a9);

        zonasE002.add(preferencial);
        zonasE002.add(platea);

        // EVENTO 3
        List<Zona> zonasE003 = new ArrayList<>();

        Zona auditorio = new Zona("Z005", "Auditorio", 100, 60000);

        auditorio.agregarAsiento(new Asiento("A010", "E", 1));
        auditorio.agregarAsiento(new Asiento("A011", "E", 2));

        zonasE003.add(auditorio);

        // MAPA FINAL

        zonasPorEvento.put("E001", zonasE001);
        zonasPorEvento.put("E002", zonasE002);
        zonasPorEvento.put("E003", zonasE003);
    }

    public List<Zona> listarZonasPorEvento(String idEvento) {
        return zonasPorEvento.getOrDefault(idEvento, new ArrayList<>());
    }

    public List<Asiento> listarAsientosDisponibles(Zona zona) {

        List<Asiento> disponibles = new ArrayList<>();

        if (zona == null) {
            return disponibles;
        }

        for (Asiento asiento : zona.getAsientos()) {

            if (asiento.getEstado() == EstadoAsiento.DISPONIBLE) {
                disponibles.add(asiento);
            }
        }

        return disponibles;
    }
}