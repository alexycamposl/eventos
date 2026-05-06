package org.mastereventos.observer;

public class NotificacionUsuario implements Observer {

    private String nombreUsuario;

    public NotificacionUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public void actualizar(String mensaje) {
        System.out.println("Notificación para " + nombreUsuario + ": " + mensaje);
    }
}