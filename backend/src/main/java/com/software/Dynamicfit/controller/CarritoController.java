package com.software.Dynamicfit.controller;

import com.software.Dynamicfit.dto.CarritoDTO;
import com.software.Dynamicfit.dto.CarritoProductoDTO;
import com.software.Dynamicfit.model.Carrito;
//import com.software.Dynamicfit.model.Producto;
import com.software.Dynamicfit.service.CarritoService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.List;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin(origins = "*")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping
    public ResponseEntity<Carrito> crearCarrito(@RequestBody CarritoDTO carritoDTO) {
        Carrito nuevoCarrito = carritoService.crearCarrito(carritoDTO);
        return ResponseEntity.ok(nuevoCarrito);
    }

    
    @GetMapping("/{idUsuario}")
    public ResponseEntity<CarritoDTO> obtenerCarritoPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarritoDTOporUsuario(idUsuario));
    }

    /*

    @DeleteMapping("/{idCarrito}")
    public ResponseEntity<?> eliminarCarrito(@PathVariable Long idCarrito) {
        carritoService.eliminarCarrito(idCarrito);
        return ResponseEntity.ok().build();
    }

    */

    @PostMapping("/{idCarrito}/agregar")
    public ResponseEntity<Carrito> agregarProductoAlCarrito(
            @PathVariable("idCarrito") Long idCarrito,
            @RequestBody CarritoProductoDTO request) {
        return ResponseEntity.ok(
                carritoService.agregarProducto(idCarrito, request.getProducto_id(), request.getCantidad())
        );
    }

    @DeleteMapping("/{idCarrito}/quitar/{idProducto}")
    public ResponseEntity<?> quitarProductoDelCarrito(
            @PathVariable("idCarrito") Long idCarrito,
            @PathVariable("idProducto") Long idProducto) {
        carritoService.quitarProducto(idCarrito, idProducto);
        return ResponseEntity.ok().build();
    }

    // Este método se usa para actualizar la cantidad de un producto en el carrito
    // Recibe el id del carrito, el id del producto y la nueva cantidad
    // Se espera que el cuerpo de la solicitud contenga un JSON con la nueva cantidad
    @PutMapping("/{idCarrito}/actualizar/{idProducto}")
    public ResponseEntity<Carrito> actualizarCantidadProducto(
            @PathVariable("idCarrito") Long idCarrito,
            @PathVariable("idProducto") Long idProducto,
            @RequestBody Map<String, Integer> requestBody) {

        int cantidad = requestBody.get("cantidad");
        return ResponseEntity.ok(carritoService.actualizarCantidad(idCarrito, idProducto, cantidad));
    }

    
    @DeleteMapping("/{idCarrito}/vaciar")
    public ResponseEntity<?> vaciarCarrito(@PathVariable("idCarrito") Long idCarrito) {
        carritoService.vaciarCarrito(idCarrito);
        return ResponseEntity.ok().build();
    }


    /*

    

    

    @GetMapping("/{idCarrito}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosDelCarrito(@PathVariable Long idCarrito) {
        return ResponseEntity.ok(carritoService.obtenerProductos(idCarrito));
    }

    */

}
