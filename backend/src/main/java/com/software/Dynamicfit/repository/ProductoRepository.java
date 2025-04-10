package com.software.Dynamicfit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.software.Dynamicfit.model.Producto;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria(String categoria); // extra si quieres filtrar por categoría
}
