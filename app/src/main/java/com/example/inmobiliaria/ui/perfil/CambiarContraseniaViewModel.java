package com.example.inmobiliaria.ui.perfil;

import static com.example.inmobiliaria.util.TipoErrorValidacion.VACIO;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelos.CambioContrasenia;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;
import com.example.inmobiliaria.util.MutableSingleEvent;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarContraseniaViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    private final MutableLiveData<String> mErrorContraseniaActual, mErrorNuevaContrasenia, mErrorRepetirContrasenia;
    private final MutableLiveData<MutableSingleEvent<String>> mErrorCambiarContrasenia, mContraseniaCambiada;

    public CambiarContraseniaViewModel(@NonNull Application application) {
        super(application);
        tokenService = TokenService.getInstancia(application);
        mErrorContraseniaActual = new MutableLiveData<>();
        mErrorNuevaContrasenia = new MutableLiveData<>();
        mErrorRepetirContrasenia = new MutableLiveData<>();
        mErrorCambiarContrasenia = new MutableLiveData<>();
        mContraseniaCambiada = new MutableLiveData<>();
    }

    public LiveData<String> getMErrorContraseniaActual() {
        return mErrorContraseniaActual;
    }

    public LiveData<String> getMErrorNuevaContrasenia() {
        return mErrorNuevaContrasenia;
    }

    public LiveData<String> getMErrorRepetirContrasenia() {
        return mErrorRepetirContrasenia;
    }

    public LiveData<MutableSingleEvent<String>> getMErrorCambiarContrasenia() {
        return mErrorCambiarContrasenia;
    }

    public LiveData<MutableSingleEvent<String>> getMContraseniaCambiada() {
        return mContraseniaCambiada;
    }

    public void cambiarContrasenia(String calveActual, String claveNueva, String claveNuevaRepetida) {
        if (sonClavesValidas(calveActual, claveNueva, claveNuevaRepetida)) {
            ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
            Call<Void> callCambiarContrasenia = api.cambiarContrasenia(tokenService.leerToken(), new CambioContrasenia(calveActual, claveNueva));

            callCambiarContrasenia.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        mContraseniaCambiada.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.contrasenia_actualizada)));
                    } else {
                        mErrorCambiarContrasenia.postValue(new MutableSingleEvent<>(ApiClient.obtenerMensajeError(response.errorBody())));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    mErrorCambiarContrasenia.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.err_al_cambiar_contrasenia)));
                }
            });
        }
    }

    public void resetearMensajesError() {
        mErrorContraseniaActual.setValue("");
        mErrorNuevaContrasenia.setValue("");
        mErrorRepetirContrasenia.setValue("");
    }

    private boolean sonClavesValidas(String calveActual, String claveNueva, String claveNuevaRepetida) {
        boolean sonValidas = validarClaveActual(calveActual);
        sonValidas = validarClaveNueva(claveNueva) && sonValidas;
        return validarClaveNuevaRepetida(claveNueva, claveNuevaRepetida) && sonValidas;
    }

    private boolean validarClaveActual(String contraseniaActual) {
        if (CambioContrasenia.validarClaveActual(contraseniaActual) == VACIO) {
            mErrorContraseniaActual.setValue(getApplication().getString(R.string.err_val_clave_actual_vacia));
            return false;
        }
        return true;
    }

    private boolean validarClaveNueva(String claveNueva) {
        if (CambioContrasenia.validarClaveNueva(claveNueva) == VACIO) {
            mErrorNuevaContrasenia.setValue(getApplication().getString(R.string.err_val_clave_nueva_vacia));
            return false;
        }
        return true;
    }

    private boolean validarClaveNuevaRepetida(String claveNueva, String claveNuevaRepetida) {
        if (!Objects.equals(claveNueva, claveNuevaRepetida)) {
            mErrorRepetirContrasenia.setValue(getApplication().getString(R.string.err_val_clave_no_coiciden));
            mErrorNuevaContrasenia.setValue(getApplication().getString(R.string.err_val_clave_no_coiciden));
            return false;
        }
        return true;
    }
}