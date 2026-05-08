package com.example.inmobiliaria.modelos;

import java.util.Objects;

public class Propietario extends Login {
    private int id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;

    public Propietario() { }

    public Propietario(int id, String nombre, String apellido, String dni, String telefono, String email) {
        super(email, null);
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    public Propietario(String nombre, String apellido, String dni, String telefono, String email) {
        super(email, null);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    public Propietario(String nombre, String apellido, String dni, String telefono, String email, String clave) {
        super(email, clave);
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.telefono = telefono;
    }

    public Propietario(int id, String nombre, String apellido, String dni, String telefono, String email, String clave) {
        super(email, clave);
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
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
}
