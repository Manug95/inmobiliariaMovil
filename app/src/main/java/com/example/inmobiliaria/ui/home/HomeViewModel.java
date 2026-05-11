package com.example.inmobiliaria.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeViewModel extends AndroidViewModel {
    private final MutableLiveData<MapaActual> mMapa;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mMapa = new MutableLiveData<>();
    }

    public LiveData<MapaActual> getMutableMapaActual() {
        return mMapa;
    }

    public void cargarMapa() {
        mMapa.setValue(new MapaActual());
    }

    public static class MapaActual implements OnMapReadyCallback {
        private final LatLng inmobiliaria = new LatLng(-33.150720, -66.306864);

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            MarkerOptions marcadorInmobiliaria = new MarkerOptions();
            marcadorInmobiliaria.position(inmobiliaria);
            marcadorInmobiliaria.title("San Luis");

            googleMap.addMarker(marcadorInmobiliaria);
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(inmobiliaria)
                    .zoom(15)
                    .bearing(45)
                    .tilt(15)
                    .build();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

            googleMap.animateCamera(cameraUpdate);
        }
    }
}