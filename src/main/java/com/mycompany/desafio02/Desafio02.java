/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.desafio02;

import com.mycompany.desafio02.util.Conexion;
import com.mycompany.desafio02.vista.FrmBiblioteca;
import javax.swing.JOptionPane;

public class Desafio02 {

    public static void main(String[] args) {
        if (!Conexion.probarConexion()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No fue posible conectar con MySQL.\n"
                    + "Revise el servicio, el usuario y la contraseña.",
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrmBiblioteca formulario = new FrmBiblioteca();
                formulario.setVisible(true);
            }
        });
    }
}
