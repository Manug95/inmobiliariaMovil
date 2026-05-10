package com.example.inmobiliaria.util;

public class MutableSingleEvent<T> {
    private final T contenido;
    private boolean contenidoUsado = false;

    public MutableSingleEvent(T contenido) {
        this.contenido = contenido;
    }

    public T getContenido() {
        if (contenidoUsado) return null;
        contenidoUsado = true;
        return contenido;
    }
}
