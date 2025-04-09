package com.software.Dynamicfit.repository;

//El repositorio es la capa que interactúa con la base de datos.

import com.software.Dynamicfit.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT COUNT(*) FROM Usuario u WHERE u.username = :username AND u.contrasena = :contrasena")
    Integer findByUsernameAndPassword(@Param("username") String username, @Param("contrasena") String contrasena);

    @Query("SELECT u FROM Usuario u WHERE u.username = :username AND u.contrasena = :contrasena")
    Usuario findByNameAndPassword(@Param("username") String username, @Param("contrasena") String contrasena);
}
