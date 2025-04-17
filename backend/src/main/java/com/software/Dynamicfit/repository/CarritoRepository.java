package com.software.Dynamicfit.repository;

import com.software.Dynamicfit.model.Carrito;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    //vacio

    //Optional<Carrito> findByUsuario_Id_usuario(Long idUsuario);

}
