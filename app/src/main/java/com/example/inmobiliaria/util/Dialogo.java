package com.example.inmobiliaria.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import androidx.annotation.ColorInt;

import com.example.inmobiliaria.R;
import com.example.inmobiliaria.databinding.DialogoMensajePersonalizadoBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public final class Dialogo {
    private final Context context;
    private final DialogoMensajePersonalizadoBinding dialogoBinding;
    @ColorInt
    private final int COLOR_ERROR;
    @ColorInt
    private final int COLOR_EXITO;

    public Dialogo(Context context, LayoutInflater inflater) {
        this.context = context;
        this.dialogoBinding = DialogoMensajePersonalizadoBinding.inflate(inflater);
        COLOR_EXITO = context.getResources().getColor(R.color.exito, null);
        COLOR_ERROR = context.getResources().getColor(R.color.error, null);
    }

    public void mostrarMensaje(String mensaje, DialogInterface.OnClickListener listener, boolean exito){
        dialogoBinding.tvMensajeDialog.setText(mensaje);
        dialogoMensaje(listener, exito);
    }

    public void mostrarMensaje(int mensaje, DialogInterface.OnClickListener listener, boolean exito) {
        dialogoBinding.tvMensajeDialog.setText(context.getString(mensaje));
        dialogoMensaje(listener, exito);
    }

    public void mostrarPregunta(String pregunta, DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo){
        dialogoBinding.tvMensajeDialog.setText(pregunta);
        dialogoPregunta(listenerSi, listenerNo);
    }

    public void mostrarPregunta(int pregunta, DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo){
        dialogoBinding.tvMensajeDialog.setText(context.getString(pregunta));
        dialogoPregunta(listenerSi, listenerNo);
    }

    private void dialogoMensaje(DialogInterface.OnClickListener listener, boolean exito) {
        dialogoBinding.tvMensajeDialog.setTextColor(exito ? COLOR_EXITO : COLOR_ERROR);

        if (listener == null) {
            listener = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface di,int i){
                    di.dismiss();
                }
            };
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.titulo_dialog_mensaje)
                .setView(dialogoBinding.getRoot())
                .setNeutralButton(R.string.dialog_ok, listener)
                .show();
    }

    private void dialogoPregunta(DialogInterface.OnClickListener listenerSi, DialogInterface.OnClickListener listenerNo) {
        if (listenerNo == null) {
            listenerNo = new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface di,int i){
                    di.dismiss();
                }
            };
        }

        new MaterialAlertDialogBuilder(context)
                .setTitle(R.string.titulo_dialog_salir)
                .setView(dialogoBinding.getRoot())
                .setPositiveButton(R.string.dialog_si, listenerSi)
                .setNegativeButton(R.string.dialog_no, listenerNo)
                .show();
    }
}
