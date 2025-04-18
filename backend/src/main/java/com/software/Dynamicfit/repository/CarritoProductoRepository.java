package com.software.Dynamicfit.repository;

import com.software.Dynamicfit.model.Carrito;
import com.software.Dynamicfit.model.CarritoProducto;
import com.software.Dynamicfit.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoProductoRepository extends JpaRepository<CarritoProducto, Long> {

    List<CarritoProducto> findByCarrito(Carrito carrito);

    Optional<CarritoProducto> findByCarritoAndProducto(Carrito carrito, Producto producto);

    void deleteByCarrito(Carrito carrito);

    void deleteByCarritoAndProducto(Carrito carrito, Producto producto);
}