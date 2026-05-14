package com.example.inmobiliaria.ui.inmueble;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelos.EditarDisponible;
import com.example.inmobiliaria.modelos.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;
import static com.example.inmobiliaria.util.Constantes.INMUEBLE;
import com.example.inmobiliaria.util.MutableSingleEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleInmuebleViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    private MutableLiveData<Inmueble> mInmueble;
    private MutableLiveData<MutableSingleEvent<String>> mExitoCambioDisponibilidad, mErrorActualizacion;
    private MutableLiveData<Boolean> mEstadoDisponible;
    public DetalleInmuebleViewModel(@NonNull Application application) {
        super(application);
        tokenService = TokenService.getInstancia(application);
    }

    public LiveData<Inmueble> getMInmueble() {
        if (mInmueble == null) mInmueble = new MutableLiveData<>();
        return mInmueble;
    }

    public LiveData<MutableSingleEvent<String>> getMEXitoCambioDisponibilidad() {
        if (mExitoCambioDisponibilidad == null) mExitoCambioDisponibilidad = new MutableLiveData<>();
        return mExitoCambioDisponibilidad;
    }

    public LiveData<MutableSingleEvent<String>> getMErroActualizacion() {
        if (mErrorActualizacion == null) mErrorActualizacion = new MutableLiveData<>();
        return mErrorActualizacion;
    }

    public LiveData<Boolean> getMEstadoDisponible() {
        if (mEstadoDisponible == null) mEstadoDisponible = new MutableLiveData<>();
        return mEstadoDisponible;
    }

    public void recuperarInmueble(Bundle bundle) {
        if (bundle != null) {
            Inmueble inmueble = (Inmueble) bundle.getSerializable(INMUEBLE);
            if (inmueble != null) mInmueble.setValue(inmueble);
        }
    }

    public void cambiarDisponibilidad(boolean estado) {
        if (mInmueble.getValue() != null) {
            ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();

            EditarDisponible editarDisponible = new EditarDisponible(estado);
            int inmuebleId = mInmueble.getValue().getId();

            Call<Void> callCambiarDisponible = api.cambiarDisponible(tokenService.leerToken(), inmuebleId, editarDisponible);

            callCambiarDisponible.enqueue(new Callback<>() {

                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        mExitoCambioDisponibilidad.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.disponibilidad_actualizada)));
                    } else {
                        mErrorActualizacion.postValue(new MutableSingleEvent<>(ApiClient.obtenerMensajeError(response.errorBody())));
                        mEstadoDisponible.postValue(!estado);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    mErrorActualizacion.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.err_al_actualizar_disponibilidad)));
                    mEstadoDisponible.postValue(!estado);
                }
            });

        }
    }

}