
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class BuscarRecetasActivity extends Activity {

    private EditText edtBuscar;
    private Spinner spnCategoria;
    private Spinner spnArea;
    private ProgressBar progreso;
    private TextView txtSinResultados;
    private ArrayList<Receta> resultados;
    private RecetaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buscar_recetas);

        edtBuscar = findViewById(R.id.edtBuscar);
        spnCategoria = findViewById(R.id.spnCategoriaBuscar);
        spnArea = findViewById(R.id.spnAreaBuscar);
        progreso = findViewById(R.id.progresoBuscar);
        txtSinResultados = findViewById(R.id.txtSinResultados);

        RecyclerView recyclerResultados = findViewById(R.id.recyclerResultados);

        resultados = new ArrayList<>();

        adapter = new RecetaAdapter(resultados, this::abrirDetalle);

        recyclerResultados.setLayoutManager(new LinearLayoutManager(this));
        recyclerResultados.setAdapter(adapter);

        Button btnBuscar = findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(v -> buscar());

        Button btnVolver = findViewById(R.id.btnVolverBuscar);
        btnVolver.setOnClickListener(v -> finish());

        cargarFiltros();
    }


    private void cargarFiltros() {

        Ejecutor.correr(MealDbApi::listarCategorias, new Ejecutor.Resultado<ArrayList<String>>() {
            @Override
            public void ok(ArrayList<String> categorias) {
                llenarSpinner(spnCategoria, categorias, R.string.todas_las_categorias);
            }

            @Override
            public void error(Exception e) {
                llenarSpinner(spnCategoria, new ArrayList<>(), R.string.todas_las_categorias);
            }
        });

        Ejecutor.correr(MealDbApi::listarAreas, new Ejecutor.Resultado<ArrayList<String>>() {
            @Override
            public void ok(ArrayList<String> areas) {
                llenarSpinner(spnArea, areas, R.string.todas_las_areas);
            }

            @Override
            public void error(Exception e) {
                llenarSpinner(spnArea, new ArrayList<>(), R.string.todas_las_areas);
            }
        });
    }

    private void llenarSpinner(Spinner spinner, ArrayList<String> valores, int idOpcionTodas) {

        ArrayList<String> items = new ArrayList<>();
        items.add(getString(idOpcionTodas));
        items.addAll(valores);

        spinner.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                items
        ));
    }

    private void buscar() {

        String nombre = edtBuscar.getText().toString().trim();
        String categoria = seleccion(spnCategoria, R.string.todas_las_categorias);
        String area = seleccion(spnArea, R.string.todas_las_areas);

        if (nombre.isEmpty() && categoria == null && area == null) {
            edtBuscar.setError(getString(R.string.error_buscar_vacio));
            edtBuscar.requestFocus();
            return;
        }

        mostrarProgreso(true);

        Ejecutor.Tarea<ArrayList<Receta>> tarea;

        if (!nombre.isEmpty()) {
            tarea = () -> MealDbApi.buscarPorNombre(nombre);
        } else if (categoria != null) {
            tarea = () -> MealDbApi.filtrarPorCategoria(categoria);
        } else {
            tarea = () -> MealDbApi.filtrarPorArea(area);
        }

        Ejecutor.correr(tarea, new Ejecutor.Resultado<ArrayList<Receta>>() {
            @Override
            public void ok(ArrayList<Receta> recetas) {
                mostrarProgreso(false);
                mostrarResultados(recetas);
            }

            @Override
            public void error(Exception e) {
                mostrarProgreso(false);
                Toast.makeText(BuscarRecetasActivity.this,
                        R.string.error_conexion,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private String seleccion(Spinner spinner, int idOpcionTodas) {

        if (spinner.getSelectedItem() == null) {
            return null;
        }

        String valor = spinner.getSelectedItem().toString();

        return valor.equals(getString(idOpcionTodas)) ? null : valor;
    }

    private void mostrarResultados(ArrayList<Receta> recetas) {

        resultados.clear();
        resultados.addAll(recetas);

        adapter.notifyDataSetChanged();

        txtSinResultados.setVisibility(
                resultados.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    private void mostrarProgreso(boolean visible) {

        progreso.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (visible) {
            txtSinResultados.setVisibility(View.GONE);
        }
    }

    private void abrirDetalle(Receta receta) {

        Intent intent = new Intent(this, DetalleRecetaActivity.class);
        intent.putExtra(DetalleRecetaActivity.EXTRA_ID_EXTERNO, receta.getIdExterno());

        startActivity(intent);
    }
}
