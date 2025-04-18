package com.software.Dynamicfit.service;

import com.software.Dynamicfit.dto.CarritoDTO;
import com.software.Dynamicfit.dto.LoginDTO;

//Contiene la lógica del negocio, separada del controlador.

import com.software.Dynamicfit.dto.UsuarioDTO;
import com.software.Dynamicfit.model.Usuario;
import com.software.Dynamicfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoService carritoService; // Inyección de CarritoService

    public List<UsuarioDTO> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(this::convertirADTO).collect(Collectors.toList());
        //return usuarioRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public UsuarioDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::convertirADTO).orElse(null);
    }

    public UsuarioDTO crearUsuario(Usuario usuario) {
        //Usuario guardado = usuarioRepository.save(usuario);
        //return convertirADTO(guardado);

        // Guardamos el usuario
        Usuario guardado = usuarioRepository.save(usuario);

        // Creamos el carrito automáticamente al crearse un usuario
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setUsuario_id(guardado.getId_usuario());
        carritoService.crearCarrito(carritoDTO);

        return convertirADTO(guardado);
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        return usuarioRepository.findById(id).map(usuarioExistente -> {
            usuarioExistente.setUsername(usuarioDTO.getUsername());
            usuarioExistente.setNombre(usuarioDTO.getNombre());
            usuarioExistente.setEmail(usuarioDTO.getEmail());
            usuarioExistente.setTelefono(usuarioDTO.getTelefono());
            usuarioExistente.setDireccion(usuarioDTO.getDireccion());
            usuarioExistente.setRol(usuarioDTO.getRol());
            /* // Si permites actualizar la contraseña:
            if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().isEmpty()) {
                usuarioExistente.setContrasena(usuarioDTO.getContrasena());
            } */
            Usuario actualizado = usuarioRepository.save(usuarioExistente);
            return convertirADTO(actualizado);
        }).orElse(null);
    }
    

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId_usuario());
        dto.setUsername(usuario.getUsername());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setRol(usuario.getRol());

        if (usuario.getCarrito() != null) {
        CarritoDTO carritoDTO = new CarritoDTO();
        carritoDTO.setId_carrito(usuario.getCarrito().getId_carrito());
        carritoDTO.setTotal_carrito(usuario.getCarrito().getTotal_carrito());
        carritoDTO.setUsuario_id(usuario.getId_usuario()); // Aqui deberia obtener el usuario_id del dto de carrito
        
        // puedes incluir más campos si tu carritoDTO los tiene
        dto.setCarrito(carritoDTO);
        }

        return dto;
    }



    public int login(LoginDTO usuarioDto) {
        return usuarioRepository.findByUsernameAndPassword(usuarioDto.getUsername(), usuarioDto.getContrasena());
    }

    public ResponseEntity<?> ingresar(LoginDTO usuarioDto) {
        Map<String, Object> response = new HashMap<>();
        Usuario usuario = null;
        try {
            usuario = usuarioRepository.findByNameAndPassword(usuarioDto.getUsername(), usuarioDto.getContrasena());
            if (usuario == null) {
                response.put("usuario", null);
                response.put("mensaje", "Usuario o contraseña incorrectos");
                response.put("statusCode", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            } else {
                response.put("usuario", convertirADTO(usuario)); // Usamos tu conversor actual
                response.put("mensaje", "Inicio de sesión exitoso");
                response.put("statusCode", HttpStatus.OK.value());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        } catch (Exception e) {
            response.put("usuario", null);
            response.put("mensaje", "Error interno en el servidor");
            response.put("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
