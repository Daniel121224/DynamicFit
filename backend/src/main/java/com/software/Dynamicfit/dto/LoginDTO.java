package com.software.Dynamicfit.dto;

/*
Configuracion del Login de la aplicación, mediante una consulta a usuarios registrados
y sus respectivas contraeñas
*/

public class LoginDTO {

    private String username;

    private String contrasena;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
}