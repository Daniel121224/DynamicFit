package com.software.Dynamicfit.dto;

import lombok.Data;

//Usado principalmente para agregar productos al carrito

@Data
public class CarritoProducto {
    private Long producto_id;
    private int cantidad;
}

