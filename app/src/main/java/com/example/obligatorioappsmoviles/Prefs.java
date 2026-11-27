
package com.example.obligatorioappsmoviles;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SharedPreferences de la app. La sesión ya no se guarda acá: la maneja Firebase.
 * La última receta se guarda por usuario, para que cada cuenta vea la suya.
 */
public class Prefs {

    private static final String ARCHIVO = "MiRecetarioPrefs";
    private static final String CLAVE_ULTIMA_RECETA = "ultima_receta";

    public static void guardarUltimaReceta(Context context, String nombre) {

        preferencias(context)
                .edit()
                .putString(claveUltimaReceta(), nombre)
                .apply();
    }

    public static String obtenerUltimaReceta(Context context) {

        return preferencias(context)
                .getString(claveUltimaReceta(), "");
    }

    /** Si borran la receta que figuraba como última, el dato deja de tener sentido. */
    public static void olvidarUltimaRecetaSiEs(Context context, String nombre) {

        if (nombre.equals(obtenerUltimaReceta(context))) {
            preferencias(context)
                    .edit()
                    .remove(claveUltimaReceta())
                    .apply();
        }
    }

    private static String claveUltimaReceta() {

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        return usuario == null
                ? CLAVE_ULTIMA_RECETA
                : CLAVE_ULTIMA_RECETA + "_" + usuario.getUid();
    }

    private static SharedPreferences preferencias(Context context) {

        return context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
    }
}
