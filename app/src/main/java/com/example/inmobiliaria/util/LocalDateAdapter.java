package com.example.inmobiliaria.util;

import com.google.gson.stream.JsonToken;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Para "yyyy-MM-dd'T'HH:mm:ss"
    private static final DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ISO_LOCAL_DATE; // Para "yyyy-MM-dd"

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            // Convierte el objeto LocalDate a una cadena con el formato deseado
            out.value(dateOnlyFormatter.format(value));
            //out.value(value.format(dateOnlyFormatter));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek().equals(JsonToken.NULL)) {
            in.nextNull();
            return null;
        }

        // Lee la cadena de texto del JSON
        String dateStr = in.nextString();

        try {
            // Intenta parsear la cadena completa que viene del servidor (ej: "2025-10-29T00:00:00")
            // `parse` puede extraer la parte de la fecha (LocalDate) de una cadena con fecha y hora.
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            // Si el primer intento falla, intenta parsear como si fuera solo una fecha (ej: "2025-10-29")
            try {
                return LocalDate.parse(dateStr, dateOnlyFormatter);
            } catch (DateTimeParseException e2) {
                // Si ambos fallan, lanza una excepción para saber que el formato es inesperado.
                throw new IOException("Failed to parse date: " + dateStr, e2);
            }
        }
    }
}
