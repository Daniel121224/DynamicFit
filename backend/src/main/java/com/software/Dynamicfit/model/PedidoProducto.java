package com.software.Dynamicfit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "pedidos_productos")
public class PedidoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido_producto;

    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id_pedido")
    @JsonBackReference(value = "pedido-productos")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "id_producto")
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;
}
