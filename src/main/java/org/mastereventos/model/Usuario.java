package org.mastereventos.model;

import java.util.ArrayList;
import java.util.List;

public class Usuario {

    private String idUsuario;
    private String nombre;
    private String correo;
    private String telefono;
    private String password;
    private Rol rol;
    private List<String> metodosPago;

    public Usuario(String idUsuario, String nombre, String correo,
                   String telefono, String password, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
        this.rol = rol;
        this.metodosPago = new ArrayList<>();
        this.metodosPago.add("Tarjeta");
    }

    public String getIdUsuario() { return idUsuario; }
    public void setIdUsuario(String idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    public List<String> getMetodosPago() { return metodosPago; }

    public void agregarMetodoPago(String metodo) {
        if (!metodosPago.contains(metodo)) metodosPago.add(metodo);
    }

    public void eliminarMetodoPago(String metodo) {
        metodosPago.remove(metodo);
    }

    @Override
    public String toString() {
        return idUsuario + " | " + nombre + " | " + correo + " | " + rol;
    }
}
