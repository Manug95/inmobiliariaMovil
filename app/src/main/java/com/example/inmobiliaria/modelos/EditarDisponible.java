package com.example.inmobiliaria.modelos;

public class EditarDisponible {
    private boolean estado;

    public EditarDisponible() {}

    public EditarDisponible(boolean estado) {
        this.estado = estado;
    }
    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
