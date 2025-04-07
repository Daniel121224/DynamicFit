package com.software.Dynamicfit.dto;

//Los DTO Sirve para transferir solo los datos 
//necesarios entre el cliente y el servidor (evita exponer la entidad completa).

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String username;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String rol;

    //No se incluye contrasena por seguridad
}
