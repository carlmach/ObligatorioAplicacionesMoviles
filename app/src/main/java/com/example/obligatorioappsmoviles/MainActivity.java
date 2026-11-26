
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView txtUltimaReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cargamos el diseño de la pantalla principal
        setContentView(R.layout.activity_main);

        // Asociamos el componente Java con el XML
        txtUltimaReceta = findViewById(R.id.txtUltimaReceta);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Recuperamos las preferencias guardadas
        SharedPreferences preferences = getSharedPreferences(
                "MiRecetarioPrefs", MODE_PRIVATE);

        String ultimaReceta = preferences.getString(
                "ultima_receta", "");

        // Mostramos la última receta guardada
        if (ultimaReceta.isEmpty()) {
            txtUltimaReceta.setText(R.string.ultima_receta);
        } else {
            txtUltimaReceta.setText(
                    getString(R.string.ultima_receta_formato, ultimaReceta)
            );
        }
    }
}
