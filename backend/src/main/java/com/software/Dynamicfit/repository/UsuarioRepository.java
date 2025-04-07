package com.software.Dynamicfit.repository;

//El repositorio es la capa que interactúa con la base de datos.

import com.software.Dynamicfit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}
