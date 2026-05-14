package com.example.inmobiliaria.ui.inmueble;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.FragmentInmuebleBinding;
import static com.example.inmobiliaria.util.Constantes.INMUEBLE;
import com.example.inmobiliaria.util.Dialogo;

public class InmuebleFragment extends Fragment {
    private FragmentInmuebleBinding binding;
    private InmuebleViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        binding = FragmentInmuebleBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InmuebleViewModel.class);

        viewModel.getMInmuebles().observe(getViewLifecycleOwner(), inmuebles -> {
            InmuebleAdapter adapter = new InmuebleAdapter(inmuebles, inmueble -> {
                Bundle bundle = new Bundle();
                bundle.putSerializable(INMUEBLE, inmueble);
                Navigation
                        .findNavController((Activity) getContext(), R.id.nav_host_fragment_content_main)
                        .navigate(R.id.action_nav_inmueble_to_detalleInmuebleFragment, bundle);
            });
            GridLayoutManager glm = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
            binding.rvInmuebles.setLayoutManager(glm);
            binding.rvInmuebles.setAdapter(adapter);
        });

        viewModel.getMErrorInmeubles().observe(getViewLifecycleOwner(), event -> {
            String mensaje = event.getContenido();
            if (mensaje != null) {
                mostrarDialogo(mensaje, false);
            }
        });

        binding.btnAddInmueble.setOnClickListener(v -> Navigation
                .findNavController(getActivity(), R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_nav_inmueble_to_nuevoInmuebleFragment)
        );

        viewModel.getInmuebles();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void mostrarDialogo(String mensaje, boolean exito){
        Dialogo dialogo = new Dialogo(getContext(), getLayoutInflater());
        dialogo.mostrarMensaje(mensaje, null, exito);
    }

}