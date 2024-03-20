package com.sb.Tienda_Libros.vista;

import com.sb.Tienda_Libros.modelo.Libro;
import com.sb.Tienda_Libros.servicio.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Component
public class LibroForm extends JFrame {

    LibroServicio libroServicio;
    private JPanel panel;
    private JTable tablaLibros;
    private JTextField idTexto;
    private JTextField LibroTexto;
    private JTextField AutorTexto;
    private JTextField PrecioTexto;
    private JTextField ExistenciasTexto;
    private JButton agregarButton;
    private JButton eliminarButton;
    private JButton editarButton;
    private DefaultTableModel tablaModeloLibros;

    @Autowired //sirve para inyectar las dependencias de las otras clases
    public LibroForm(LibroServicio libroServicio){
        this.libroServicio = libroServicio;
        iniciarForma();
        agregarButton.addActionListener(e -> agregarLibro());

        tablaLibros.addMouseListener(new MouseAdapter() {
            //este metodo es para editar los valores de nuestra tabla
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarLibroSeleccionado();
            }
        });


        editarButton.addActionListener(e->editarLibro());
        eliminarButton.addActionListener(e->eliminarLibro());
    }

    //este metodo nos ayuda a instanciar
    private  void  iniciarForma(){
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900,700);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension tamañoPantalla = toolkit.getScreenSize();
        int x = (tamañoPantalla.width=getWidth()/2);
        int y = (tamañoPantalla.width=getWidth()/2);
        setLocation(x,y);
    }

    private void agregarLibro(){
        //leer os valores del formulario
        if (LibroTexto.getText().equals("")){
            mostarMensaje("Proporciona el nombre del libro");
            LibroTexto.requestFocusInWindow();
            return;
        }
        var  nombreLibro = LibroTexto.getText();
        var autor = AutorTexto.getText();
        var precio = Double.parseDouble(PrecioTexto.getText());
        var existencias = Integer.parseInt(ExistenciasTexto.getText());

        //cerar el objeto libro
       // var libro = new Libro();
        var libro = new Libro(null,nombreLibro,autor,precio,existencias);
       // libro.setNombreLibro(nombreLibro);
       // libro.setAutor(autor);
       // libro.setPrecio(precio);
       // libro.setExistencia(existencias);
        this.libroServicio.GuardarLibro(libro);
        mostarMensaje("se agergo el libro... ");
        limpiarFormulario();
        listarLibros();
    }

    private void editarLibro(){
        if (this.idTexto.getText().equals("")){
            mostarMensaje("Debe de seleccionar un registro...");
        }else{
            //verificamos que el nombre del libro no sea null
            if (LibroTexto.getText().equals("")){
                mostarMensaje("Proporciona el nombre de libro..");
                LibroTexto.requestFocusInWindow();
                return;
            }
            //llenar el objeto del libro a actualizar
            int idLibro = Integer.parseInt(idTexto.getText());
            var nombreLibro = LibroTexto.getText();
            var autor = AutorTexto.getText();
            var precio = Double.parseDouble(PrecioTexto.getText());
            var existencias = Integer.parseInt(ExistenciasTexto.getText());
            var libro = new Libro(idLibro,nombreLibro,autor,precio,existencias);

            libroServicio.GuardarLibro(libro);
            mostarMensaje("Semodifico el libro correctamente");
            limpiarFormulario();
            listarLibros();


        }
    }

    private void eliminarLibro(){
        var renglon = tablaLibros.getSelectedRow();
        if (renglon!=-1){
            String idLibro= tablaLibros.getModel().getValueAt(renglon,0).toString();
            var libro = new Libro();
            libro.setIdLibro(Integer.parseInt(idLibro));
            libroServicio.eliminarLibro(libro);
            mostarMensaje("Libro "+ idLibro + "eliminado");
            limpiarFormulario();
            listarLibros();
        }else{
            mostarMensaje("No se a seleccionado ningun libro para eliminar");
        }
    }
    private void cargarLibroSeleccionado(){
        //indces de la tavbla inician en cero
        var renglon = tablaLibros.getSelectedRow();
        if (renglon!=-1){
            //regresa -1 si no se selecciona ningun registro
            String idLibro = tablaLibros.getModel().getValueAt(renglon,0).toString();
            //llenar los campos de la tabla
            idTexto.setText(idLibro);

            String nombreLibro= tablaLibros.getModel().getValueAt(renglon,1).toString();
            LibroTexto.setText(nombreLibro);

            String autor= tablaLibros.getModel().getValueAt(renglon,2).toString();
            AutorTexto.setText(autor);

            String precio= tablaLibros.getModel().getValueAt(renglon,3).toString();
            PrecioTexto.setText(precio);

            String existencia= tablaLibros.getModel().getValueAt(renglon,4).toString();
            ExistenciasTexto.setText(existencia);

        }
    }
    private  void limpiarFormulario(){
        LibroTexto.setText("");
        AutorTexto.setText("");
        PrecioTexto.setText("");
        ExistenciasTexto.setText("");

    }
    private  void  mostarMensaje(String mensaje){
        JOptionPane.showMessageDialog(this,mensaje );
    }

        //metodo para personalisar los componentes de nuestro formulario
    private void createUIComponents() {
        // TODO: place custom component creation code here
        //crear elemento id texto oculto
        idTexto = new JTextField("");
        idTexto.setVisible(false);

        this.tablaModeloLibros = new DefaultTableModel(0,5){
            @Override
            public  boolean isCellEditable(int row, int colum){
                return false;
            }
        };
        String[] cabeceros = {"Id","Libro","Autor","Precio","Existencias"};
        this.tablaModeloLibros.setColumnIdentifiers(cabeceros);
        //Instanciar el objeto jtable
        this.tablaLibros = new JTable(tablaModeloLibros);
        //evitar que se seleccionen varios regoitros de la tabla
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listarLibros();
    }

    private  void  listarLibros(){
        //Limpiar tabla
        tablaModeloLibros.setRowCount(0);
        //obtener libros
        var libros = libroServicio.ListarLibros();
        libros.forEach((libro)->{
            Object[] renglonLibro = {
                    libro.getIdLibro(),
                    libro.getNombreLibro(),
                    libro.getAutor(),
                    libro.getPrecio(),
                    libro.getExistencia()
            };
            this.tablaModeloLibros.addRow(renglonLibro);
        });

    }
}
