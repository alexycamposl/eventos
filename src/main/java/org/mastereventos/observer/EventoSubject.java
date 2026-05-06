package org.mastereventos.observer;

import java.util.ArrayList;
import java.util.List;

public class EventoSubject implements Subject {

    private List<Observer> observers = new ArrayList<>();

    @Override
    public void agregarObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void eliminarObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notificarObservers(String mensaje) {
        for (Observer observer : observers) {
            observer.actualizar(mensaje);
        }
    }
}