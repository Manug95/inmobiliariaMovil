package com.example.inmobiliaria.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentPerfilBinding;
import com.example.inmobiliaria.util.Dialogo;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private PerfilViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(PerfilViewModel.class);

        viewModel.getMPropietario().observe(getViewLifecycleOwner(), propietario -> {
            binding.etNombre.setText(propietario.getNombre());
            binding.etApellido.setText(propietario.getApellido());
            binding.etDni.setText(propietario.getDni());
            binding.etTelefono.setText(propietario.getTelefono());
            binding.etEmail.setText(propietario.getEmail());
        });

        viewModel.getMEstadoEditar().observe(getViewLifecycleOwner(), aBoolean -> {
            binding.etNombre.setEnabled(aBoolean);
            binding.etApellido.setEnabled(aBoolean);
            binding.etDni.setEnabled(aBoolean);
            binding.etTelefono.setEnabled(aBoolean);
            binding.etEmail.setEnabled(aBoolean);
        });

        viewModel.getMTextoBoton().observe(getViewLifecycleOwner(), s -> binding.btnAccion.setText(s));

        viewModel.getMMensajeError().observe(getViewLifecycleOwner(), s -> muestraDialog(s, false));

        viewModel.getMPerfilActualizado().observe(getViewLifecycleOwner(), event -> {
            String mensaje = event.getContenido();
            if (mensaje != null) {
                setBotonCancelarVisible(false);
                muestraDialog(mensaje, true);
            }
        });

        viewModel.getMErrorDni().observe(getViewLifecycleOwner(), s -> binding.tvErrorDni.setText(s));

        viewModel.getMErrorNombre().observe(getViewLifecycleOwner(), s -> binding.tvErrorNombre.setText(s));

        viewModel.getMErrorApellido().observe(getViewLifecycleOwner(), s -> binding.tvErrorApellido.setText(s));

        viewModel.getMErrorTelefono().observe(getViewLifecycleOwner(), s -> binding.tvErrorTelefono.setText(s));

        viewModel.getMErrorEmail().observe(getViewLifecycleOwner(), s -> binding.tvErrorEmail.setText(s));

        binding.btnAccion.setOnClickListener(v -> {
            resetearMensajesError();

            String nombre = binding.etNombre.getText().toString();
            String apellido = binding.etApellido.getText().toString();
            String dni = binding.etDni.getText().toString();
            String telefono = binding.etTelefono.getText().toString();
            String email = binding.etEmail.getText().toString();

            setBotonCancelarVisible(true);

            viewModel.editarPropietario(binding.btnAccion.getText().toString(), nombre, apellido, dni, telefono, email);
        });

        binding.btnCancelarEdicionPerfil.setOnClickListener( v -> {
            setBotonCancelarVisible(false);
            viewModel.cambiarAEditar();
            viewModel.resetearDatosPropietario();
        });

        binding.btnIrCambiarContrasenia.setOnClickListener(v -> Navigation
                .findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_nav_perfil_to_cambiarContraseniaFragment)
        );

        viewModel.obtenerPropietario();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void muestraDialog(String mensaje, boolean exito){
        Dialogo dialogo = new Dialogo(requireContext(), getLayoutInflater());
        dialogo.mostrarMensaje(mensaje, null, exito);
    }

    private void resetearMensajesError() {
        binding.tvErrorDni.setText("");
        binding.tvErrorNombre.setText("");
        binding.tvErrorApellido.setText("");
        binding.tvErrorTelefono.setText("");
        binding.tvErrorEmail.setText("");
    }

    private void setBotonCancelarVisible(boolean visible) {
        binding.btnCancelarEdicionPerfil.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

}