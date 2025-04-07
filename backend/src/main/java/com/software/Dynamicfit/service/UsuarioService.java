package com.software.Dynamicfit.service;

//Contiene la lógica del negocio, separada del controlador.

import com.software.Dynamicfit.dto.UsuarioDTO;
import com.software.Dynamicfit.model.Usuario;
import com.software.Dynamicfit.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public UsuarioDTO obtenerPorId(Long id) {
        return usuarioRepository.findById(id).map(this::convertirADTO).orElse(null);
    }

    public UsuarioDTO crearUsuario(Usuario usuario) {
        Usuario guardado = usuarioRepository.save(usuario);
        return convertirADTO(guardado);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setRol(usuario.getRol());
        return dto;
    }
}
