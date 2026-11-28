
package com.example.obligatorioappsmoviles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecetaAdapter extends RecyclerView.Adapter<RecetaAdapter.ViewHolderRecetas> {

    public interface AlTocarReceta {
        void onReceta(Receta receta);
    }

    private final ArrayList<Receta> listaItems;
    private final AlTocarReceta listener;

    public RecetaAdapter(ArrayList<Receta> lista, AlTocarReceta listener) {
        this.listaItems = lista;
        this.listener = listener;
    }

    public class ViewHolderRecetas extends RecyclerView.ViewHolder {

        private final ImageView imgReceta;
        private final TextView txtNombre;
        private final TextView txtIngredientes;

        public ViewHolderRecetas(View itemView) {
            super(itemView);

            imgReceta = itemView.findViewById(R.id.imgReceta);
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

        Receta receta = listaItems.get(position);

        holder.txtNombre.setText(receta.getNombre());
        holder.txtIngredientes.setText(subtitulo(receta));

        String imagen = receta.getImagenUrl();

        if (imagen == null || imagen.isEmpty()) {
            holder.imgReceta.setVisibility(View.GONE);
        } else {
            holder.imgReceta.setVisibility(View.VISIBLE);
            Glide.with(holder.imgReceta.getContext())
                    .load(imagen)
                    .centerCrop()
                    .into(holder.imgReceta);
        }

        holder.itemView.setOnClickListener(v -> listener.onReceta(receta));
    }


    private String subtitulo(Receta receta) {

        String categoria = receta.getCategoria();
        String area = receta.getArea();

        if (categoria != null && !categoria.isEmpty()) {

            if (area != null && !area.isEmpty()) {
                return categoria + " · " + area;
            }

            return categoria;
        }

        return receta.getIngredientes();
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }
}
