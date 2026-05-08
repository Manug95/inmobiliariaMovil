package com.example.inmobiliaria.request;

import com.example.inmobiliaria.BuildConfig;
import com.example.inmobiliaria.modelos.Login;
import com.example.inmobiliaria.modelos.Propietario;
import com.example.inmobiliaria.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.time.LocalDate;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class ApiClient {
    public static InmobiliariaService getInmobiliariaService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit.create(InmobiliariaService.class);
    }

    public interface InmobiliariaService {
        @POST("Propietario/login")
        Call<String> login(@Body Login login);

        @GET("Propietario")
        Call<Propietario> getPropietario(@Header("Authorization") String token);
    }

    public static String obtenerMensajeError(ResponseBody responseBody) {
        String mensajeError = "Error";
        try {
            String errorJson = responseBody.string();
            JSONObject errorObj = new JSONObject(errorJson);
            if (errorObj.has("mensaje")) {
                mensajeError = errorObj.getString("mensaje");
            }
        } catch (Exception ignored) {
        }
        return mensajeError;
    }
}
