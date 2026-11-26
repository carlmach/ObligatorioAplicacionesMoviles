
package com.example.obligatorioappsmoviles;

public class RecetaListado {

    private String nombre;
    private String ingredientes;

    public RecetaListado(String nombre, String ingredientes) {
        this.nombre = nombre;
        this.ingredientes = ingredientes;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }
}
