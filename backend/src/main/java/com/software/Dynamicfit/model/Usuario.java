package com.software.Dynamicfit.model;


import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")  // Opcional
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    private String username;
    private String contraseña;
    private String nombre;
    private String email;

    // Getters y setters
}

