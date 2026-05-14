package com.example.inmobiliaria.modelos;

import com.example.inmobiliaria.util.TipoErrorValidacion;

import java.io.Serializable;
import java.util.Objects;

public class Inmueble implements Serializable {
    private int id;
    private int idPropietario;
    private int idTipoInmueble;
    private int cantidadAmbientes;
    private String uso;
    private String calle;
    private int nroCalle;
    private double precio;
    private double latitud;
    private double longitud;
    private boolean disponible;
    private String foto;
    private Propietario duenio;
    private String tipoInmueble;
    //private TipoInmueble tipoInmuebleObj;

    public Inmueble() { }

    public Inmueble(int id, int idPropietario, int idTipoInmueble, String tipoInmueble, int cantidadAmbientes, String uso, String calle, int nroCalle, double precio, double latitud, double longitud, boolean disponible) {
        this.id = id;
        this.idPropietario = idPropietario;
        this.idTipoInmueble = idTipoInmueble;
        this.cantidadAmbientes = cantidadAmbientes;
        this.uso = uso;
        this.calle = calle;
        this.nroCalle = nroCalle;
        this.precio = precio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.disponible = disponible;
        this.tipoInmueble = tipoInmueble;
    }

    public Inmueble(int idPropietario, int idTipoInmueble, String tipoInmueble, int cantidadAmbientes, String uso, String calle, int nroCalle, double precio, double latitud, double longitud, boolean disponible) {
        this.idPropietario = idPropietario;
        this.idTipoInmueble = idTipoInmueble;
        this.cantidadAmbientes = cantidadAmbientes;
        this.uso = uso;
        this.calle = calle;
        this.nroCalle = nroCalle;
        this.precio = precio;
        this.latitud = latitud;
        this.longitud = longitud;
        this.disponible = disponible;
        this.tipoInmueble = tipoInmueble;
    }

    public Inmueble(String calle, int nroCalle, int cantidadAmbientes, String tipoInmueble, String uso, double precio, boolean disponible) {
        this.cantidadAmbientes = cantidadAmbientes;
        this.uso = uso;
        this.calle = calle;
        this.nroCalle = nroCalle;
        this.precio = precio;
        this.disponible = disponible;
        this.tipoInmueble = tipoInmueble;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public int getIdTipoInmueble() {
        return idTipoInmueble;
    }

    public void setIdTipoInmueble(int idTipoInmueble) {
        this.idTipoInmueble = idTipoInmueble;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public int getCantidadAmbientes() {
        return cantidadAmbientes;
    }

    public void setCantidadAmbientes(int cantidadAmbientes) {
        this.cantidadAmbientes = cantidadAmbientes;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNroCalle() {
        return nroCalle;
    }

    public void setNroCalle(int nroCalle) {
        this.nroCalle = nroCalle;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Propietario getDuenio() {
        return duenio;
    }

    public void setDuenio(Propietario duenio) {
        this.duenio = duenio;
    }

    /*public TipoInmueble getTipoInmuebleObj() {
        return tipoInmuebleObj;
    }

    public void setTipoInmuebleObj(TipoInmueble tipoInmuebleObj) {
        this.tipoInmuebleObj = tipoInmuebleObj;
    }*/

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inmueble inmueble = (Inmueble) o;
        return id == inmueble.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public String getDireccion() {
        return calle + " " + nroCalle;
    }

    /**
     * Valida si el precio es válido.
     * @param precioStr Precio a validar
     * @return OK si el precio es válido,
     * VACIO si el precio está vacío,
     * NO_ES_NUMERO si el precio no es un número válido,
     * VALOR_NO_POSITIVO si el precio es menor o igual a 0
     */
    public static TipoErrorValidacion validarPrecio(String precioStr) {
        if (precioStr.isBlank()) {
            return TipoErrorValidacion.VACIO;
        }
        double precio;
        try {
            precio = Double.parseDouble(precioStr);
            if (precio <= 0) {
                return TipoErrorValidacion.VALOR_NO_POSITIVO;
            }
        } catch (NumberFormatException e) {
            return TipoErrorValidacion.NO_ES_NUMERO;
        }
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si el uso es válido.
     * @param uso Uso a validar
     * @return OK si el uso es válido, LONGITUD_MAXIMA si el uso tiene más de 50 caracteres, VACIO si el uso está vacío
     */
    public static TipoErrorValidacion validarUso(String uso) {
        if (uso.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }
        if (uso.length() > 50) {
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        }
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si el tipo de inmueble es válido.
     * @param tipoInmueble Tipo de inmueble a validar
     * @return OK si el tipo de inmueble es válido, LONGITUD_MAXIMA si el tipo de inmueble tiene más de 50 caracteres, VACIO si el tipo de inmueble está vacío
     */
    public static TipoErrorValidacion validarTipoInmueble(String tipoInmueble) {
        if (tipoInmueble.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }
        if (tipoInmueble.length() > 50) {
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        }
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si la calle es válida.
     * @param calle Calle a validar
     * @return OK si la calle es válida, LONGITUD_MAXIMA si la calle tiene más de 100 caracteres, VACIO si la calle está vacío
     */
    public static TipoErrorValidacion validarCalle(String calle) {
        if (calle.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }
        if (calle.length() > 100) {
            return TipoErrorValidacion.LONGITUD_MAXIMA;
        }
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si el número de calle es válido.
     * @param nroCalleStr Número de calle a validar
     * @return OK si el número de calle es válido,
     * VACIO si el número de calle está vacío,
     * VALOR_NO_POSITIVO si el número de calle es menor o igual a 0,
     * NO_ES_NUMERO si el número de calle no es un número válido
     */
    public static TipoErrorValidacion validarNroCalle(String nroCalleStr) {
        if (nroCalleStr.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }
        int nroCalle;
        try {
            nroCalle = Integer.parseInt(nroCalleStr);
            if (nroCalle <= 0) {
                return TipoErrorValidacion.VALOR_NO_POSITIVO;
            }
        } catch (NumberFormatException e) {
            return TipoErrorValidacion.NO_ES_NUMERO;
        }
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si la latitud es válida.
     * @param latitudStr Latitud a validar
     * @return OK si la latitud es válida,
     * VACIO si la latitud está vacía,
     * RANGO_INCORRECTO si la latitud está fuera del rango -90 a 90,
     * NO_ES_NUMERO si la latitud no es un número válido
     */
    public static TipoErrorValidacion validarLatitud(String latitudStr) {
        /*if (latitudStr.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }*/
        double latitud;
        try {
            latitud = Double.parseDouble(latitudStr);
            if (latitud < -90 || latitud > 90) {
                return TipoErrorValidacion.RANGO_INCORRECTO;
            }
        } catch (NumberFormatException e) {
            return TipoErrorValidacion.NO_ES_NUMERO;
        }
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si la longitud es válida.
     * @param longitudStr Longitud a validar
     * @return OK si la longitud es válida,
     * VACIO si la longitud está vacía,
     * RANGO_INCORRECTO si la longitud está fuera del rango -180 a 180,
     * NO_ES_NUMERO si la longitud no es un número válido
     */
    public static TipoErrorValidacion validarLongitud(String longitudStr) {
        /*if (longitudStr.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }*/
        double longitud;
        try {
            longitud = Double.parseDouble(longitudStr);
            if (longitud < -180 || longitud > 180) {
                return TipoErrorValidacion.RANGO_INCORRECTO;
            }
        } catch (NumberFormatException e) {
            return TipoErrorValidacion.NO_ES_NUMERO;
        }
        return TipoErrorValidacion.OK;
    }

    public static TipoErrorValidacion validarDisponible(boolean disponible) {
        return TipoErrorValidacion.OK;
    }

    /**
     * Valida si la cantidad de ambientes es válida.
     * @param cantidadAmbientesStr Cantidad de ambientes a validar
     * @return OK si la cantidad de ambientes es válida,
     * VACIO si la cantidad de ambientes está vacía,
     * VALOR_NO_POSITIVO si la cantidad de ambientes es menor o igual a 0,
     * NO_ES_NUMERO si la cantidad de ambientes no es un número válido
     */
    public static TipoErrorValidacion validarCantidadAmbientes(String cantidadAmbientesStr) {
        if (cantidadAmbientesStr.isEmpty()) {
            return TipoErrorValidacion.VACIO;
        }
        int cantidadAmbientes;
        try {
            cantidadAmbientes = Integer.parseInt(cantidadAmbientesStr);
            if (cantidadAmbientes <= 0) {
                return TipoErrorValidacion.VALOR_NO_POSITIVO;
            }
        } catch (NumberFormatException e) {
            return TipoErrorValidacion.NO_ES_NUMERO;
        }
        return TipoErrorValidacion.OK;
    }
}
