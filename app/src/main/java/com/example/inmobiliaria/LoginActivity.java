package com.example.inmobiliaria;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.inmobiliaria.databinding.ActivityLoginBinding;
import com.example.inmobiliaria.util.Dialogo;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginActivityViewModel.class);

        viewModel.comprobarSesion();

        viewModel.getMEmailIncorrecto().observe(this, s -> binding.tvEmailIncorrecto.setText(s));

        viewModel.getMClaveIncorrecta().observe(this, s -> binding.tvClaveIncorrecta.setText(s));

        viewModel.getMLoginCorrecto().observe(this, s -> {
            iniciarMainActivity();
            Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
            finish();
        });

        viewModel.getMLoginIncorrecto().observe(this, s -> {
            Dialogo dialogo = new Dialogo(LoginActivity.this, getLayoutInflater());
            dialogo.mostrarMensaje(s, null, false);
        });

        viewModel.getmEstaLogueado().observe(this, v -> {
            iniciarMainActivity();
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            limpiarMensajes();
            String email = binding.etMailLogin.getText().toString();
            String password = binding.etPasswordLogin.getText().toString();
            viewModel.login(email, password);
        });
    }

    private void limpiarMensajes() {
        binding.tvEmailIncorrecto.setText("");
        binding.tvClaveIncorrecta.setText("");
    }

    private void iniciarMainActivity() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}