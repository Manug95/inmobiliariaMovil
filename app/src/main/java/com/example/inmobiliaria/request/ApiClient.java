package com.example.inmobiliaria.request;

import com.example.inmobiliaria.BuildConfig;
import com.example.inmobiliaria.modelos.CambioContrasenia;
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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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

        @PATCH("Propietario/clave")
        Call<Void> cambiarContrasenia(@Header("Authorization") String token, @Body CambioContrasenia cambiarContrasenia);

        @GET("Propietario")
        Call<Propietario> getPropietario(@Header("Authorization") String token);

        @PUT("Propietario")
        Call<Propietario> putPropietario(@Header("Authorization") String token, @Body Propietario propietario);
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
