
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MisRecetasActivity extends Activity {

    private ArrayList<RecetaListado> listaRecetas;
    private RecetaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_recetas);

        RecyclerView recyclerRecetas =
                findViewById(R.id.recyclerRecetas);

        listaRecetas = new ArrayList<>();

        adapter = new RecetaAdapter(listaRecetas);

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

        listaRecetas.add(new RecetaListado(
                "Milanesa napolitana",
                "Carne, queso y tomate"
        ));

        listaRecetas.add(new RecetaListado(
                "Tortilla de papas",
                "Papas, huevo y cebolla"
        ));

        listaRecetas.add(new RecetaListado(
                "Fideos con salsa",
                "Pasta y salsa de tomate"
        ));

        adapter.notifyDataSetChanged();
    }
}
