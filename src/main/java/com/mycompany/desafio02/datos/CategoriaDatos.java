package com.mycompany.desafio02.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.desafio02.beans.CategoriaBeans;
import com.mycompany.desafio02.util.Conexion;

public class CategoriaDatos {

    private static final String SQL_LISTAR =
            "SELECT id_categoria, nombre_categoria "
            + "FROM categoria "
            + "ORDER BY nombre_categoria";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT id_categoria, nombre_categoria "
            + "FROM categoria "
            + "WHERE id_categoria = ?";

    public List<CategoriaBeans> listarCategorias()
            throws SQLException {

        List<CategoriaBeans> categorias = new ArrayList<>();

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_LISTAR);
            ResultSet resultado = sentencia.executeQuery()
        ) {
            while (resultado.next()) {
                CategoriaBeans categoria =
                        crearCategoria(resultado);

                categorias.add(categoria);
            }
        }

        return categorias;
    }

    public CategoriaBeans buscarPorId(int idCategoria)
            throws SQLException {

        try (
            Connection conexion = Conexion.getConexion();
            PreparedStatement sentencia =
                    conexion.prepareStatement(SQL_BUSCAR_POR_ID)
        ) {
            sentencia.setInt(1, idCategoria);

            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return crearCategoria(resultado);
                }
            }
        }

        return null;
    }

    private CategoriaBeans crearCategoria(ResultSet resultado)
            throws SQLException {

        return new CategoriaBeans(
                resultado.getInt("id_categoria"),
                resultado.getString("nombre_categoria")
        );
    }
}