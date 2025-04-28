package com.software.Dynamicfit.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PedidoDTO {
    private Long id_pedido;
    private Integer total_pedido;
    private String estado;
    private LocalDate fechaPedido;
    private Long usuario_id;
    private List<PedidoProductoDTO> productos;
}
