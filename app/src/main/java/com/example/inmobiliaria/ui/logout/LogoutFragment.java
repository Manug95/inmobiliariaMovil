package com.example.inmobiliaria.ui.logout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.LoginActivity;
import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentLogoutBinding;
import com.example.inmobiliaria.request.ApiClient;
import com.example.inmobiliaria.servicios.TokenService;
import com.example.inmobiliaria.util.Dialogo;

public class LogoutFragment extends Fragment {
    private FragmentLogoutBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);

        Dialogo dialogo = new Dialogo(getContext(), getLayoutInflater());
        dialogo.mostrarPregunta(getString(R.string.pregunta_dialog_salir), (dialogInterface, i) -> {
            TokenService.getInstancia(getActivity().getApplication()).borrarToken();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            getActivity().finish();
        }, null);

        return binding.getRoot();
    }
}