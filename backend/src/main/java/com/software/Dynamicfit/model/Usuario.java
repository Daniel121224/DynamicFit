package com.software.Dynamicfit.model;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity //le dice a Spring que esta clase es una entidad de base de datos.
@Data //con lombok genera getters, setters, constructores, etc
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuario") //nombre de la tabla en la base de datos.
public class Usuario {

    @Id //indica que el campo id es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //para generar un valor unico como id
    private Long id;

    private String username;

    private String nombre;

    private String email;

    private String contrasena;

    private String telefono;

    private String direccion;

    private String rol; // Administrador o Cliente

    public Usuario(String username, String nombre, String email, String contrasena, String telefono, String direccion, String rol) {
        this.username = username;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
    }

    // BORRAR
    //getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }


}

