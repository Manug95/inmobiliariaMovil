package com.example.inmobiliaria.servicios;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria.BuildConfig;

public class TokenService {
    private static TokenService instancia;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private TokenService(Application context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(BuildConfig.ARCHIVO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static TokenService getInstancia(Application context) {
        if (instancia == null) {
            instancia = new TokenService(context);
        }
        return instancia;
    }

    public void guardarToken(String token) {
        editor.putString(BuildConfig.TOKEN, "Bearer " + token);
        editor.apply();
    }

    public String leerToken() {
        return sharedPreferences.getString(BuildConfig.TOKEN, null);
    }

    public void borrarToken(){
        editor.remove(BuildConfig.TOKEN);
        editor.apply();
    }

}
