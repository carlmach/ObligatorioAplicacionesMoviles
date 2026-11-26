
package com.example.obligatorioappsmoviles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.ViewHolderRecetas> {

    private ArrayList<RecetaListado> listaItems;

    public RecetaAdapter(ArrayList<RecetaListado> lista) {
        this.listaItems = lista;
    }


    public class ViewHolderRecetas extends RecyclerView.ViewHolder {

        private TextView txtNombre;
        private TextView txtIngredientes;

        public ViewHolderRecetas(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.txtNombreReceta);
            txtIngredientes = itemView.findViewById(R.id.txtIngredientesReceta);
        }
    }

    @NonNull
    @Override
    public ViewHolderRecetas onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receta, parent, false);

        return new ViewHolderRecetas(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolderRecetas holder, int position) {

        RecetaListado receta = listaItems.get(position);

        holder.txtNombre.setText(receta.getNombre());
        holder.txtIngredientes.setText(receta.getIngredientes());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        receta.getNombre(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }
}
