package com.software.Dynamicfit.dto;

import lombok.*;

//import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDTO {

    private Long id_carrito;
    private Integer total_carrito;
    private Long usuario_id; 
    //private List<Long> productosId;
}
    
    