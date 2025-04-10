package com.software.Dynamicfit.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id_producto;
    private String nombre_producto;
    private String descripcion;
    private Integer precio;
    private String categoria;
}
