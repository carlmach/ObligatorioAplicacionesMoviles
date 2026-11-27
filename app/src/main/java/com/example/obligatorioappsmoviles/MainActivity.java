
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Button;



public class MainActivity extends Activity {

    private TextView txtUltimaReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        Button btnMisRecetas = findViewById(R.id.btnMisRecetas);

        btnMisRecetas.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    MisRecetasActivity.class
            );

            startActivity(intent);
        });


        Button btnBuscar = findViewById(R.id.btnBuscarRecetas);

        btnBuscar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    BuscarRecetasActivity.class
            );

            startActivity(intent);
        });


        Button btnAgregar = findViewById(R.id.btnAgregarReceta);

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AgregarRecetaActivity.class
            );

            startActivity(intent);
        });

        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(v -> {
            Prefs.cerrarSesion(this);

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();
        });

        txtUltimaReceta = findViewById(R.id.txtUltimaReceta);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String ultimaReceta = Prefs.obtenerUltimaReceta(this);

        if (ultimaReceta.isEmpty()) {
            txtUltimaReceta.setText(R.string.ultima_receta);
        } else {
            txtUltimaReceta.setText(
                    getString(R.string.ultima_receta_formato, ultimaReceta)
            );
        }
    }
}
