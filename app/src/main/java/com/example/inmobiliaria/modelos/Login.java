package com.example.inmobiliaria.modelos;

import com.example.inmobiliaria.util.TipoErrorValidacion;

import java.io.Serializable;

public class Login implements Serializable {
    private String email;
    private String clave;

    public Login() {}

    public Login(String email, String clave) {
        this.email = email;
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Valida el email ingresado
     * @param email Email a validar
     * @return TipoErrorValidacion: OK si el email es válido, VACIO si el email está vacío, LONGITUD_MAXIMA si el email es muy largo
     */
    public static TipoErrorValidacion validarEmail(String email){
        if (email == null || email.isEmpty())
            return TipoErrorValidacion.VACIO;
        if (email.length() > 100)
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida la clave ingresada
     * @param clave Clave a validar
     * @return TipoErrorValidacion: OK si la clave es válida, CAMPO_VACIO si la clave está vacía, LONGITUD_MINIMA si la clave es muy corta
     */
    public static TipoErrorValidacion validarClave(String clave) {
        if (clave == null || clave.isEmpty())
            return TipoErrorValidacion.VACIO;
        /*if (clave.length() < 8)
            return TipoErrorValidacion.LONGITUD_MINIMA;*/
        return TipoErrorValidacion.OK;
    }
}