package com.example.inmobiliaria.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentCambiarContraseniaBinding;
import com.example.inmobiliaria.util.Dialogo;

public class CambiarContraseniaFragment extends Fragment {
    private FragmentCambiarContraseniaBinding binding;
    private CambiarContraseniaViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentCambiarContraseniaBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(CambiarContraseniaViewModel.class);

        viewModel.getMContraseniaCambiada().observe(getViewLifecycleOwner(), s -> muestraDialog(s, true));

        viewModel.getMErrorCambiarContrasenia().observe(getViewLifecycleOwner(), s -> muestraDialog(s, false));

        viewModel.getMErrorNuevaContrasenia().observe(getViewLifecycleOwner(), s -> binding.tvErrorContraseniaNueva.setText(s));

        viewModel.getMErrorContraseniaActual().observe(getViewLifecycleOwner(), s -> binding.tvErrorContraseniaActual.setText(s));

        viewModel.getMErrorRepetirContrasenia().observe(getViewLifecycleOwner(), s -> binding.tvErrorContraseniaNuevaRepetida.setText(s));

        binding.btnCambiarContrasenia.setOnClickListener(v -> {
            resetearMensajesError();

            String contraseniaActual = binding.etContraseniaActual.getText().toString();
            String nuevaContrasenia = binding.etNuevaContrasenia.getText().toString();
            String repetirContrasenia = binding.etNuevaContraseniaRepetida.getText().toString();

            viewModel.cambiarContrasenia(contraseniaActual, nuevaContrasenia, repetirContrasenia);
        });

        return binding.getRoot();
    }

    private void muestraDialog(String mensaje, boolean exito){
        Dialogo dialogo = new Dialogo(getContext(), getLayoutInflater());
        dialogo.mostrarMensaje(mensaje, null, exito);
    }

    private void resetearMensajesError() {
        binding.tvErrorContraseniaActual.setText("");
        binding.tvErrorContraseniaNueva.setText("");
        binding.tvErrorContraseniaNuevaRepetida.setText("");
    }

}