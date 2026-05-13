package com.example.inmobiliaria.modelos;

import com.example.inmobiliaria.util.TipoErrorValidacion;

import java.util.Objects;

public class Propietario {
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String dni;
    private String telefono;

    public Propietario() { }

    public Propietario(int id, String nombre, String apellido, String dni, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.telefono = telefono;
    }

    public Propietario(String nombre, String apellido, String dni, String telefono, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.dni = dni;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Propietario that = (Propietario) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * Valida el nombre ingresado
     * @param nombre Nombre a validar
     * @return TipoErrorValidacion: OK si el nombre es válido, VACIO si el nombre está vacío, LONGITUD_MAXIMA si el nombre es muy largo
     */
    public static TipoErrorValidacion validarNombre(String nombre){
        if (nombre == null || nombre.isEmpty())
            return TipoErrorValidacion.VACIO;
        if (nombre.length() > 50)
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida el apellido ingresado
     * @param apellido Apellido a validar
     * @return TipoErrorValidacion: OK si el apellido es válido, VACIO si el apellido está vacío, LONGITUD_MAXIMA si el apellido es muy largo
     */
    public static TipoErrorValidacion validarApellido(String apellido){
        if (apellido == null || apellido.isEmpty())
            return TipoErrorValidacion.VACIO;
        if (apellido.length() > 50)
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida el DNI ingresado
     * @param dni DNI a validar
     * @return TipoErrorValidacion: OK si el DNI es válido, VACIO si el DNI está vacío, NO_ES_NUMERO si el DNI no es un número, LONGITUD_INCORRECTA si el DNI tiene una longitud incorrecta
     */
    public static TipoErrorValidacion validarDni(String dni){
        if (dni == null || dni.isEmpty())
            return TipoErrorValidacion.VACIO;
        try {
            Integer.parseInt(dni);
        } catch (NumberFormatException e) {
            return TipoErrorValidacion.NO_ES_NUMERO;
        }
        if (dni.length() < 7 || dni.length() > 8)
            return TipoErrorValidacion.LONGITUD_INCORRECTA;
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida el teléfono ingresado
     * @param telefono Teléfono a validar
     * @return TipoErrorValidacion: OK si el teléfono es válido, VACIO si el teléfono está vacío, LONGITUD_MAXIMA si el teléfono es muy largo
     */
    public static TipoErrorValidacion validarTelefono(String telefono){
        if (telefono == null || telefono.isEmpty())
            return TipoErrorValidacion.VACIO;
        if (telefono.length() > 25)
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        return TipoErrorValidacion.OK;
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
}
