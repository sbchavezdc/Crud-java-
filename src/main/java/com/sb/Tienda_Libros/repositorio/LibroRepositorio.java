package com.sb.Tienda_Libros.repositorio;

import com.sb.Tienda_Libros.modelo.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
//realizar consultas con JpaRepository
public interface LibroRepositorio extends JpaRepository<Libro,Integer> {
}
