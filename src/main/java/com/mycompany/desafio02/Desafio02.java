/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.desafio02;

import java.sql.SQLException;

import com.mycompany.desafio02.beans.AutorBeans;
import com.mycompany.desafio02.beans.CategoriaBeans;
import com.mycompany.desafio02.beans.LibroBeans;
import com.mycompany.desafio02.datos.AutorDatos;
import com.mycompany.desafio02.datos.CategoriaDatos;
import com.mycompany.desafio02.datos.LibroDatos;
import com.mycompany.desafio02.util.Conexion;

public class Desafio02 {

    public static void main(String[] args) {
        if (!Conexion.probarConexion()) {
            System.out.println(
                    "Revise el usuario, la contrasena y el servicio MySQL."
            );
            return;
        }

        System.out.println("Conexion exitosa con biblioteca_db.");

        try {
            AutorDatos autorDatos = new AutorDatos();
            CategoriaDatos categoriaDatos =
                    new CategoriaDatos();
            LibroDatos libroDatos = new LibroDatos();

            System.out.println("\nAUTORES");

            for (AutorBeans autor : autorDatos.listarAutores()) {
                System.out.println(
                        autor.getIdAutor()
                        + " - "
                        + autor.getNombre()
                );
            }

            System.out.println("\nCATEGORIAS");

            for (
                CategoriaBeans categoria
                : categoriaDatos.listarCategorias()
            ) {
                System.out.println(
                        categoria.getIdCategoria()
                        + " - "
                        + categoria.getNombreCategoria()
                );
            }

            System.out.println("\nLIBROS");

            for (LibroBeans libro : libroDatos.listarLibros()) {
                System.out.println(
                        libro.getIdLibro()
                        + " - "
                        + libro.getTitulo()
                        + " - "
                        + libro.getNombreAutor()
                        + " - "
                        + libro.getNombreCategoria()
                );
            }
        } catch (SQLException e) {
            System.err.println(
                    "Error al consultar la base de datos: "
                    + e.getMessage()
            );
        }
    }
}