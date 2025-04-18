package com.software.Dynamicfit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "carrito_producto")
public class CarritoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_carrito_producto;

    @ManyToOne
    @JoinColumn(name = "carrito_id", referencedColumnName = "id_carrito")
    @JsonBackReference
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id_producto")
    @JsonBackReference
    private Producto producto;

    private Integer cantidad;
}
