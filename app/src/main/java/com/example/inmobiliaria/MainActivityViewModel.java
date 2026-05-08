package com.example.inmobiliaria;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelos.DatosUsuario;
import com.example.inmobiliaria.modelos.Propietario;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    public MutableLiveData<DatosUsuario> mDatosUsuario;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.tokenService = TokenService.getInstancia(application);
        mDatosUsuario = new MutableLiveData<>();
    }

    public LiveData<DatosUsuario> getmDatosUsuario() {
        return mDatosUsuario;
    }

    public void traerPropietario() {
        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<Propietario> apiCall = api.getPropietario(tokenService.leerToken());

        apiCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Propietario> call, @NonNull Response<Propietario> response) {
                if (response.isSuccessful()) {
                    Propietario propietario = response.body();
                    if (propietario != null) {
                        String nombreCompleto = propietario.getNombre() + " " + propietario.getApellido();
                        mDatosUsuario.postValue(new DatosUsuario(nombreCompleto, propietario.getEmail()));
                    }
                } else {
                    Toast.makeText(getApplication(), ApiClient.obtenerMensajeError(response.errorBody()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Propietario> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), R.string.err_al_obtener_propietario, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
