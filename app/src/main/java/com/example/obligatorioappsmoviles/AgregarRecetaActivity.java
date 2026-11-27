
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AgregarRecetaActivity extends Activity {

    private static final int LARGO_MINIMO_NOMBRE = 3;
    private static final int LARGO_MINIMO_INGREDIENTES = 5;
    private static final int LARGO_MINIMO_PREPARACION = 10;

    private EditText edtNombre;
    private Spinner spnCategoria;
    private EditText edtIngredientes;
    private EditText edtPreparacion;
    private RecetaDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agregar_receta);

        dao = new RecetaDao(this);

        edtNombre = findViewById(R.id.edtNombre);
        spnCategoria = findViewById(R.id.spnCategoria);
        edtIngredientes = findViewById(R.id.edtIngredientes);
        edtPreparacion = findViewById(R.id.edtPreparacion);

        spnCategoria.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.categorias)
        ));

        Button btnValidar = findViewById(R.id.btnValidar);
        btnValidar.setOnClickListener(v -> validarReceta());

        Button btnVolverAgregar = findViewById(R.id.btnVolverAgregar);
        btnVolverAgregar.setOnClickListener(v -> finish());
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

        if (nombre.length() < LARGO_MINIMO_NOMBRE) {
            edtNombre.setError(getString(R.string.error_nombre_corto,
                    LARGO_MINIMO_NOMBRE));
            edtNombre.requestFocus();
            return;
        }

        if (ingredientes.isEmpty()) {
            edtIngredientes.setError(getString(R.string.error_ingredientes));
            edtIngredientes.requestFocus();
            return;
        }

        if (ingredientes.length() < LARGO_MINIMO_INGREDIENTES) {
            edtIngredientes.setError(getString(R.string.error_ingredientes_corto,
                    LARGO_MINIMO_INGREDIENTES));
            edtIngredientes.requestFocus();
            return;
        }

        if (preparacion.isEmpty()) {
            edtPreparacion.setError(getString(R.string.error_preparacion));
            edtPreparacion.requestFocus();
            return;
        }

        if (preparacion.length() < LARGO_MINIMO_PREPARACION) {
            edtPreparacion.setError(getString(R.string.error_preparacion_corta,
                    LARGO_MINIMO_PREPARACION));
            edtPreparacion.requestFocus();
            return;
        }

        guardarReceta(nombre, ingredientes, preparacion);
    }

    private void guardarReceta(String nombre,
                               String ingredientes,
                               String preparacion) {

        String categoria = spnCategoria.getSelectedItem().toString();

        Receta receta = new Receta(nombre, categoria, "", ingredientes, preparacion);

        if (dao.insertar(receta) == -1) {
            Toast.makeText(this,
                    R.string.error_guardar,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Prefs.guardarUltimaReceta(this, nombre);

        Toast.makeText(this,
                R.string.receta_guardada,
                Toast.LENGTH_SHORT).show();

        finish();
    }
}
