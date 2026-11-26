
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

public class AgregarRecetaActivity extends Activity {

    private EditText edtNombre;
    private EditText edtIngredientes;
    private EditText edtPreparacion;
    private Button btnValidar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agregar_receta);

        edtNombre = findViewById(R.id.edtNombre);
        edtIngredientes = findViewById(R.id.edtIngredientes);
        edtPreparacion = findViewById(R.id.edtPreparacion);
        btnValidar = findViewById(R.id.btnValidar);

        btnValidar.setOnClickListener(v -> validarReceta());

        Button btnVolverAgregar = findViewById(R.id.btnVolverAgregar);

        btnVolverAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void validarReceta() {

        String nombre = edtNombre.getText().toString().trim();
        String ingredientes = edtIngredientes.getText().toString().trim();
        String preparacion = edtPreparacion.getText().toString().trim();

        if (nombre.isEmpty()) {
            edtNombre.setError(getString(R.string.error_nombre));
            edtNombre.requestFocus();
            return;
        }

        if (ingredientes.isEmpty()) {
            edtIngredientes.setError(getString(R.string.error_ingredientes));
            edtIngredientes.requestFocus();
            return;
        }

        if (preparacion.isEmpty()) {
            edtPreparacion.setError(getString(R.string.error_preparacion));
            edtPreparacion.requestFocus();
            return;
        }

        Toast.makeText(this,
                R.string.formulario_valido,
                Toast.LENGTH_SHORT).show();
    }
}
