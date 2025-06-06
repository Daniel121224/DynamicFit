package com.software.Dynamicfit.model;

//import java.util.List;

import jakarta.persistence.Column;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

//import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Data
@NoArgsConstructor // Constructor sin argumentos
@AllArgsConstructor // Constructor con todos los argumentos
@Builder
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @Column(name = "nombre_producto", nullable = false, length = 70)
    private String nombre_producto;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Integer precio;

    @Column(name = "categoria", nullable = false, length = 30)
    private String categoria;

     
    @OneToMany(mappedBy = "producto", cascade = jakarta.persistence.CascadeType.ALL)
    @JsonManagedReference(value = "producto-carritoProducto")
    private List<CarritoProducto> carritoProductos; // Relación con CarritoProducto

    
    
    public Producto(String nombre_producto, String descripcion, Integer precio, String categoria) {
        this.nombre_producto = nombre_producto;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
    }
}
