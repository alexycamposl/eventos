package org.mastereventos.model;

public class Evento {

    private String idEvento;
    private String nombre;
    private String categoria;
    private String descripcion;
    private String ciudad;
    private String fecha;
    private String estado;
    private String recinto;
    private int aforo;
    private String politicaCancelacion;
    private String politicaReembolso;

    public Evento(String idEvento, String nombre, String categoria,
                  String descripcion, String ciudad,
                  String fecha, String estado) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fecha = fecha;
        this.estado = estado;
        this.recinto = "Por definir";
        this.aforo = 0;
        this.politicaCancelacion = "Cancelacion hasta 48h antes del evento.";
        this.politicaReembolso = "Reembolso del 80% con 72h de anticipacion.";
    }

    public Evento(String idEvento, String nombre, String categoria,
                  String descripcion, String ciudad, String fecha, String estado,
                  String recinto, int aforo,
                  String politicaCancelacion, String politicaReembolso) {
        this(idEvento, nombre, categoria, descripcion, ciudad, fecha, estado);
        this.recinto = recinto;
        this.aforo = aforo;
        this.politicaCancelacion = politicaCancelacion;
        this.politicaReembolso = politicaReembolso;
    }

    public String getIdEvento() { return idEvento; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public String getDescripcion() { return descripcion; }
    public String getCiudad() { return ciudad; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public String getRecinto() { return recinto; }
    public int getAforo() { return aforo; }
    public String getPoliticaCancelacion() { return politicaCancelacion; }
    public String getPoliticaReembolso() { return politicaReembolso; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setRecinto(String recinto) { this.recinto = recinto; }
    public void setAforo(int aforo) { this.aforo = aforo; }
    public void setPoliticaCancelacion(String p) { this.politicaCancelacion = p; }
    public void setPoliticaReembolso(String p) { this.politicaReembolso = p; }

    @Override
    public String toString() {
        return nombre + " | " + categoria + " | " + ciudad + " | " + fecha + " | " + estado;
    }
}
