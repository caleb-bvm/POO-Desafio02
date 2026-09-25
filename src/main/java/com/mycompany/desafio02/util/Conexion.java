package com.mycompany.desafio02.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Conexion {

    private static final String URL =
            "jdbc:mysql://localhost:3306/biblioteca_db"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=America/Guatemala";

    private static final String USUARIO = "root";

    // Cambiar por la contraseña de MySQL de la computadora.
    private static final String CONTRASENA = "";

    private Conexion() {
        // Evita que se creen objetos de esta clase.
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(
                URL,
                USUARIO,
                CONTRASENA
        );
    }

    public static boolean probarConexion() {
        try (Connection conexion = getConexion()) {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            System.err.println(
                    "No fue posible conectarse a MySQL: "
                    + e.getMessage()
            );
            return false;
        }
    }
}