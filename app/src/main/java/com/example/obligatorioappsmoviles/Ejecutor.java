
package com.example.obligatorioappsmoviles;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Corre la descarga en un hilo trabajador y devuelve el resultado
 * en el hilo principal, que es el único que puede tocar las vistas.
 */
public class Ejecutor {

    public interface Tarea<T> {
        T enSegundoPlano() throws Exception;
    }

    public interface Resultado<T> {
        void ok(T valor);
        void error(Exception e);
    }

    private static final ExecutorService POOL = Executors.newFixedThreadPool(2);
    private static final Handler PRINCIPAL = new Handler(Looper.getMainLooper());

    public static <T> void correr(Tarea<T> tarea, Resultado<T> resultado) {

        POOL.execute(() -> {
            try {
                T valor = tarea.enSegundoPlano();
                PRINCIPAL.post(() -> resultado.ok(valor));

            } catch (Exception e) {
                PRINCIPAL.post(() -> resultado.error(e));
            }
        });
    }
}
