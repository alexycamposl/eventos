package org.mastereventos.observer;

public interface Subject {
    void agregarObserver(Observer observer);
    void eliminarObserver(Observer observer);
    void notificarObservers(String mensaje);
}