package com.mycompany.desafio02.beans;

public class LibroBeans {

    private int idLibro;
    private String titulo;
    private int anioPublicacion;
    private int idAutor;
    private int idCategoria;

    /*
     * Estos atributos se utilizan para mostrar los nombres obtenidos
     * mediante JOIN, sin realizar nuevas consultas desde la interfaz.
     */
    private String nombreAutor;
    private String nombreCategoria;

    public LibroBeans() {
    }

    public LibroBeans(
            int idLibro,
            String titulo,
            int anioPublicacion,
            int idAutor,
            int idCategoria
    ) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    public LibroBeans(
            int idLibro,
            String titulo,
            int anioPublicacion,
            int idAutor,
            int idCategoria,
            String nombreAutor,
            String nombreCategoria
    ) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
        this.nombreAutor = nombreAutor;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}