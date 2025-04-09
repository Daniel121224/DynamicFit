package com.software.Dynamicfit.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.software.Dynamicfit.dto.LoginDTO;
import com.software.Dynamicfit.model.Usuario;
import com.software.Dynamicfit.repository.UsuarioRepository;

/*
Este controlador recibe las solicitudes de inicio de sesión con el LoginDto 
y llama al AuthService para validar las credenciales del usuario.
*/

/**
 * Clase AuthController (NO UTILIZADA ACTUALMENTE)
 *
 * Esta clase fue creada inicialmente para manejar la lógica de autenticación (login) de usuarios.
 * Sin embargo, actualmente no está en uso, ya que la funcionalidad de inicio de sesión se implementó
 * directamente en la clase UsuarioController, en el endpoint '/api/usuarios/login'.
 *
 * El AuthController puede eliminarse si no se planea ampliar la autenticación del sistema.
 * No obstante, se recomienda conservarlo si en el futuro se desea:
 *   - Separar responsabilidades y mantener una arquitectura más limpia (login y registro por separado).
 *   - Implementar autenticación mediante JWT (JSON Web Tokens).
 *   - Utilizar Spring Security u otros mecanismos más avanzados de seguridad.
 *
 * Por ahora, toda la lógica de login está centralizada en UsuarioController.
 */


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDto, HttpSession session) {
        Usuario usuario = usuarioRepo.findByNameAndPassword(loginDto.getUsername(), loginDto.getContrasena());
        if (usuario != null) {
            session.setAttribute("usuario", usuario); // Guarda el usuario en la sesión
            return ResponseEntity.ok("Inicio de sesión exitoso");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // Invalida la sesión
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }
}
