package com.example.inmobiliaria.ui.perfil;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelos.Propietario;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;
import com.example.inmobiliaria.util.MutableSingleEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    private Propietario propietario;
    private final String EDITAR;
    private final String GUARDAR;
    private final MutableLiveData<Propietario> mPropietario;
    private final MutableLiveData<Boolean> mEstadoEditar;
    private final MutableLiveData<String> mTextoBoton,
            mErrorDni,
            mErrorNombre,
            mErrorApellido,
            mErrorEmail,
            mErrorTelefono;
    private final MutableLiveData<MutableSingleEvent<String>> mPerfilActualizado, mMensajeError;

    public PerfilViewModel(@NonNull Application application) {
        super(application);
        tokenService = TokenService.getInstancia(application);
        EDITAR = getApplication().getResources().getString(R.string.editar);
        GUARDAR = getApplication().getResources().getString(R.string.guardar);
        mPropietario = new MutableLiveData<>();
        mEstadoEditar = new MutableLiveData<>();
        mTextoBoton = new MutableLiveData<>();
        mErrorDni = new MutableLiveData<>();
        mErrorNombre = new MutableLiveData<>();
        mErrorApellido = new MutableLiveData<>();
        mErrorEmail = new MutableLiveData<>();
        mErrorTelefono = new MutableLiveData<>();
        mMensajeError = new MutableLiveData<>();
        mPerfilActualizado = new MutableLiveData<>();
    }

    public LiveData<String> getMErrorDni() {
        return mErrorDni;
    }
    public LiveData<Propietario> getMPropietario() {
        return mPropietario;
    }
    public LiveData<Boolean> getMEstadoEditar() {
        return mEstadoEditar;
    }
    public LiveData<String> getMTextoBoton() {
        return mTextoBoton;
    }
    public LiveData<String> getMErrorNombre() {
        return mErrorNombre;
    }
    public LiveData<String> getMErrorApellido() {
        return mErrorApellido;
    }
    public LiveData<String> getMErrorEmail() {
        return mErrorEmail;
    }
    public LiveData<String> getMErrorTelefono() {
        return mErrorTelefono;
    }
    public LiveData<MutableSingleEvent<String>> getMMensajeError() {
        return mMensajeError;
    }
    public LiveData<MutableSingleEvent<String>> getMPerfilActualizado() {
        return mPerfilActualizado;
    }

    public void editarPropietario(String btnText, String nombre, String apelldio, String dni, String telefono, String email) {
        if (btnText.equals(EDITAR)) {
            cambiarAGuardar();
        } else {
            if (esPropietarioValido(nombre, apelldio, dni, telefono, email)) {
                cambiarAEditar();
                if (mPropietario.getValue() != null)
                    actualizarPropietario(new Propietario(mPropietario.getValue().getId(), nombre, apelldio, dni, telefono, email));
            }
        }
    }

    public void obtenerPropietario() {
        if (propietario == null) {
            ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
            Call<Propietario> apiCall = api.getPropietario(tokenService.leerToken());

            apiCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> response) {
                    if (response.isSuccessful()) {
                        propietario = response.body();
                        if (propietario != null) {
                            mPropietario.postValue(propietario);
                        } else {
                            mMensajeError.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.no_se_pudo_obtener_el_perfil)));
                        }
                    } else {
                        mMensajeError.postValue(new MutableSingleEvent<>(ApiClient.obtenerMensajeError(response.errorBody())));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                    mMensajeError.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.err_al_obtener_perfil)));
                }
            });
        } else {
            mPropietario.setValue(propietario);
        }
    }

    private void actualizarPropietario(Propietario propietarioActualizado) {
        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();

        Call<Propietario> apiCall = api.putPropietario(tokenService.leerToken(), propietarioActualizado);

        apiCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> response) {
                if (response.isSuccessful()) {
                    propietario = response.body();
                    mPropietario.postValue(propietario);
                    mPerfilActualizado.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.perfil_actualizado)));
                } else {
                    mMensajeError.postValue(new MutableSingleEvent<>(ApiClient.obtenerMensajeError(response.errorBody())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                mMensajeError.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.err_al_actualizar_perfil)));
            }
        });
    }

    public void resetearDatosPropietario() {
        mPropietario.setValue(mPropietario.getValue());
    }

    public void resetearMensajesError() {
        mErrorDni.setValue("");
        mErrorNombre.setValue("");
        mErrorApellido.setValue("");
        mErrorTelefono.setValue("");
        mErrorEmail.setValue("");
    }

    public void cambiarAEditar() {
        mTextoBoton.setValue(EDITAR);
        mEstadoEditar.setValue(false);
    }

    private void cambiarAGuardar() {
        mTextoBoton.setValue(GUARDAR);
        mEstadoEditar.setValue(true);
    }

    private boolean esPropietarioValido(String nombre, String apelldio, String dni, String telefono, String email) {
        boolean esValido = validarNombre(nombre);
        esValido = validarApellido(apelldio) && esValido;
        esValido = validarDNI(dni) && esValido;
        esValido = validarTelefono(telefono) && esValido;
        return validarEmail(email) && esValido;
    }

    private boolean validarNombre(String nombre) {
        switch (Propietario.validarNombre(nombre)) {
            case VACIO:
                mErrorNombre.setValue(getApplication().getString(R.string.err_val_nombre_vacio));
                return false;
                case LONGITUD_MAXIMA:
                mErrorNombre.setValue(getApplication().getString(R.string.err_val_nombre_muy_largo));
                return false;
            default:
                return true;
        }
    }

    private boolean validarApellido(String apellido) {
        switch (Propietario.validarApellido(apellido)) {
            case VACIO:
                mErrorApellido.setValue(getApplication().getString(R.string.err_val_apellido_vacio));
                return false;
            case LONGITUD_MAXIMA:
                mErrorApellido.setValue(getApplication().getString(R.string.err_val_apellido_muy_largo));
                return false;
            default:
                return true;
        }
    }

    private boolean validarDNI(String dni) {
        switch (Propietario.validarDni(dni)) {
            case VACIO:
                mErrorDni.setValue(getApplication().getString(R.string.err_val_dni_vacio));
                return false;
            case NO_ES_NUMERO:
                mErrorDni.setValue(getApplication().getString(R.string.err_val_dni_no_es_numero));
                return false;
            case LONGITUD_INCORRECTA:
                    mErrorDni.setValue(getApplication().getString(R.string.err_val_dni_formato_incorrecto));
                return false;
            default:
                return true;
        }
    }

    private boolean validarTelefono(String telefono) {
        switch (Propietario.validarTelefono(telefono)) {
            case VACIO:
                mErrorTelefono.setValue(getApplication().getString(R.string.err_val_telefono_vacio));
                return false;
            case LONGITUD_MAXIMA:
                mErrorTelefono.setValue(getApplication().getString(R.string.err_val_telefono_muy_largo));
                return false;
            default:
                return true;
        }
    }

    private boolean validarEmail(String email) {
        switch (Propietario.validarEmail(email)) {
            case VACIO:
                mErrorEmail.setValue(getApplication().getString(R.string.err_val_email_vacio));
                return false;
            case LONGITUD_MAXIMA:
                mErrorEmail.setValue(getApplication().getString(R.string.err_val_email_muy_largo));
                return false;
            default:
                return true;
        }
    }
}