
package com.example.obligatorioappsmoviles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class RecetaDao {

    private final RecetaDbHelper helper;

    public RecetaDao(Context context) {
        helper = new RecetaDbHelper(context.getApplicationContext());
    }

    public long insertar(Receta receta) {

        ContentValues valores = new ContentValues();

        valores.put(RecetaDbHelper.COL_UID, uidActual());
        valores.put(RecetaDbHelper.COL_NOMBRE, receta.getNombre());
        valores.put(RecetaDbHelper.COL_CATEGORIA, receta.getCategoria());
        valores.put(RecetaDbHelper.COL_AREA, receta.getArea());
        valores.put(RecetaDbHelper.COL_INGREDIENTES, receta.getIngredientes());
        valores.put(RecetaDbHelper.COL_PREPARACION, receta.getPreparacion());
        valores.put(RecetaDbHelper.COL_IMAGEN_URL, receta.getImagenUrl());
        valores.put(RecetaDbHelper.COL_NOTAS, receta.getNotas());
        valores.put(RecetaDbHelper.COL_ID_EXTERNO, receta.getIdExterno());

        SQLiteDatabase db = helper.getWritableDatabase();

        return db.insert(RecetaDbHelper.TABLA, null, valores);
    }

    public ArrayList<Receta> listarTodas() {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                RecetaDbHelper.TABLA,
                null,
                RecetaDbHelper.COL_UID + " = ?",
                new String[]{uidActual()},
                null, null,
                RecetaDbHelper.COL_ID + " DESC"
        );

        return leerTodas(cursor);
    }

    public Receta obtenerPorId(long id) {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                RecetaDbHelper.TABLA,
                null,
                RecetaDbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        Receta receta = null;

        if (cursor.moveToFirst()) {
            receta = leerFila(cursor);
        }

        cursor.close();

        return receta;
    }

    public boolean existeIdExterno(String idExterno) {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                RecetaDbHelper.TABLA,
                new String[]{RecetaDbHelper.COL_ID},
                RecetaDbHelper.COL_ID_EXTERNO + " = ? AND " +
                        RecetaDbHelper.COL_UID + " = ?",
                new String[]{idExterno, uidActual()},
                null, null, null
        );

        boolean existe = cursor.getCount() > 0;

        cursor.close();

        return existe;
    }

    public void actualizarNotas(long id, String notas) {

        ContentValues valores = new ContentValues();
        valores.put(RecetaDbHelper.COL_NOTAS, notas);

        SQLiteDatabase db = helper.getWritableDatabase();

        db.update(
                RecetaDbHelper.TABLA,
                valores,
                RecetaDbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public void eliminar(long id) {

        SQLiteDatabase db = helper.getWritableDatabase();

        db.delete(
                RecetaDbHelper.TABLA,
                RecetaDbHelper.COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    /** uid del usuario logueado en Firebase; vacío si no hay sesión. */
    private String uidActual() {

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();

        return usuario == null ? "" : usuario.getUid();
    }

    private ArrayList<Receta> leerTodas(Cursor cursor) {

        ArrayList<Receta> recetas = new ArrayList<>();

        while (cursor.moveToNext()) {
            recetas.add(leerFila(cursor));
        }

        cursor.close();

        return recetas;
    }

    private Receta leerFila(Cursor cursor) {

        Receta receta = new Receta(
                cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_NOMBRE)),
                cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_CATEGORIA)),
                cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_AREA)),
                cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_INGREDIENTES)),
                cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_PREPARACION))
        );

        receta.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_ID)));
        receta.setImagenUrl(cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_IMAGEN_URL)));
        receta.setNotas(cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_NOTAS)));
        receta.setIdExterno(cursor.getString(cursor.getColumnIndexOrThrow(RecetaDbHelper.COL_ID_EXTERNO)));

        return receta;
    }
}
