package com.software.Dynamicfit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//Usado principalmente para agregar productos al carrito

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CarritoProductoDTO {
    private Long id_carrito_producto;
    private Long producto_id;
    private int cantidad;
}

