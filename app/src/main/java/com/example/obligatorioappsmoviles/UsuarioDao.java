
package com.example.obligatorioappsmoviles;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsuarioDao {

    private final RecetaDbHelper helper;

    public UsuarioDao(Context context) {
        helper = new RecetaDbHelper(context.getApplicationContext());
    }

    public boolean existeEmail(String email) {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                RecetaDbHelper.TABLA_USUARIOS,
                new String[]{RecetaDbHelper.COL_USUARIO_ID},
                RecetaDbHelper.COL_EMAIL + " = ?",
                new String[]{email},
                null, null, null
        );

        boolean existe = cursor.getCount() > 0;

        cursor.close();

        return existe;
    }

    public long registrar(String email, String password) {

        ContentValues valores = new ContentValues();

        valores.put(RecetaDbHelper.COL_EMAIL, email);
        valores.put(RecetaDbHelper.COL_PASSWORD, hash(password));

        SQLiteDatabase db = helper.getWritableDatabase();

        return db.insert(RecetaDbHelper.TABLA_USUARIOS, null, valores);
    }

    public boolean validarCredenciales(String email, String password) {

        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                RecetaDbHelper.TABLA_USUARIOS,
                new String[]{RecetaDbHelper.COL_USUARIO_ID},
                RecetaDbHelper.COL_EMAIL + " = ? AND " +
                        RecetaDbHelper.COL_PASSWORD + " = ?",
                new String[]{email, hash(password)},
                null, null, null
        );

        boolean valido = cursor.getCount() > 0;

        cursor.close();

        return valido;
    }

    private String hash(String texto) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(texto.getBytes());

            return String.format("%064x", new BigInteger(1, bytes));

        } catch (NoSuchAlgorithmException e) {
            return texto;
        }
    }
}
