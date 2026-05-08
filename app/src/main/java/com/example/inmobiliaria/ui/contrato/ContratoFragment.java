package com.example.inmobiliaria.ui.contrato;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliaria.databinding.FragmentContratoBinding;

public class ContratoFragment extends Fragment {
    private FragmentContratoBinding binding;
    private ContratoViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentContratoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ContratoViewModel.class);



        return binding.getRoot();
    }

}