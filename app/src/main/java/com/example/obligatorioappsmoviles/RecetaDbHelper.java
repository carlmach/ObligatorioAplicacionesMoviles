
package com.example.obligatorioappsmoviles;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecetaDbHelper extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "mirecetario.db";

    // v3: se quita la tabla de usuarios (ahora es Firebase) y cada receta guarda el uid de su dueño.
    private static final int VERSION_BD = 3;

    public static final String TABLA = "recetas";
    public static final String COL_ID = "id";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_CATEGORIA = "categoria";
    public static final String COL_AREA = "area";
    public static final String COL_INGREDIENTES = "ingredientes";
    public static final String COL_PREPARACION = "preparacion";
    public static final String COL_IMAGEN_URL = "imagen_url";
    public static final String COL_NOTAS = "notas";
    public static final String COL_ID_EXTERNO = "id_externo";
    public static final String COL_UID = "uid_usuario";

    // Nombre de la tabla de la versión anterior, solo para borrarla al actualizar.
    private static final String TABLA_USUARIOS_VIEJA = "usuarios";

    private static final String SQL_CREAR_RECETAS =
            "CREATE TABLE " + TABLA + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_UID + " TEXT NOT NULL, " +
                    COL_NOMBRE + " TEXT NOT NULL, " +
                    COL_CATEGORIA + " TEXT, " +
                    COL_AREA + " TEXT, " +
                    COL_INGREDIENTES + " TEXT NOT NULL, " +
                    COL_PREPARACION + " TEXT NOT NULL, " +
                    COL_IMAGEN_URL + " TEXT, " +
                    COL_NOTAS + " TEXT, " +
                    COL_ID_EXTERNO + " TEXT)";

    public RecetaDbHelper(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_RECETAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionVieja, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USUARIOS_VIEJA);
        onCreate(db);
    }
}
