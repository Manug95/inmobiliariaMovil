package com.example.inmobiliaria.ui.inmueble;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.inmobiliaria.databinding.FragmentNuevoInmuebleBinding;
import com.example.inmobiliaria.modelos.TipoInmueble;
import com.example.inmobiliaria.util.Dialogo;
import com.example.inmobiliaria.util.MutableSingleEvent;

import java.util.List;

public class NuevoInmuebleFragment extends Fragment {
    private FragmentNuevoInmuebleBinding binding;
    private NuevoInmuebleViewModel viewModel;
    private ActivityResultLauncher<Intent> arl;
    private Intent intentActivityGaleriaFotos;

    public static NuevoInmuebleFragment newInstance() {
        return new NuevoInmuebleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentNuevoInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(NuevoInmuebleViewModel.class);

        viewModel.getMURIFoto().observe(getViewLifecycleOwner(), uri -> binding.ivFotoNueva.setImageURI(uri));

        viewModel.getMMensajeExito().observe(getViewLifecycleOwner(), event -> mostrarMensaje(event, true));

        viewModel.getMMensajeError().observe(getViewLifecycleOwner(), event -> mostrarMensaje(event, true));

        viewModel.getMErrorCalle().observe(getViewLifecycleOwner(), s -> binding.tvErrorCalle.setText(s));

        viewModel.getMErrorCantidadAmbientes().observe(getViewLifecycleOwner(), s -> binding.tvErrorCantidadAmbientes.setText(s));

        viewModel.getMErrorPrecio().observe(getViewLifecycleOwner(), s -> binding.tvErrorPrecio.setText(s));

        viewModel.getMErrorNroCalle().observe(getViewLifecycleOwner(), s -> binding.tvErrorNroCalle.setText(s));

        viewModel.getMErrorTipoInmueble().observe(getViewLifecycleOwner(), s -> binding.tvErrorTipoInmueble.setText(s));

        viewModel.getMErrorUso().observe(getViewLifecycleOwner(), s -> binding.tvErrorUso.setText(s));

        viewModel.getMErrorFoto().observe(getViewLifecycleOwner(), s -> binding.tvErrorFoto.setText(s));

        viewModel.getMTiposInmueble().observe(getViewLifecycleOwner(), tiposInmueble -> {
            ArrayAdapter<TipoInmueble> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, tiposInmueble);
            binding.etTipoInmueble.setAdapter(adapter);
        });

        binding.btnGuardarNuevoInmeuble.setOnClickListener(v -> {
            viewModel.limpiarMensajesError();

            String calle = binding.etCalle.getText().toString();
            String nro = binding.etNroCalle.getText().toString();
            String tipoInmueble = binding.etTipoInmueble.getText().toString();
            String uso = binding.etUso.getText().toString();
            String cantAmbientes = binding.etCantidadAmbientes.getText().toString();
            String precio = binding.etPrecio.getText().toString();

            viewModel.guardarInmueble(calle, nro, tipoInmueble, uso, cantAmbientes, precio);
        });

        binding.btnCargarFoto.setOnClickListener(v -> arl.launch(intentActivityGaleriaFotos));

        viewModel.getTipoInmuebles();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, new String[]{"COMERCIAL", "RESIDENCIAL"});
        binding.etUso.setAdapter(adapter);
        abrirGaleria();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void abrirGaleria() {
        intentActivityGaleriaFotos = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        arl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                viewModel.recibirFoto(result);
            }
        });
    }

    private void mostrarDialogo(String mensaje, boolean exito){
        Dialogo dialogo = new Dialogo(getContext(),getLayoutInflater());
        dialogo.mostrarMensaje(mensaje, null, exito);
    }

    private void mostrarMensaje(MutableSingleEvent<String> mse, boolean exito) {
        String mensaje = mse.getContenido();
        if (mensaje != null) {
            mostrarDialogo(mensaje, exito);
        }
    }

}