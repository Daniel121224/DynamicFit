package com.software.Dynamicfit.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoProductoDTO {

    private Long id_pedido_producto;
    private Long producto_id;
    private Integer cantidad;
}
