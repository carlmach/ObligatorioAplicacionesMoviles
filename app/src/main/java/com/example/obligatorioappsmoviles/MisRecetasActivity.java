
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MisRecetasActivity extends Activity {

    private ArrayList<Receta> listaRecetas;
    private RecetaAdapter adapter;
    private TextView txtSinRecetas;
    private RecetaDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_recetas);

        dao = new RecetaDao(this);

        RecyclerView recyclerRecetas =
                findViewById(R.id.recyclerRecetas);

        txtSinRecetas = findViewById(R.id.txtSinRecetas);

        listaRecetas = new ArrayList<>();

        adapter = new RecetaAdapter(listaRecetas, this::abrirDetalle);

        recyclerRecetas.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerRecetas.setAdapter(adapter);

        Button btnVolver =
                findViewById(R.id.btnVolverMisRecetas);

        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();

        listaRecetas.clear();
        listaRecetas.addAll(dao.listarTodas());

        adapter.notifyDataSetChanged();

        txtSinRecetas.setVisibility(
                listaRecetas.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    private void abrirDetalle(Receta receta) {

        Intent intent = new Intent(this, DetalleRecetaActivity.class);
        intent.putExtra(DetalleRecetaActivity.EXTRA_ID_RECETA, receta.getId());

        startActivity(intent);
    }
}
