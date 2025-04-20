package com.software.Dynamicfit.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito;

    @Column(name = "total_carrito", nullable = false)
    private Integer total_carrito;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario")
    @JsonBackReference(value = "usuario-carrito")
    private Usuario usuario;


    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "carrito-productos")
    private List<CarritoProducto> productos;
}
