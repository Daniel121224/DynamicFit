package com.software.Dynamicfit.controller;

import com.software.Dynamicfit.dto.LoginDTO;

//La capa que maneja las solicitudes HTTP (GET, POST, DELETE...).

import com.software.Dynamicfit.dto.UsuarioDTO;
import com.software.Dynamicfit.model.Usuario;
import com.software.Dynamicfit.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Permitir solicitudes de cualquier origen
// Esta anotación permite que el controlador acepte solicitudes de diferentes dominios, útil para aplicaciones frontend y backend separados.
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtenerUsuario(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    @PostMapping
    public UsuarioDTO crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }



    @PostMapping("/loginclient")
    public int login(@RequestBody LoginDTO usuario){
        return usuarioService.login(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCliente(@RequestBody LoginDTO usuario){
        return usuarioService.ingresar(usuario);
    }

}
