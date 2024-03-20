package com.sb.Tienda_Libros.servicio;

import com.sb.Tienda_Libros.modelo.Libro;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ILibroServicio {
    public List<Libro> ListarLibros();

    public Libro buscarLibroPorId(Integer idLibro);

    public  void GuardarLibro(Libro libro);

    public  void eliminarLibro(Libro libro);
}
