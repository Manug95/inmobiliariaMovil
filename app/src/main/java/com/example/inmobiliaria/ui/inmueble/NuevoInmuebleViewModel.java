package com.example.inmobiliaria.ui.inmueble;

import static android.app.Activity.RESULT_OK;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.modelos.Inmueble;
import com.example.inmobiliaria.modelos.TipoInmueble;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;
import com.example.inmobiliaria.util.Constantes;
import com.example.inmobiliaria.util.MutableSingleEvent;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevoInmuebleViewModel extends AndroidViewModel {
    private final TokenService tokenService;
    private List<TipoInmueble> tiposInmueble;
    private final MutableLiveData<String> mErrorFoto,
            mErrorCalle,
            mErrorNroCalle,
            mErrorTipoInmueble,
            mErrorUso,
            mErrorCantidadAmbientes,
            mErrorPrecio;
    private final MutableLiveData<MutableSingleEvent<String>> mMensajeExito, mMensajeError;
    private final MutableLiveData<Uri> mUriFoto;
    private final MutableLiveData<List<TipoInmueble>> mTiposInmueble;

    public NuevoInmuebleViewModel(@NonNull Application application) {
        super(application);
        tokenService = TokenService.getInstancia(application);
        mMensajeError = new MutableLiveData<>();
        mMensajeExito = new MutableLiveData<>();
        mErrorCalle = new MutableLiveData<>();
        mErrorNroCalle = new MutableLiveData<>();
        mErrorTipoInmueble = new MutableLiveData<>();
        mErrorUso = new MutableLiveData<>();
        mErrorCantidadAmbientes = new MutableLiveData<>();
        mErrorPrecio = new MutableLiveData<>();
        mUriFoto = new MutableLiveData<>();
        mErrorFoto = new MutableLiveData<>();
        mTiposInmueble = new MutableLiveData<>();
    }

    public void guardarInmueble(String calle, String nro, String tipoInmueble, String uso, String cantAmbientes, String precio) {
        if (!esInmuebleValido(calle, nro, tipoInmueble, uso, cantAmbientes, precio))
            return;

        Inmueble inmueble = new Inmueble(
                calle,
                Integer.parseInt(nro),
                Integer.parseInt(cantAmbientes),
                tipoInmueble,
                uso,
                Double.parseDouble(precio),
                false
        );
        int idTipoInmueble = getIdTipoInmueble(tipoInmueble);
        if (idTipoInmueble > 0)
            inmueble.setIdTipoInmueble(idTipoInmueble);

        String inmuebleJson = new Gson().toJson(inmueble);
        RequestBody inmuebleBody = RequestBody.create(MediaType.parse(Constantes.CONTENT_TYPE_JSON), inmuebleJson);

        byte[] foto = transformarImagen();
        RequestBody requestFile = RequestBody.create(MediaType.parse(Constantes.IMG_MEDIATYPE_JPEG), foto);
        String fileName = Constantes.FOTO + "." + Constantes.JPG;
        MultipartBody.Part imagenPart = MultipartBody.Part.createFormData(Constantes.FOTO, fileName, requestFile);

        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<Inmueble> callCrearInmueble = api.crearInmueble(tokenService.leerToken(), imagenPart, inmuebleBody);

        callCrearInmueble.enqueue(new Callback<Inmueble>() {

            @Override
            public void onResponse(@NonNull Call<Inmueble> call, @NonNull Response<Inmueble> response) {
                if (response.isSuccessful()) {
                    mMensajeExito.setValue(new MutableSingleEvent<>(getApplication().getString(R.string.inmueble_creado)));
                } else {
                    mMensajeError.setValue(new MutableSingleEvent<>(ApiClient.obtenerMensajeError(response.errorBody())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Inmueble> call, @NonNull Throwable t) {
                mMensajeError.setValue(new MutableSingleEvent<>(getApplication().getString(R.string.err_peticion)));
            }
        });
    }

    public void getTipoInmuebles() {
        ApiClient.InmobiliariaService api = ApiClient.getInmobiliariaService();
        Call<List<TipoInmueble>> callGetTiposInmuebles = api.getTiposInmuebles(tokenService.leerToken());

        callGetTiposInmuebles.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<TipoInmueble>> call, @NonNull Response<List<TipoInmueble>> response) {
                if (response.isSuccessful()) {
                    tiposInmueble = response.body();
                    mTiposInmueble.postValue(tiposInmueble);
                } else
                    Toast.makeText(getApplication(), getApplication().getString(R.string.peticion_exitosa), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(@NonNull Call<List<TipoInmueble>> call, @NonNull Throwable t) {
                Toast.makeText(getApplication(), "se ejecuto onFailure", Toast.LENGTH_LONG).show();
            }
        });

    }

    private byte[] transformarImagen() {
        try {
            Uri uri = mUriFoto.getValue();
            InputStream inputStream = getApplication().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (
                FileNotFoundException er) {
            Toast.makeText(getApplication(), getApplication().getString(R.string.err_foto), Toast.LENGTH_SHORT).show();
            return new byte[]{};
        }
    }

    public void recibirFoto(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                Uri uri = data.getData();
                mUriFoto.setValue(uri);
            }
        }
    }

    public void limpiarMensajesError() {
        mErrorCalle.setValue("");
        mErrorNroCalle.setValue("");
        mErrorTipoInmueble.setValue("");
        mErrorUso.setValue("");
        mErrorCantidadAmbientes.setValue("");
        mErrorPrecio.setValue("");
        mErrorFoto.setValue("");
    }

    private boolean esInmuebleValido(String calle, String nro, String tipoInmueble, String uso, String cantAmbientes, String precio) {
        boolean esValido = validarCalle(calle);
        esValido = validarNroCalle(nro) && esValido;
        esValido = validarTipoInmueble(tipoInmueble) && esValido;
        esValido = validarUso(uso) && esValido;
        esValido = validarCantidadAmbientes(cantAmbientes) && esValido;
        esValido = validarPrecio(precio) && esValido;
        return validarFoto() && esValido;
    }

    private int getIdTipoInmueble(String tipo) {
        for (TipoInmueble tipoInmueble : tiposInmueble) {
            if (tipoInmueble.getTipo().equals(tipo)) {
                return tipoInmueble.getId();
            }
        }
        return -1;
    }

    public LiveData<MutableSingleEvent<String>> getMMensajeError() {
        return mMensajeError;
    }

    public LiveData<MutableSingleEvent<String>> getMMensajeExito() {
        return mMensajeExito;
    }

    public LiveData<String> getMErrorCalle() {
        return mErrorCalle;
    }

    public LiveData<String> getMErrorNroCalle() {
        return mErrorNroCalle;
    }

    public LiveData<String> getMErrorTipoInmueble() {
        return mErrorTipoInmueble;
    }

    public LiveData<String> getMErrorUso() {
        return mErrorUso;
    }

    public LiveData<String> getMErrorCantidadAmbientes() {
        return mErrorCantidadAmbientes;
    }

    public LiveData<String> getMErrorPrecio() {
        return mErrorPrecio;
    }

    public LiveData<Uri> getMURIFoto() {
        return mUriFoto;
    }

    public LiveData<String> getMErrorFoto() {
        return mErrorFoto;
    }

    public LiveData<List<TipoInmueble>> getMTiposInmueble() {
        return mTiposInmueble;
    }

    private boolean validarCalle(String calle) {
        switch (Inmueble.validarCalle(calle)) {
            case VACIO:
                mErrorCalle.setValue(getApplication().getString(R.string.err_val_calle_vacia));
                return false;
            case LONGITUD_MAXIMA:
                mErrorCalle.setValue(getApplication().getString(R.string.err_val_calle_muy_larga));
                return false;
            default:
                return true;
        }
    }

    private boolean validarNroCalle(String nro) {
        switch (Inmueble.validarNroCalle(nro)) {
            case VACIO:
                mErrorNroCalle.setValue(getApplication().getString(R.string.err_val_nro_calle_vacio));
                return false;
            case VALOR_NO_POSITIVO:
                mErrorNroCalle.setValue(getApplication().getString(R.string.err_val_nro_calle_no_positivo));
                return false;
            case NO_ES_NUMERO:
                mErrorNroCalle.setValue(getApplication().getString(R.string.err_val_nro_calle_no_es_numero));
                return false;
            default:
                return true;
        }
    }

    private boolean validarTipoInmueble(String tipo) {
        switch (Inmueble.validarTipoInmueble(tipo)) {
            case VACIO:
                mErrorTipoInmueble.setValue(getApplication().getString(R.string.err_val_tipo_inmueble_vacio));
                return false;
            case LONGITUD_MAXIMA:
                mErrorTipoInmueble.setValue(getApplication().getString(R.string.err_val_tipo_inmueble_muy_largo));
                return false;
            default:
                return true;
        }
    }

    private boolean validarUso(String uso) {
        switch (Inmueble.validarUso(uso)) {
            case VACIO:
                mErrorUso.setValue(getApplication().getString(R.string.err_val_uso_vacio));
                return false;
            case LONGITUD_MAXIMA:
                mErrorUso.setValue(getApplication().getString(R.string.err_val_uso_muy_largo));
                return false;
            default:
                return true;
        }
    }

    private boolean validarCantidadAmbientes(String cantAmbientes) {
        switch (Inmueble.validarCantidadAmbientes(cantAmbientes)) {
            case VACIO:
                mErrorCantidadAmbientes.setValue(getApplication().getString(R.string.err_val_cant_ambientes_vacia));
                return false;
            case VALOR_NO_POSITIVO:
                mErrorCantidadAmbientes.setValue(getApplication().getString(R.string.err_val_cant_ambientes_no_positiva));
                return false;
            case NO_ES_NUMERO:
                mErrorCantidadAmbientes.setValue(getApplication().getString(R.string.err_val_cant_ambientes_no_es_numero));
                return false;
            default:
                return true;
        }
    }

    private boolean validarPrecio(String precio) {
        switch (Inmueble.validarPrecio(precio)) {
            case VACIO:
                mErrorPrecio.setValue(getApplication().getString(R.string.err_val_precio_vacio));
                return false;
            case VALOR_NO_POSITIVO:
                mErrorPrecio.setValue(getApplication().getString(R.string.err_val_precio_no_positivo));
                return false;
            case NO_ES_NUMERO:
                mErrorPrecio.setValue(getApplication().getString(R.string.err_val_precio_no_es_numero));
                return false;
            default:
                return true;
        }
    }

    private boolean validarFoto() {
        if (mUriFoto.getValue() == null) {
            mErrorFoto.setValue(getApplication().getString(R.string.err_val_foto));
            return false;
        }
        return true;
    }
}