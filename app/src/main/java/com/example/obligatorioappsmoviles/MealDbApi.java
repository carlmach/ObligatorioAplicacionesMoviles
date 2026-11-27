
package com.example.obligatorioappsmoviles;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Cliente de TheMealDB. Todos los métodos hacen red: llamarlos
 * siempre desde {@link Ejecutor}, nunca desde el hilo principal.
 */
public class MealDbApi {

    private static final String BASE = "https://www.themealdb.com/api/json/v1/1/";

    public static ArrayList<String> listarCategorias() throws IOException {
        return listarNombres("list.php?c=list", "strCategory");
    }

    public static ArrayList<String> listarAreas() throws IOException {
        return listarNombres("list.php?a=list", "strArea");
    }

    public static ArrayList<Receta> buscarPorNombre(String nombre) throws IOException {
        return leerRecetas("search.php?s=" + codificar(nombre));
    }

    public static ArrayList<Receta> filtrarPorCategoria(String categoria) throws IOException {
        return leerRecetas("filter.php?c=" + codificar(categoria));
    }

    public static ArrayList<Receta> filtrarPorArea(String area) throws IOException {
        return leerRecetas("filter.php?a=" + codificar(area));
    }

    /**
     * filter.php sólo devuelve id, nombre e imagen. Para tener ingredientes
     * e instrucciones hay que pedir la receta completa por su id.
     */
    public static Receta obtenerPorIdExterno(String idExterno) throws IOException {

        ArrayList<Receta> recetas = leerRecetas("lookup.php?i=" + codificar(idExterno));

        return recetas.isEmpty() ? null : recetas.get(0);
    }

    private static ArrayList<String> listarNombres(String ruta, String campo) throws IOException {

        ArrayList<String> nombres = new ArrayList<>();

        try {
            JSONObject raiz = new JSONObject(pedir(ruta));
            JSONArray lista = raiz.optJSONArray("meals");

            if (lista == null) {
                return nombres;
            }

            for (int i = 0; i < lista.length(); i++) {
                nombres.add(lista.getJSONObject(i).getString(campo));
            }

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        return nombres;
    }

    private static ArrayList<Receta> leerRecetas(String ruta) throws IOException {

        ArrayList<Receta> recetas = new ArrayList<>();

        try {
            JSONObject raiz = new JSONObject(pedir(ruta));
            JSONArray lista = raiz.optJSONArray("meals");

            if (lista == null) {
                return recetas;
            }

            for (int i = 0; i < lista.length(); i++) {
                recetas.add(aReceta(lista.getJSONObject(i)));
            }

        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        return recetas;
    }

    private static Receta aReceta(JSONObject json) {

        Receta receta = new Receta(
                json.optString("strMeal", ""),
                json.optString("strCategory", ""),
                json.optString("strArea", ""),
                armarIngredientes(json),
                json.optString("strInstructions", "")
        );

        receta.setImagenUrl(json.optString("strMealThumb", ""));
        receta.setIdExterno(json.optString("idMeal", ""));

        return receta;
    }

    /**
     * La API no manda una lista de ingredientes: manda 20 pares sueltos
     * strIngredient1..20 / strMeasure1..20, muchos vacíos.
     */
    private static String armarIngredientes(JSONObject json) {

        StringBuilder texto = new StringBuilder();

        for (int i = 1; i <= 20; i++) {

            String ingrediente = json.optString("strIngredient" + i, "").trim();
            String medida = json.optString("strMeasure" + i, "").trim();

            if (ingrediente.isEmpty() || ingrediente.equals("null")) {
                continue;
            }

            if (texto.length() > 0) {
                texto.append("\n");
            }

            texto.append("• ").append(ingrediente);

            if (!medida.isEmpty() && !medida.equals("null")) {
                texto.append(" (").append(medida).append(")");
            }
        }

        return texto.toString();
    }

    private static String pedir(String ruta) throws IOException {

        HttpURLConnection conexion = (HttpURLConnection) new URL(BASE + ruta).openConnection();

        conexion.setConnectTimeout(10000);
        conexion.setReadTimeout(10000);

        try {
            if (conexion.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP " + conexion.getResponseCode());
            }

            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(conexion.getInputStream()));

            StringBuilder respuesta = new StringBuilder();
            String linea;

            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }

            lector.close();

            return respuesta.toString();

        } finally {
            conexion.disconnect();
        }
    }

    private static String codificar(String texto) throws IOException {
        return URLEncoder.encode(texto, "UTF-8");
    }
}
