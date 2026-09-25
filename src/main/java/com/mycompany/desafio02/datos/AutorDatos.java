package com.mycompany.desafio02.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.desafio02.beans.AutorBeans;
import com.mycompany.desafio02.util.Conexion;

public class AutorDatos {

    private static final String SQL_LISTAR =
            "SELECT id_autor, nombre, nacionalidad "
            + "FROM autor "
            + "ORDER BY nombre";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT id_autor, nombre, nacionalidad "
            + "FROM autor "
            + "WHERE id_autor = ?";

    public List<AutorBeans> listarAutores() throws SQLException {
        List<AutorBeans> autores = new ArrayList<>();

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_LISTAR);
            ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                AutorBeans autor = crearAutor(resultado);
                autores.add(autor);
            }
        }

        return autores;
    }

    public AutorBeans buscarPorId(int idAutor) throws SQLException {
        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_BUSCAR_POR_ID)
        ) {
            sentencia.setInt(1, idAutor);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return crearAutor(resultado);
                }
            }
        }

        return null;
    }

    private AutorBeans crearAutor(ResultSet resultado)
            throws SQLException {

        return new AutorBeans(
                resultado.getInt("id_autor"),
                resultado.getString("nombre"),
                resultado.getString("nacionalidad")
        );
    }
}