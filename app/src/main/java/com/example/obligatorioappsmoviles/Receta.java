
package com.example.obligatorioappsmoviles;

public class Receta {

    private long id;
    private String nombre;
    private String categoria;
    private String area;
    private String ingredientes;
    private String preparacion;
    private String imagenUrl;
    private String notas;
    private String idExterno;

    public Receta(String nombre, String categoria, String area,
                  String ingredientes, String preparacion) {

        this.nombre = nombre;
        this.categoria = categoria;
        this.area = area;
        this.ingredientes = ingredientes;
        this.preparacion = preparacion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getArea() {
        return area;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getIdExterno() {
        return idExterno;
    }

    public void setIdExterno(String idExterno) {
        this.idExterno = idExterno;
    }
}
