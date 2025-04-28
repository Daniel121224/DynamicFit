package com.software.Dynamicfit.model;

import java.time.LocalDate;
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
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;

    @Column(name = "total_pedido", nullable = false)
    private Integer total_pedido;

    @Column(nullable = false)
    private String estado; // <--- Nuevo atributo

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido; // <--- Nuevo atributo

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "idUsuario")
    @JsonBackReference(value = "usuario-pedidos")
    private Usuario usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "pedido-productos")
    private List<PedidoProducto> productos;
}
