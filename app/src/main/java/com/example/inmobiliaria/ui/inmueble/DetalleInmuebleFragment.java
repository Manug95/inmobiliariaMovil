package com.example.inmobiliaria.ui.inmueble;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentDetalleInmuebleBinding;
import com.example.inmobiliaria.util.Dialogo;
import com.example.inmobiliaria.util.MutableSingleEvent;

public class DetalleInmuebleFragment extends Fragment {
    private FragmentDetalleInmuebleBinding binding;
    private DetalleInmuebleViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentDetalleInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DetalleInmuebleViewModel.class);
        viewModel.getMInmueble().observe(getViewLifecycleOwner(), inmueble -> {
            String direccion = inmueble.getCalle() + " " + inmueble.getNroCalle();
            binding.tvDireccion.setText(direccion);
            binding.tvUso.setText(inmueble.getUso());
            binding.tvTipoInmueble.setText(inmueble.getTipoInmueble());
            binding.tvAmbientes.setText(String.valueOf(inmueble.getCantidadAmbientes()));
            binding.tvLatitud.setText(String.valueOf(inmueble.getLatitud()));
            binding.tvLongitud.setText(String.valueOf(inmueble.getLongitud()));
            binding.tvPrecio.setText(String.valueOf(inmueble.getPrecio()));
            binding.checkDisponible.setChecked(inmueble.isDisponible());
            /*Glide.with(binding.getRoot())
                    .load(ApiClient.URL_BASE + inmueble.getFoto())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.ivFoto);*/
        });

        viewModel.getMErroActualizacion().observe(getViewLifecycleOwner(), event -> mostrarMensaje(event, false));

        viewModel.getMEstadoDisponible().observe(getViewLifecycleOwner(), aBoolean -> binding.checkDisponible.setChecked(aBoolean));

        viewModel.getMEXitoCambioDisponibilidad().observe(getViewLifecycleOwner(), event -> mostrarMensaje(event, true));

        binding.checkDisponible.setOnClickListener(v -> viewModel.cambiarDisponibilidad(binding.checkDisponible.isChecked()));

        viewModel.recuperarInmueble(getArguments());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void mostrarDialogo(String mensaje, boolean exito){
        Dialogo dialogo = new Dialogo(getContext(),getLayoutInflater());
        dialogo.mostrarMensaje(mensaje, null, exito);
    }

    private void mostrarMensaje(MutableSingleEvent<String> event, boolean exito) {
        String mensaje = event.getContenido();
        if (mensaje != null) {
            mostrarDialogo(mensaje, exito);
        }
    }

}