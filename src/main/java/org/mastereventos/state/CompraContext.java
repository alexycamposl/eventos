package org.mastereventos.state;

public class CompraContext {

    private EstadoCompra estado;

    public CompraContext() {
        estado = new CompraCreada();
    }

    public void setEstado(EstadoCompra estado) {
        this.estado = estado;
    }

    public void mostrarEstado() {
        estado.manejarEstado();
    }

    public String getEstadoActual() {
        return estado.getNombreEstado();
    }
}