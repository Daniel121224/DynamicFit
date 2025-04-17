package com.software.Dynamicfit.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
//import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity //le dice a Spring que esta clase es una entidad de base de datos.
@Data //con lombok genera getters, setters, constructores, etc
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Builder
@Table(name = "usuarios") //nombre de la tabla en la base de datos.
public class Usuario {

    @Id //indica que el campo id es la clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //para generar un valor unico como id
    private Long id_usuario;

    @Column(name = "username", nullable = false, length = 70)
    private String username;

    @Column(name = "nombre", nullable = false, length = 70)
    private String nombre;

    @Column(name = "email", nullable = false, length = 70)
    private String email;

    @Column(name = "contrasena", nullable = false, length = 70)
    private String contrasena;

    @Column(name = "telefono", nullable = false, length = 70)
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 70)
    private String direccion;

    @Column(name = "rol", nullable = false, length = 20)
    private String rol; // Administrador o Cliente

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Carrito carrito;



    public Usuario(String username, String nombre, String email, String contrasena, String telefono, String direccion, String rol) {
        this.username = username;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.direccion = direccion;
        this.rol = rol;
    }

}

