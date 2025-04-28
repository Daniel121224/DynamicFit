package com.software.Dynamicfit.repository;

import com.software.Dynamicfit.model.Carrito;
import com.software.Dynamicfit.model.Usuario;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    Optional<Carrito> findByUsuario_IdUsuario(Long idUsuario);

    Optional<Carrito> findByUsuario(Usuario usuario);


}
