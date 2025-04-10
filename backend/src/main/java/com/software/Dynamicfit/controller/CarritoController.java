package com.software.Dynamicfit.controller;

import com.software.Dynamicfit.dto.CarritoDTO;
import com.software.Dynamicfit.model.Carrito;
import com.software.Dynamicfit.model.Producto;
import com.software.Dynamicfit.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Carrito> obtenerCarritoPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(idUsuario));
    }

    @PostMapping("/{idCarrito}/agregar")
    public ResponseEntity<Carrito> agregarProductoAlCarrito(
            @PathVariable Long idCarrito,
            @RequestParam Long idProducto,
            @RequestParam(defaultValue = "1") int cantidad) {
        return ResponseEntity.ok(carritoService.agregarProducto(idCarrito, idProducto, cantidad));
    }

    @DeleteMapping("/{idCarrito}/quitar/{idProducto}")
    public ResponseEntity<?> quitarProductoDelCarrito(
            @PathVariable Long idCarrito,
            @PathVariable Long idProducto) {
        carritoService.quitarProducto(idCarrito, idProducto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idCarrito}/vaciar")
    public ResponseEntity<?> vaciarCarrito(@PathVariable Long idCarrito) {
        carritoService.vaciarCarrito(idCarrito);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idCarrito}")
    public ResponseEntity<?> eliminarCarrito(@PathVariable Long idCarrito) {
        carritoService.eliminarCarrito(idCarrito);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idCarrito}/actualizar")
    public ResponseEntity<Carrito> actualizarCantidadProducto(
            @PathVariable Long idCarrito,
            @RequestParam Long idProducto,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(idCarrito, idProducto, cantidad));
    }

    @GetMapping("/{idCarrito}/productos")
    public ResponseEntity<List<Producto>> obtenerProductosDelCarrito(@PathVariable Long idCarrito) {
        return ResponseEntity.ok(carritoService.obtenerProductos(idCarrito));
    }
}
