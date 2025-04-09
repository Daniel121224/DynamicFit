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
