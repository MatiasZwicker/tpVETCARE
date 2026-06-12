package com.example.PetCare.repository;

import com.example.PetCare.model.Producto;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto,Integer> {
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByNombre(String nombre);
    List<Producto> findByPrecioBefore(Double precioBefore);
    List<Producto> findByPrecioAfter(Double precioAfter);
    List<Producto> findByActivo(Boolean activo);
    List<Producto> findByStockBefore(Integer stockBefore);
}
