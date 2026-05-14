package com.example.inmobiliaria.request;

import com.example.inmobiliaria.BuildConfig;
import com.example.inmobiliaria.modelos.CambioContrasenia;
import com.example.inmobiliaria.modelos.EditarDisponible;
import com.example.inmobiliaria.modelos.Inmueble;
import com.example.inmobiliaria.modelos.Login;
import com.example.inmobiliaria.modelos.Propietario;
import com.example.inmobiliaria.modelos.TipoInmueble;
import com.example.inmobiliaria.util.Constantes;
import com.example.inmobiliaria.util.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public class ApiClient {
    public static final String URL_BASE = BuildConfig.API_URL_BASE;

    public static InmobiliariaService getInmobiliariaService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
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

        @GET("Inmueble")
        Call<List<Inmueble>> getInmuebles(@Header("Authorization") String token);

        @PATCH("Inmueble/{id}/disponible")
        Call<Void> cambiarDisponible(@Header("Authorization") String token, @Path("id") int id, @Body EditarDisponible editarDisponible);

        @Multipart
        @POST("Inmueble")
        Call<Inmueble> crearInmueble(@Header("Authorization") String token,
                                     @Part MultipartBody.Part foto,
                                     @Part("inmueble") RequestBody inmuebleBody);

        @GET("TipoInmueble")
        Call<List<TipoInmueble>> getTiposInmuebles(@Header("Authorization") String token);
    }

    public static String obtenerMensajeError(ResponseBody responseBody) {
        String mensajeError = Constantes.ERROR;
        try {
            String errorJson = responseBody.string();
            JSONObject errorObj = new JSONObject(errorJson);
            if (errorObj.has(Constantes.MENSAJE)) {
                mensajeError = errorObj.getString(Constantes.MENSAJE);
            }
        } catch (Exception ignored) {
        }
        return mensajeError;
    }
}
