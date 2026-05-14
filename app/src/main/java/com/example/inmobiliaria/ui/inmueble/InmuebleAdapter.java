package com.example.inmobiliaria.ui.inmueble;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inmobiliaria.databinding.ItemInmuebleBinding;
import com.example.inmobiliaria.modelos.Inmueble;

import java.util.List;

public class InmuebleAdapter extends RecyclerView.Adapter<InmuebleAdapter.InmuebleViewHolder>{
    private final List<Inmueble> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Inmueble inmueble);
    }

    public InmuebleAdapter(List<Inmueble> listaInmuebles, OnItemClickListener listener) {
        this.lista = listaInmuebles;
        this.listener = listener;
    }

    public InmuebleAdapter(List<Inmueble> listaInmuebles) {
        this.lista = listaInmuebles;
    }

    @NonNull
    @Override
    public InmuebleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInmuebleBinding binding = ItemInmuebleBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InmuebleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InmuebleViewHolder holder, int position) {
        Inmueble inmueble = lista.get(position);
        holder.bind(inmueble);
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public class InmuebleViewHolder extends RecyclerView.ViewHolder{
        private final ItemInmuebleBinding binding;

        public InmuebleViewHolder(@NonNull ItemInmuebleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void bind(Inmueble inmueble) {
            String direccion = inmueble.getCalle() + " " + inmueble.getNroCalle();
            binding.tvDireccion.setText(direccion);
            binding.tvPrecio.setText("$" + inmueble.getPrecio());
            /*Glide.with(binding.getRoot())
                    .load(ApiClient.URL_BASE + inmueble.getFoto())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.ivFotoInmueble);*/

            binding.itemInmueble.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(inmueble);
                }
            });

        }
    }
}
