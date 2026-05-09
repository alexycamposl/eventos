package org.mastereventos.singleton;

import org.mastereventos.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    private static DataStore instancia;

    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Evento> eventos = new ArrayList<>();
    private final List<Compra> compras = new ArrayList<>();
    private final List<Incidencia> incidencias = new ArrayList<>();
    private final Map<String, List<Zona>> zonasPorEvento = new HashMap<>();

    private DataStore() {
        inicializarUsuarios();
        inicializarEventos();
        inicializarZonas();
        inicializarCompras();
    }

    public static DataStore getInstancia() {
        if (instancia == null) {
            instancia = new DataStore();
        }
        return instancia;
    }

    private void inicializarUsuarios() {
        usuarios.add(new Usuario("U001", "Administrador",
                "admin@mastereventos.com", "3000000000", "1234", Rol.ADMIN));
        usuarios.add(new Usuario("U002", "Juan Garcia",
                "user@mastereventos.com", "3111111111", "1234", Rol.USUARIO));
        usuarios.add(new Usuario("U003", "Maria Lopez",
                "maria@email.com", "3222222222", "1234", Rol.USUARIO));
    }

    private void inicializarEventos() {
        eventos.add(new Evento("E001", "Concierto Rock Nacional", "Concierto",
                "Evento musical con las mejores bandas de rock del pais.",
                "Bogota", "2026-06-15 20:00", "Publicado",
                "Estadio El Campin", 250,
                "Cancelacion hasta 48h antes.", "Reembolso 80% con 72h."));

        eventos.add(new Evento("E002", "Obra de Teatro Clasico", "Teatro",
                "Presentacion teatral Hamlet para publico general.",
                "Medellin", "2026-07-10 19:00", "Publicado",
                "Teatro Metropolitano", 80,
                "No hay cancelacion.", "Sin reembolso."));

        eventos.add(new Evento("E003", "Conferencia de Tecnologia", "Conferencia",
                "Charlas sobre innovacion, software e inteligencia artificial.",
                "Cali", "2026-08-05 09:00", "Publicado",
                "Centro de Convenciones", 100,
                "Cancelacion hasta 24h antes.", "Reembolso 50%."));

        eventos.add(new Evento("E004", "Festival de Jazz", "Concierto",
                "Festival anual de jazz con artistas nacionales e internacionales.",
                "Bogota", "2026-09-20 18:00", "Pausado",
                "Parque Simon Bolivar", 500,
                "Cancelacion hasta 72h antes.", "Reembolso 100% con 1 semana."));

        eventos.add(new Evento("E005", "Concierto Pop Latina", "Concierto",
                "Los mejores exitos del pop en espanol.",
                "Medellin", "2026-10-12 21:00", "Borrador",
                "Estadio Atanasio Girardot", 300,
                "Por definir.", "Por definir."));
    }

    private void inicializarZonas() {
        List<Zona> zonasE001 = new ArrayList<>();
        Zona vipE001 = new Zona("Z001", "VIP", 50, 250000);
        vipE001.agregarAsiento(new Asiento("A001", "A", 1, "Disponible"));
        vipE001.agregarAsiento(new Asiento("A002", "A", 2, "Disponible"));
        vipE001.agregarAsiento(new Asiento("A003", "A", 3, "Bloqueado"));
        vipE001.agregarAsiento(new Asiento("A004", "A", 4, "Vendido"));

        Zona generalE001 = new Zona("Z002", "General", 200, 90000);
        generalE001.agregarAsiento(new Asiento("A005", "B", 1, "Disponible"));
        generalE001.agregarAsiento(new Asiento("A006", "B", 2, "Disponible"));
        generalE001.agregarAsiento(new Asiento("A007", "B", 3, "Vendido"));

        Zona preferencialE001 = new Zona("Z003", "Preferencial", 80, 150000);
        preferencialE001.agregarAsiento(new Asiento("A008", "C", 1, "Disponible"));
        preferencialE001.agregarAsiento(new Asiento("A009", "C", 2, "Reservado"));

        zonasE001.add(vipE001);
        zonasE001.add(generalE001);
        zonasE001.add(preferencialE001);
        zonasPorEvento.put("E001", zonasE001);

        List<Zona> zonasE002 = new ArrayList<>();
        Zona plateaE002 = new Zona("Z004", "Platea", 80, 120000);
        plateaE002.agregarAsiento(new Asiento("A010", "D", 1, "Disponible"));
        plateaE002.agregarAsiento(new Asiento("A011", "D", 2, "Disponible"));
        plateaE002.agregarAsiento(new Asiento("A012", "D", 3, "Vendido"));

        Zona balconE002 = new Zona("Z005", "Balcon", 50, 70000);
        balconE002.agregarAsiento(new Asiento("A013", "E", 1, "Disponible"));
        balconE002.agregarAsiento(new Asiento("A014", "E", 2, "Disponible"));

        zonasE002.add(plateaE002);
        zonasE002.add(balconE002);
        zonasPorEvento.put("E002", zonasE002);

        List<Zona> zonasE003 = new ArrayList<>();
        Zona auditorioE003 = new Zona("Z006", "Auditorio", 100, 60000);
        auditorioE003.agregarAsiento(new Asiento("A015", "F", 1, "Disponible"));
        auditorioE003.agregarAsiento(new Asiento("A016", "F", 2, "Vendido"));
        auditorioE003.agregarAsiento(new Asiento("A017", "F", 3, "Disponible"));

        zonasE003.add(auditorioE003);
        zonasPorEvento.put("E003", zonasE003);

        List<Zona> zonasE004 = new ArrayList<>();
        Zona generalE004 = new Zona("Z007", "General", 500, 80000);
        generalE004.agregarAsiento(new Asiento("A018", "G", 1, "Disponible"));
        generalE004.agregarAsiento(new Asiento("A019", "G", 2, "Disponible"));
        generalE004.agregarAsiento(new Asiento("A020", "G", 3, "Disponible"));

        zonasE004.add(generalE004);
        zonasPorEvento.put("E004", zonasE004);
    }

    private void inicializarCompras() {
        Usuario user2 = usuarios.get(1);
        Usuario user3 = usuarios.get(2);
        Evento e001 = eventos.get(0);
        Evento e002 = eventos.get(1);
        Evento e003 = eventos.get(2);

        List<Entrada> ents1 = new ArrayList<>();
        ents1.add(new Entrada("ENT001",
                zonasPorEvento.get("E001").get(0),
                zonasPorEvento.get("E001").get(0).getAsientos().get(3),
                400000, "Activa"));
        List<String> serv1 = new ArrayList<>();
        serv1.add("VIP");
        compras.add(new Compra("COM001", user2, e001, ents1, 400000,
                "Pagada", "2026-05-01", serv1));

        List<Entrada> ents2 = new ArrayList<>();
        ents2.add(new Entrada("ENT002",
                zonasPorEvento.get("E002").get(0),
                zonasPorEvento.get("E002").get(0).getAsientos().get(2),
                120000, "Activa"));
        compras.add(new Compra("COM002", user2, e002, ents2, 120000,
                "Confirmada", "2026-05-03", new ArrayList<>()));

        List<Entrada> ents3 = new ArrayList<>();
        ents3.add(new Entrada("ENT003",
                zonasPorEvento.get("E001").get(1),
                zonasPorEvento.get("E001").get(1).getAsientos().get(2),
                90000, "Anulada"));
        compras.add(new Compra("COM003", user3, e001, ents3, 90000,
                "Cancelada", "2026-05-05", new ArrayList<>()));

        List<Entrada> ents4 = new ArrayList<>();
        ents4.add(new Entrada("ENT004",
                zonasPorEvento.get("E003").get(0),
                zonasPorEvento.get("E003").get(0).getAsientos().get(1),
                60000, "Activa"));
        List<String> serv4 = new ArrayList<>();
        serv4.add("Seguro");
        compras.add(new Compra("COM004", user3, e003, ents4, 80000,
                "Pagada", "2026-05-07", serv4));

        List<Entrada> ents5 = new ArrayList<>();
        ents5.add(new Entrada("ENT005",
                zonasPorEvento.get("E001").get(2),
                zonasPorEvento.get("E001").get(2).getAsientos().get(1),
                150000, "Activa"));
        List<String> serv5 = new ArrayList<>();
        serv5.add("Merchandising");
        compras.add(new Compra("COM005", user3, e001, ents5, 170000,
                "Confirmada", "2026-05-08", serv5));
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Evento> getEventos() { return eventos; }
    public List<Compra> getCompras() { return compras; }
    public List<Incidencia> getIncidencias() { return incidencias; }
    public Map<String, List<Zona>> getZonasPorEvento() { return zonasPorEvento; }
}
