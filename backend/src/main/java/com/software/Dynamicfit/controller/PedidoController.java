package com.software.Dynamicfit.controller;

import com.software.Dynamicfit.dto.PedidoDTO;
import com.software.Dynamicfit.model.Pedido;
import com.software.Dynamicfit.service.PedidoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crear/{idUsuario}")
    public ResponseEntity<Pedido> crearPedido(@PathVariable("idUsuario") Long idUsuario) {
        Pedido nuevoPedido = pedidoService.crearPedidoDesdeCarrito(idUsuario);
        return ResponseEntity.ok(nuevoPedido);
    }


    // Listar todos los pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarTodosLosPedidos() {
        List<PedidoDTO> pedidos = pedidoService.listarTodosLosPedidos();
        return ResponseEntity.ok(pedidos);
    }

    // Listar pedidos por usuario
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<PedidoDTO>> listarPedidosPorUsuario(@PathVariable("idUsuario") Long idUsuario) {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorUsuario(idUsuario);
        return ResponseEntity.ok(pedidos);
    }

    // Listar un pedido por ID
    @GetMapping("/{idPedido}")
    public ResponseEntity<PedidoDTO> listarPedidoPorId(@PathVariable("idPedido") Long idPedido) {
        PedidoDTO pedido = pedidoService.listarPedidoPorId(idPedido);
        return ResponseEntity.ok(pedido);
    }

    // Eliminar un pedido por ID
    @DeleteMapping("/{idPedido}")
    public ResponseEntity<String> eliminarPedido(@PathVariable("idPedido") Long idPedido) {
        pedidoService.eliminarPedido(idPedido);
         return ResponseEntity.noContent().build();
    }

    // Actualizar el estado de un pedido por ID
    @PutMapping("/{idPedido}/estado")
    public ResponseEntity<PedidoDTO> actualizarEstadoPedido(@PathVariable("idPedido") Long idPedido, @RequestBody String nuevoEstado) {
        PedidoDTO pedidoActualizado = pedidoService.actualizarEstadoPedido(idPedido, nuevoEstado);
        return ResponseEntity.ok(pedidoActualizado);
    }


}
