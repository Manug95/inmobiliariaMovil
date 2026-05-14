package com.example.inmobiliaria.ui.inmueble;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelos.Inmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;
import com.example.inmobiliaria.util.MutableSingleEvent;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InmuebleViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    private MutableLiveData<List<Inmueble>> mInmuebles;
    private MutableLiveData<MutableSingleEvent<String>> mErrorInmuebles;

    public InmuebleViewModel(@NonNull Application application) {
        super(application);
        tokenService = TokenService.getInstancia(application);
    }

    public LiveData<List<Inmueble>> getMInmuebles() {
        if (mInmuebles == null) mInmuebles = new MutableLiveData<>();
        return mInmuebles;
    }

    public LiveData<MutableSingleEvent<String>> getMErrorInmeubles() {
        if (mErrorInmuebles == null) mErrorInmuebles = new MutableLiveData<>();
        return mErrorInmuebles;
    }

    public void getInmuebles() {
        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<List<Inmueble>> callGetInmuebles = api.getInmuebles(tokenService.leerToken());

        callGetInmuebles.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Inmueble>> call, @NonNull Response<List<Inmueble>> response) {
                if (response.isSuccessful()) {
                    List<Inmueble> inmuebles = response.body();
                    if (inmuebles != null && inmuebles.isEmpty())
                        mErrorInmuebles.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.lista_inmuebles_vacia)));
                    else
                        mInmuebles.postValue(inmuebles);
                } else {
                    mErrorInmuebles.postValue(new MutableSingleEvent<>(ApiClient.obtenerMensajeError(response.errorBody())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Inmueble>> call, @NonNull Throwable t) {
                mErrorInmuebles.postValue(new MutableSingleEvent<>(getApplication().getString(R.string.err_cargar_inmuebles)));
            }
        });
    }
}