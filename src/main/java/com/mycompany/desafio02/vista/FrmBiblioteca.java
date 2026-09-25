/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.desafio02.vista;
import com.mycompany.desafio02.beans.AutorBeans;
import com.mycompany.desafio02.beans.CategoriaBeans;
import com.mycompany.desafio02.beans.LibroBeans;
import com.mycompany.desafio02.datos.AutorDatos;
import com.mycompany.desafio02.datos.CategoriaDatos;
import com.mycompany.desafio02.datos.LibroDatos;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author caleb
 */
public class FrmBiblioteca extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmBiblioteca.class.getName());
    
    
    private LibroDatos libroDatos;
    private AutorDatos autorDatos;
    private CategoriaDatos categoriaDatos;

    private List<AutorBeans> listaAutores;
    private List<CategoriaBeans> listaCategorias;

    private DefaultTableModel modeloTabla;

    private int idLibroSeleccionado;
    private boolean cambiandoFiltros;
    
    /**
     * Creates new form FrmBiblioteca
     */
    public FrmBiblioteca() {
    initComponents();

    libroDatos = new LibroDatos();
    autorDatos = new AutorDatos();
    categoriaDatos = new CategoriaDatos();

    listaAutores = new ArrayList<>();
    listaCategorias = new ArrayList<>();

    idLibroSeleccionado = 0;
    cambiandoFiltros = false;

    configurarTabla();
    cargarDatos();
    limpiarFormulario();

    setLocationRelativeTo(null);
    }
    
    
    private void configurarTabla() {
    String[] columnas = {
        "ID",
        "Título",
        "Año",
        "Autor",
        "Categoría"
    };

    modeloTabla = new DefaultTableModel(columnas, 0);

    tablaLibros.setModel(modeloTabla);
    tablaLibros.setDefaultEditor(Object.class, null);
    tablaLibros.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
    );
    }
    
    private void cargarDatos() {
    try {
        cargarAutores();
        cargarCategorias();
        cargarLibros();
    } catch (SQLException e) {
        mostrarError(
                "No fue posible cargar los datos.",
                e
        );
    }
}
    
    private void cargarAutores() throws SQLException {
    listaAutores = autorDatos.listarAutores();

    DefaultComboBoxModel<String> modeloAutores =
            new DefaultComboBoxModel<>();

    DefaultComboBoxModel<String> modeloFiltro =
            new DefaultComboBoxModel<>();

    modeloFiltro.addElement("Todos");

    for (AutorBeans autor : listaAutores) {
        modeloAutores.addElement(autor.getNombre());
        modeloFiltro.addElement(autor.getNombre());
    }

    cambiandoFiltros = true;

    cmbAutor.setModel(modeloAutores);
    cmbFiltroAutor.setModel(modeloFiltro);
    cmbFiltroAutor.setSelectedIndex(0);

    cambiandoFiltros = false;
}
    
    private void cargarCategorias() throws SQLException {
    listaCategorias =
            categoriaDatos.listarCategorias();

    DefaultComboBoxModel<String> modeloCategorias =
            new DefaultComboBoxModel<>();

    DefaultComboBoxModel<String> modeloFiltro =
            new DefaultComboBoxModel<>();

    modeloFiltro.addElement("Todas");

    for (CategoriaBeans categoria : listaCategorias) {
        modeloCategorias.addElement(
                categoria.getNombreCategoria()
        );

        modeloFiltro.addElement(
                categoria.getNombreCategoria()
        );
    }

    cambiandoFiltros = true;

    cmbCategoria.setModel(modeloCategorias);
    cmbFiltroCategoria.setModel(modeloFiltro);
    cmbFiltroCategoria.setSelectedIndex(0);

    cambiandoFiltros = false;
}
    
    private void cargarLibros() throws SQLException {
    List<LibroBeans> libros =
            libroDatos.listarLibros();

    llenarTabla(libros);
}
    
    private void llenarTabla(List<LibroBeans> libros) {
    modeloTabla.setRowCount(0);

    for (LibroBeans libro : libros) {
        Object[] fila = {
            libro.getIdLibro(),
            libro.getTitulo(),
            libro.getAnioPublicacion(),
            libro.getNombreAutor(),
            libro.getNombreCategoria()
        };

        modeloTabla.addRow(fila);
    }
}
    
    
    private LibroBeans obtenerLibroFormulario() {
    String titulo = txtTitulo.getText().trim();
    String textoAnio = txtAnio.getText().trim();

    if (titulo.isEmpty()) {
        throw new IllegalArgumentException(
                "Ingrese el título del libro."
        );
    }

    if (titulo.length() > 150) {
        throw new IllegalArgumentException(
                "El título no puede superar 150 caracteres."
        );
    }

    if (textoAnio.isEmpty()) {
        throw new IllegalArgumentException(
                "Ingrese el año de publicación."
        );
    }

    int anio;

    try {
        anio = Integer.parseInt(textoAnio);
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException(
                "El año debe ser un número entero."
        );
    }

    if (anio < 1000 || anio > 9999) {
        throw new IllegalArgumentException(
                "Ingrese un año válido de cuatro dígitos."
        );
    }

    int posicionAutor = cmbAutor.getSelectedIndex();
    int posicionCategoria =
            cmbCategoria.getSelectedIndex();

    if (
        posicionAutor < 0
        || posicionAutor >= listaAutores.size()
    ) {
        throw new IllegalArgumentException(
                "Seleccione un autor."
        );
    }

    if (
        posicionCategoria < 0
        || posicionCategoria >= listaCategorias.size()
    ) {
        throw new IllegalArgumentException(
                "Seleccione una categoría."
        );
    }

    AutorBeans autor =
            listaAutores.get(posicionAutor);

    CategoriaBeans categoria =
            listaCategorias.get(posicionCategoria);

    LibroBeans libro = new LibroBeans();

    libro.setTitulo(titulo);
    libro.setAnioPublicacion(anio);
    libro.setIdAutor(autor.getIdAutor());
    libro.setIdCategoria(
            categoria.getIdCategoria()
    );

    return libro;
}
    
    private void guardarLibro() {
    try {
        LibroBeans libro = obtenerLibroFormulario();

        boolean guardado = libroDatos.insertar(libro);

        if (guardado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro registrado correctamente."
            );

            restablecerFiltros();
            cargarLibros();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible registrar el libro.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Datos incorrectos",
                JOptionPane.WARNING_MESSAGE
        );
    } catch (SQLException e) {
        mostrarError(
                "Error al registrar el libro.",
                e
        );
    }
}
    
    private void editarLibro() {
    if (idLibroSeleccionado == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Seleccione un libro de la tabla."
        );

        return;
    }

    try {
        LibroBeans libro = obtenerLibroFormulario();

        libro.setIdLibro(idLibroSeleccionado);

        boolean editado =
                libroDatos.actualizar(libro);

        if (editado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente."
            );

            restablecerFiltros();
            cargarLibros();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible actualizar el libro."
            );
        }
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(
                this,
                e.getMessage(),
                "Datos incorrectos",
                JOptionPane.WARNING_MESSAGE
        );
    } catch (SQLException e) {
        mostrarError(
                "Error al actualizar el libro.",
                e
        );
    }
}
    
    private void eliminarLibro() {
    if (idLibroSeleccionado == 0) {
        JOptionPane.showMessageDialog(
                this,
                "Seleccione un libro de la tabla."
        );

        return;
    }

    int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar el libro?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
    );

    if (respuesta != JOptionPane.YES_OPTION) {
        return;
    }

    try {
        boolean eliminado =
                libroDatos.eliminar(idLibroSeleccionado);

        if (eliminado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente."
            );

            restablecerFiltros();
            cargarLibros();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No fue posible eliminar el libro."
            );
        }
    } catch (SQLException e) {
        mostrarError(
                "Error al eliminar el libro.",
                e
        );
    }
}
    
    private void seleccionarLibro() {
    int fila = tablaLibros.getSelectedRow();

    if (fila == -1) {
        return;
    }

    idLibroSeleccionado = Integer.parseInt(
            modeloTabla.getValueAt(fila, 0).toString()
    );

    try {
        LibroBeans libro =
                libroDatos.buscarPorId(
                        idLibroSeleccionado
                );

        if (libro == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "El libro ya no existe."
            );

            cargarLibros();
            limpiarFormulario();
            return;
        }

        txtTitulo.setText(libro.getTitulo());

        txtAnio.setText(
                String.valueOf(
                        libro.getAnioPublicacion()
                )
        );

        seleccionarAutor(libro.getIdAutor());

        seleccionarCategoria(
                libro.getIdCategoria()
        );

        btnGuardar.setEnabled(false);
        btnEditar.setEnabled(true);
        btnEliminar.setEnabled(true);
    } catch (SQLException e) {
        mostrarError(
                "No fue posible obtener el libro.",
                e
        );
    }
}
    
    private void seleccionarAutor(int idAutor) {
    for (int i = 0; i < listaAutores.size(); i++) {
        AutorBeans autor = listaAutores.get(i);

        if (autor.getIdAutor() == idAutor) {
            cmbAutor.setSelectedIndex(i);
            break;
        }
    }
}

private void seleccionarCategoria(int idCategoria) {
    for (
        int i = 0;
        i < listaCategorias.size();
        i++
    ) {
        CategoriaBeans categoria =
                listaCategorias.get(i);

        if (
            categoria.getIdCategoria()
            == idCategoria
        ) {
            cmbCategoria.setSelectedIndex(i);
            break;
        }
    }
}

private void limpiarFormulario() {
    idLibroSeleccionado = 0;

    txtTitulo.setText("");
    txtAnio.setText("");

    if (cmbAutor.getItemCount() > 0) {
        cmbAutor.setSelectedIndex(0);
    }

    if (cmbCategoria.getItemCount() > 0) {
        cmbCategoria.setSelectedIndex(0);
    }

    tablaLibros.clearSelection();

    btnGuardar.setEnabled(true);
    btnEditar.setEnabled(false);
    btnEliminar.setEnabled(false);

    txtTitulo.requestFocus();
}

private void restablecerFiltros() {
    cambiandoFiltros = true;

    if (cmbFiltroAutor.getItemCount() > 0) {
        cmbFiltroAutor.setSelectedIndex(0);
    }

    if (cmbFiltroCategoria.getItemCount() > 0) {
        cmbFiltroCategoria.setSelectedIndex(0);
    }

    cambiandoFiltros = false;
}

private void filtrarAutor() {
    if (cambiandoFiltros) {
        return;
    }

    try {
        int posicion =
                cmbFiltroAutor.getSelectedIndex();

        cambiandoFiltros = true;
        cmbFiltroCategoria.setSelectedIndex(0);
        cambiandoFiltros = false;

        if (posicion == 0) {
            cargarLibros();
        } else {
            AutorBeans autor =
                    listaAutores.get(posicion - 1);

            List<LibroBeans> libros =
                    libroDatos.filtrarPorAutor(
                            autor.getIdAutor()
                    );

            llenarTabla(libros);
        }

        limpiarFormulario();
    } catch (SQLException e) {
        cambiandoFiltros = false;

        mostrarError(
                "Error al filtrar por autor.",
                e
        );
    }
}

private void filtrarCategoria() {
    if (cambiandoFiltros) {
        return;
    }

    try {
        int posicion =
                cmbFiltroCategoria.getSelectedIndex();

        cambiandoFiltros = true;
        cmbFiltroAutor.setSelectedIndex(0);
        cambiandoFiltros = false;

        if (posicion == 0) {
            cargarLibros();
        } else {
            CategoriaBeans categoria =
                    listaCategorias.get(posicion - 1);

            List<LibroBeans> libros =
                    libroDatos.filtrarPorCategoria(
                            categoria.getIdCategoria()
                    );

            llenarTabla(libros);
        }

        limpiarFormulario();
    } catch (SQLException e) {
        cambiandoFiltros = false;

        mostrarError(
                "Error al filtrar por categoría.",
                e
        );
    }
}

private void mostrarError(
        String mensaje,
        SQLException error
) {
    JOptionPane.showMessageDialog(
            this,
            mensaje + "\n" + error.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE
    );
}



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtTitulo = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        cmbAutor = new javax.swing.JComboBox<>();
        cmbCategoria = new javax.swing.JComboBox<>();
        cmbFiltroAutor = new javax.swing.JComboBox<>();
        cmbFiltroCategoria = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaLibros = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtTitulo.setText("jTextField1");

        txtAnio.setText("jTextField1");

        cmbAutor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbFiltroAutor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFiltroAutor.addActionListener(this::cmbFiltroAutorActionPerformed);

        cmbFiltroCategoria.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbFiltroCategoria.addActionListener(this::cmbFiltroCategoriaActionPerformed);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnEditar.setText("Editar");
        btnEditar.addActionListener(this::btnEditarActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        tablaLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaLibrosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaLibros);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(cmbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addComponent(cmbFiltroAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(cmbFiltroCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(24, 24, 24)))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnEliminar)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnGuardar)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(btnEditar))))
                            .addComponent(btnLimpiar))))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFiltroAutor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbFiltroCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEditar)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)
                        .addGap(18, 18, 18)
                        .addComponent(btnLimpiar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarLibro();
        // TODO add your handling code here
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        editarLibro();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarLibro();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void tablaLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaLibrosMouseClicked
        // TODO add your handling code here:
        seleccionarLibro();
    }//GEN-LAST:event_tablaLibrosMouseClicked

    private void cmbFiltroAutorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFiltroAutorActionPerformed
        // TODO add your handling code here:
        filtrarAutor();
    }//GEN-LAST:event_cmbFiltroAutorActionPerformed

    private void cmbFiltroCategoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbFiltroCategoriaActionPerformed
        // TODO add your handling code here:
        filtrarCategoria();
    }//GEN-LAST:event_cmbFiltroCategoriaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmBiblioteca().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cmbAutor;
    private javax.swing.JComboBox<String> cmbCategoria;
    private javax.swing.JComboBox<String> cmbFiltroAutor;
    private javax.swing.JComboBox<String> cmbFiltroCategoria;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablaLibros;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
