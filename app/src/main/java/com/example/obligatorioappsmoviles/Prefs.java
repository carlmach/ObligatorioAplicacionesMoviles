
package com.example.obligatorioappsmoviles;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {

    private static final String ARCHIVO = "MiRecetarioPrefs";
    private static final String CLAVE_ULTIMA_RECETA = "ultima_receta";
    private static final String CLAVE_EMAIL = "email_usuario";

    public static void guardarUltimaReceta(Context context, String nombre) {

        preferencias(context)
                .edit()
                .putString(CLAVE_ULTIMA_RECETA, nombre)
                .apply();
    }

    public static String obtenerUltimaReceta(Context context) {

        return preferencias(context)
                .getString(CLAVE_ULTIMA_RECETA, "");
    }

    /** Si borran la receta que figuraba como última, el dato deja de tener sentido. */
    public static void olvidarUltimaRecetaSiEs(Context context, String nombre) {

        if (nombre.equals(obtenerUltimaReceta(context))) {
            preferencias(context)
                    .edit()
                    .remove(CLAVE_ULTIMA_RECETA)
                    .apply();
        }
    }

    public static void guardarSesion(Context context, String email) {

        preferencias(context)
                .edit()
                .putString(CLAVE_EMAIL, email)
                .apply();
    }

    public static String obtenerSesion(Context context) {

        return preferencias(context)
                .getString(CLAVE_EMAIL, "");
    }

    public static void cerrarSesion(Context context) {

        preferencias(context)
                .edit()
                .remove(CLAVE_EMAIL)
                .apply();
    }

    private static SharedPreferences preferencias(Context context) {

        return context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
    }
}
