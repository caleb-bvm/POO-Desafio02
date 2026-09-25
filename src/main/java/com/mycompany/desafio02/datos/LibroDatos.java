package com.mycompany.desafio02.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.desafio02.beans.LibroBeans;
import com.mycompany.desafio02.util.Conexion;

public class LibroDatos {

    private static final String SQL_INSERTAR =
            "INSERT INTO libro "
            + "(titulo, anio_publicacion, id_autor, id_categoria) "
            + "VALUES (?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR =
            "UPDATE libro "
            + "SET titulo = ?, "
            + "anio_publicacion = ?, "
            + "id_autor = ?, "
            + "id_categoria = ? "
            + "WHERE id_libro = ?";

    private static final String SQL_ELIMINAR =
            "DELETE FROM libro "
            + "WHERE id_libro = ?";

    private static final String SQL_LISTAR =
            "SELECT "
            + "l.id_libro, "
            + "l.titulo, "
            + "l.anio_publicacion, "
            + "l.id_autor, "
            + "l.id_categoria, "
            + "a.nombre AS nombre_autor, "
            + "c.nombre_categoria "
            + "FROM libro l "
            + "INNER JOIN autor a "
            + "ON l.id_autor = a.id_autor "
            + "INNER JOIN categoria c "
            + "ON l.id_categoria = c.id_categoria "
            + "ORDER BY l.id_libro";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT "
            + "l.id_libro, "
            + "l.titulo, "
            + "l.anio_publicacion, "
            + "l.id_autor, "
            + "l.id_categoria, "
            + "a.nombre AS nombre_autor, "
            + "c.nombre_categoria "
            + "FROM libro l "
            + "INNER JOIN autor a "
            + "ON l.id_autor = a.id_autor "
            + "INNER JOIN categoria c "
            + "ON l.id_categoria = c.id_categoria "
            + "WHERE l.id_libro = ?";

    private static final String SQL_FILTRAR_AUTOR =
            "SELECT "
            + "l.id_libro, "
            + "l.titulo, "
            + "l.anio_publicacion, "
            + "l.id_autor, "
            + "l.id_categoria, "
            + "a.nombre AS nombre_autor, "
            + "c.nombre_categoria "
            + "FROM libro l "
            + "INNER JOIN autor a "
            + "ON l.id_autor = a.id_autor "
            + "INNER JOIN categoria c "
            + "ON l.id_categoria = c.id_categoria "
            + "WHERE l.id_autor = ? "
            + "ORDER BY l.titulo";

    private static final String SQL_FILTRAR_CATEGORIA =
            "SELECT "
            + "l.id_libro, "
            + "l.titulo, "
            + "l.anio_publicacion, "
            + "l.id_autor, "
            + "l.id_categoria, "
            + "a.nombre AS nombre_autor, "
            + "c.nombre_categoria "
            + "FROM libro l "
            + "INNER JOIN autor a "
            + "ON l.id_autor = a.id_autor "
            + "INNER JOIN categoria c "
            + "ON l.id_categoria = c.id_categoria "
            + "WHERE l.id_categoria = ? "
            + "ORDER BY l.titulo";

    public boolean insertar(LibroBeans libro) throws SQLException {
        validarLibro(libro);

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia = conexion.prepareStatement(
                    SQL_INSERTAR,
                    Statement.RETURN_GENERATED_KEYS
            )
        ) {
            asignarDatos(sentencia, libro);

            int filasAfectadas = sentencia.executeUpdate();

            if (filasAfectadas > 0) {
                asignarIdGenerado(sentencia, libro);
                return true;
            }

            return false;
        }
    }

    public boolean actualizar(LibroBeans libro)
            throws SQLException {

        validarLibro(libro);

        if (libro.getIdLibro() <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del libro no es valido."
            );
        }

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_ACTUALIZAR)
        ) {
            asignarDatos(sentencia, libro);
            sentencia.setInt(5, libro.getIdLibro());

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idLibro) throws SQLException {
        if (idLibro <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del libro no es valido."
            );
        }

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_ELIMINAR)
        ) {
            sentencia.setInt(1, idLibro);

            return sentencia.executeUpdate() > 0;
        }
    }

    public List<LibroBeans> listarLibros()
            throws SQLException {

        List<LibroBeans> libros = new ArrayList<>();

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_LISTAR);
            ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                libros.add(crearLibro(resultado));
            }
        }

        return libros;
    }

    public LibroBeans buscarPorId(int idLibro)
            throws SQLException {

        if (idLibro <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del libro no es valido."
            );
        }

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_BUSCAR_POR_ID)
        ) {
            sentencia.setInt(1, idLibro);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return crearLibro(resultado);
                }
            }
        }

        return null;
    }

    public List<LibroBeans> filtrarPorAutor(int idAutor)
            throws SQLException {

        if (idAutor <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del autor no es valido."
            );
        }

        return ejecutarFiltro(SQL_FILTRAR_AUTOR, idAutor);
    }

    public List<LibroBeans> filtrarPorCategoria(
            int idCategoria
    ) throws SQLException {

        if (idCategoria <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la categoria no es valido."
            );
        }

        return ejecutarFiltro(
                SQL_FILTRAR_CATEGORIA,
                idCategoria
        );
    }

    private List<LibroBeans> ejecutarFiltro(
            String consulta,
            int parametro
    ) throws SQLException {

        List<LibroBeans> libros = new ArrayList<>();

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(consulta)
        ) {
            sentencia.setInt(1, parametro);

            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    libros.add(crearLibro(resultado));
                }
            }
        }

        return libros;
    }

    private void asignarDatos(
            PreparedStatement sentencia,
            LibroBeans libro
    ) throws SQLException {

        sentencia.setString(1, libro.getTitulo().trim());
        sentencia.setInt(2, libro.getAnioPublicacion());
        sentencia.setInt(3, libro.getIdAutor());
        sentencia.setInt(4, libro.getIdCategoria());
    }

    private void asignarIdGenerado(
            PreparedStatement sentencia,
            LibroBeans libro
    ) throws SQLException {

        try (ResultSet claves = sentencia.getGeneratedKeys()) {
            if (claves.next()) {
                libro.setIdLibro(claves.getInt(1));
            }
        }
    }

    private LibroBeans crearLibro(ResultSet resultado)
            throws SQLException {

        return new LibroBeans(
                resultado.getInt("id_libro"),
                resultado.getString("titulo"),
                resultado.getInt("anio_publicacion"),
                resultado.getInt("id_autor"),
                resultado.getInt("id_categoria"),
                resultado.getString("nombre_autor"),
                resultado.getString("nombre_categoria")
        );
    }

    private void validarLibro(LibroBeans libro) {
        if (libro == null) {
            throw new IllegalArgumentException(
                    "Debe proporcionar un libro."
            );
        }

        if (
            libro.getTitulo() == null
            || libro.getTitulo().isBlank()
        ) {
            throw new IllegalArgumentException(
                    "El titulo del libro es obligatorio."
            );
        }

        if (libro.getTitulo().trim().length() > 150) {
            throw new IllegalArgumentException(
                    "El titulo no puede superar 150 caracteres."
            );
        }

        if (
            libro.getAnioPublicacion() < 1000
            || libro.getAnioPublicacion() > 9999
        ) {
            throw new IllegalArgumentException(
                    "El anio de publicacion no es valido."
            );
        }

        if (libro.getIdAutor() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un autor."
            );
        }

        if (libro.getIdCategoria() <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una categoria."
            );
        }
    }
}