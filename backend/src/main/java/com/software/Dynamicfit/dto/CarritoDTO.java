package com.software.Dynamicfit.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDTO {
    private Long id_usuario;
    private List<Long> productosId;
}
