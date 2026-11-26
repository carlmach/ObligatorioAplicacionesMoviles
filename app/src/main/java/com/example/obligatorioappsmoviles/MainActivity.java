
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;

public class MainActivity extends Activity {

    private TextView txtUltimaReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Button btnAgregar = findViewById(R.id.btnAgregarReceta);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AgregarRecetaActivity.class
            );

            startActivity(intent);
        });

        txtUltimaReceta = findViewById(R.id.txtUltimaReceta);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences preferences = getSharedPreferences(
                "MiRecetarioPrefs", MODE_PRIVATE);

        String ultimaReceta = preferences.getString(
                "ultima_receta", "");

        if (ultimaReceta.isEmpty()) {
            txtUltimaReceta.setText(R.string.ultima_receta);
        } else {
            txtUltimaReceta.setText(
                    getString(R.string.ultima_receta_formato, ultimaReceta)
            );
        }
    }
}
