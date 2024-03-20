package com.sb.Tienda_Libros.servicio;

import com.sb.Tienda_Libros.modelo.Libro;
import com.sb.Tienda_Libros.repositorio.LibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LibroServicio implements ILibroServicio{
    //el autowired nos sirve para inyectar la instancia de libro repositorio
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Override
    public List<Libro> ListarLibros() {
        //regresa todos los libros encontrados
        return libroRepositorio.findAll();
    }

    @Override
    public Libro buscarLibroPorId(Integer idLibro) {
       Libro libro = libroRepositorio.findById(idLibro).orElse(null);
        return  libro;
    }

    @Override
    public void GuardarLibro(Libro libro) {
        libroRepositorio.save(libro);
    }

    @Override
    public void eliminarLibro(Libro libro) {
        libroRepositorio.delete(libro);
    }
}
