package com.example.inmobiliaria.modelos;

import com.example.inmobiliaria.util.TipoErrorValidacion;

import java.io.Serializable;

public class CambioContrasenia implements Serializable {
    private String claveActual;
    private String claveNueva;

    public CambioContrasenia(String claveActual, String claveNueva) {
        this.claveActual = claveActual;
        this.claveNueva = claveNueva;
    }

    public String getClaveNueva() {
        return claveNueva;
    }

    public void setClaveNueva(String claveNueva) {
        this.claveNueva = claveNueva;
    }

    public String getClaveActual() {
        return claveActual;
    }

    public void setClaveActual(String claveActual) {
        this.claveActual = claveActual;
    }

    /**
     * Valida la clave actual ingresada
     * @param claveActual Clave actual a validar
     * @return TipoErrorValidacion: OK si la clave es válida, VACIO si la clave está vacía
     */
    public static TipoErrorValidacion validarClaveActual(String claveActual) {
        if (claveActual == null || claveActual.isEmpty())
            return TipoErrorValidacion.VACIO;

        return TipoErrorValidacion.OK;
    }

    /**
     * Valida la clave nueva ingresada
     * @param claveNueva Clave nueva a validar
     * @return TipoErrorValidacion: OK si la clave es válida, VACIO si la clave está vacía
     */
    public static TipoErrorValidacion validarClaveNueva(String claveNueva) {
        if (claveNueva == null || claveNueva.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }

        return TipoErrorValidacion.OK;
    }
}
