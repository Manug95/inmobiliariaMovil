package com.example.inmobiliaria.servicios;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.inmobiliaria.BuildConfig;

public class TokenService {
    private static TokenService instancia;
    private static Application application;

    private TokenService() { }

    public static TokenService getInstancia(Application context) {
        application = context;
        if (instancia == null) {
            instancia = new TokenService();
        }
        return instancia;
    }

    private static SharedPreferences getSharedPreferencesObject(Context context) {
        return context.getSharedPreferences(BuildConfig.ARCHIVO_SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getSharedPreferencesEditorObject(SharedPreferences sp) {
        return sp.edit();
    }

    public void guardarToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferencesObject(application.getApplicationContext());
        SharedPreferences.Editor editor = getSharedPreferencesEditorObject(sharedPreferences);
        editor.putString(BuildConfig.TOKEN, "Bearer " + token);
        editor.apply();
    }

    public String leerToken() {
        SharedPreferences sharedPreferences = getSharedPreferencesObject(application.getApplicationContext());
        return sharedPreferences.getString(BuildConfig.TOKEN, null);
    }

    public void borrarToken(){
        SharedPreferences sharedPreferences = getSharedPreferencesObject(application.getApplicationContext());
        SharedPreferences.Editor editor = getSharedPreferencesEditorObject(sharedPreferences);
        editor.remove(BuildConfig.TOKEN);
        editor.apply();
    }

}
