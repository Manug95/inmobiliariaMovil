package com.example.inmobiliaria;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.modelos.Login;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    private final MutableLiveData<String>
            mLoginCorrecto,
            mLoginIncorrecto,
            mEmailIncorrecto,
            mClaveIncorrecta;
    private final MutableLiveData<Void> mEstaLogueado;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        this.tokenService = TokenService.getInstancia(application);
        mLoginCorrecto = new MutableLiveData<>();
        mLoginIncorrecto = new MutableLiveData<>();
        mEstaLogueado = new MutableLiveData<>();
        mEmailIncorrecto = new MutableLiveData<>();
        mClaveIncorrecta = new MutableLiveData<>();
    }

    public LiveData<String> getMLoginCorrecto() {
        return mLoginCorrecto;
    }
    public LiveData<String> getMLoginIncorrecto() {
        return mLoginIncorrecto;
    }
    public LiveData<Void> getmEstaLogueado() {
        return mEstaLogueado;
    }
    public LiveData<String> getMEmailIncorrecto() { return mEmailIncorrecto; }
    public LiveData<String> getMClaveIncorrecta() { return mClaveIncorrecta; }


    public void login(String email, String password) {
        if (validarCredenciales(email, password)) {
            ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
            Call<String> apiCall = api.login(new Login(email, password));

            apiCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        tokenService.guardarToken(response.body());
                        mLoginCorrecto.setValue(getApplication().getString(R.string.bienvenido));
                    } else {
                        mLoginIncorrecto.setValue(ApiClient.obtenerMensajeError(response.errorBody()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    mLoginIncorrecto.setValue(getApplication().getString(R.string.err_al_iniciar_sesion));
                }
            });
        }
    }

    public void comprobarSesion() {
        String token = tokenService.leerToken();
        if (token != null) {
            mEstaLogueado.setValue(null);
        }
    }

    private boolean validarCredenciales(String email, String clave) {
        boolean esValido = validarEmail(email);
        return validarClave(clave) && esValido;
    }

    private boolean validarEmail(String email) {
        switch(Login.validarEmail(email)) {
            case VACIO:
                mEmailIncorrecto.setValue(getApplication().getString(R.string.err_val_email_vacio));
                return false;
            case LONGITUD_MAXIMA:
                mEmailIncorrecto.setValue(getApplication().getString(R.string.err_val_email_muy_largo));
                return false;
            default:
                return true;
        }
    }

    private boolean validarClave(String clave) {
        switch (Login.validarClave(clave)) {
            case VACIO:
                mClaveIncorrecta.setValue(getApplication().getString(R.string.err_val_clave_vacia));
                return false;
            case LONGITUD_MINIMA:
                mClaveIncorrecta.setValue(getApplication().getString(R.string.err_val_clave_muy_corta));
                return false;
            default:
                return true;
        }
    }
}
