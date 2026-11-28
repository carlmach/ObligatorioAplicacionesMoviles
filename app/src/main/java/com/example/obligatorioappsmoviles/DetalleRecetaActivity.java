
package com.example.obligatorioappsmoviles;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class DetalleRecetaActivity extends Activity {

    public static final String EXTRA_ID_RECETA = "id_receta";
    public static final String EXTRA_ID_EXTERNO = "id_externo";

    private RecetaDao dao;
    private Receta receta;
    private boolean esPropia;

    private ImageView imgDetalle;
    private TextView txtNombre;
    private TextView txtCategoria;
    private TextView txtIngredientes;
    private TextView txtPreparacion;
    private View bloqueNotas;
    private EditText edtNotas;
    private Button btnGuardarNotas;
    private Button btnEliminar;
    private Button btnAgregarColeccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detalle_receta);

        dao = new RecetaDao(this);

        imgDetalle = findViewById(R.id.imgDetalle);
        txtNombre = findViewById(R.id.txtDetalleNombre);
        txtCategoria = findViewById(R.id.txtDetalleCategoria);
        txtIngredientes = findViewById(R.id.txtDetalleIngredientes);
        txtPreparacion = findViewById(R.id.txtDetallePreparacion);
        bloqueNotas = findViewById(R.id.bloqueNotas);
        edtNotas = findViewById(R.id.edtNotas);
        btnGuardarNotas = findViewById(R.id.btnGuardarNotas);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnAgregarColeccion = findViewById(R.id.btnAgregarColeccion);

        Button btnVolver = findViewById(R.id.btnVolverDetalle);
        btnVolver.setOnClickListener(v -> finish());

        btnGuardarNotas.setOnClickListener(v -> guardarNotas());
        btnEliminar.setOnClickListener(v -> confirmarEliminar());
        btnAgregarColeccion.setOnClickListener(v -> agregarAColeccion());

        String idExterno = getIntent().getStringExtra(EXTRA_ID_EXTERNO);

        if (idExterno != null) {
            esPropia = false;
            cargarDesdeApi(idExterno);
        } else {
            esPropia = true;
            cargarDesdeBase(getIntent().getLongExtra(EXTRA_ID_RECETA, -1));
        }
    }

    private void cargarDesdeBase(long id) {

        receta = dao.obtenerPorId(id);

        if (receta == null) {
            Toast.makeText(this, R.string.error_receta_no_encontrada,
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrar();
    }

    private void cargarDesdeApi(String idExterno) {

        Ejecutor.correr(() -> MealDbApi.obtenerPorIdExterno(idExterno),
                new Ejecutor.Resultado<Receta>() {
                    @Override
                    public void ok(Receta valor) {

                        if (valor == null) {
                            Toast.makeText(DetalleRecetaActivity.this,
                                    R.string.error_receta_no_encontrada,
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }

                        receta = valor;
                        mostrar();
                    }

                    @Override
                    public void error(Exception e) {
                        Toast.makeText(DetalleRecetaActivity.this,
                                R.string.error_conexion,
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void mostrar() {

        txtNombre.setText(receta.getNombre());
        txtCategoria.setText(describirCategoria());
        txtIngredientes.setText(receta.getIngredientes());
        txtPreparacion.setText(receta.getPreparacion());

        String imagen = receta.getImagenUrl();

        if (imagen == null || imagen.isEmpty()) {
            imgDetalle.setVisibility(View.GONE);
        } else {
            imgDetalle.setVisibility(View.VISIBLE);
            Glide.with(this).load(imagen).centerCrop().into(imgDetalle);
        }

        if (esPropia) {
            bloqueNotas.setVisibility(View.VISIBLE);
            btnGuardarNotas.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
            btnAgregarColeccion.setVisibility(View.GONE);

            edtNotas.setText(receta.getNotas() == null ? "" : receta.getNotas());

        } else {
            bloqueNotas.setVisibility(View.GONE);
            btnGuardarNotas.setVisibility(View.GONE);
            btnEliminar.setVisibility(View.GONE);
            btnAgregarColeccion.setVisibility(View.VISIBLE);
        }
    }

    private String describirCategoria() {

        String categoria = receta.getCategoria();
        String area = receta.getArea();

        if (categoria == null || categoria.isEmpty()) {
            return getString(R.string.sin_categoria);
        }

        if (area != null && !area.isEmpty()) {
            return categoria + " · " + area;
        }

        return categoria;
    }

    private void guardarNotas() {

        dao.actualizarNotas(receta.getId(), edtNotas.getText().toString().trim());

        // La letra pide mostrar la última receta ingresada o modificada.
        Prefs.guardarUltimaReceta(this, receta.getNombre());

        Toast.makeText(this, R.string.notas_guardadas, Toast.LENGTH_SHORT).show();
    }

    private void confirmarEliminar() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.eliminar_receta)
                .setMessage(getString(R.string.confirmar_eliminar, receta.getNombre()))
                .setPositiveButton(R.string.eliminar, (dialog, which) -> eliminar())
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    private void eliminar() {

        dao.eliminar(receta.getId());

        Prefs.olvidarUltimaRecetaSiEs(this, receta.getNombre());

        Toast.makeText(this, R.string.receta_eliminada, Toast.LENGTH_SHORT).show();

        finish();
    }

    private void agregarAColeccion() {

        if (dao.existeIdExterno(receta.getIdExterno())) {
            Toast.makeText(this, R.string.receta_repetida, Toast.LENGTH_SHORT).show();
            return;
        }

        if (dao.insertar(receta) == -1) {
            Toast.makeText(this, R.string.error_guardar, Toast.LENGTH_SHORT).show();
            return;
        }

        Prefs.guardarUltimaReceta(this, receta.getNombre());

        Toast.makeText(this, R.string.receta_agregada, Toast.LENGTH_SHORT).show();

        finish();
    }
}
