package com.software.Dynamicfit.service;

import com.software.Dynamicfit.dto.PedidoDTO;
import com.software.Dynamicfit.dto.PedidoProductoDTO;
import com.software.Dynamicfit.model.*;
import com.software.Dynamicfit.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CarritoRepository carritoRepository;
    private final CarritoProductoRepository carritoProductoRepository;
    private final PedidoProductoRepository pedidoProductoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
                          UsuarioRepository usuarioRepository,
                          CarritoRepository carritoRepository,
                          CarritoProductoRepository carritoProductoRepository,
                          PedidoProductoRepository pedidoProductoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.carritoRepository = carritoRepository;
        this.carritoProductoRepository = carritoProductoRepository;
        this.pedidoProductoRepository = pedidoProductoRepository;
    }

    @Transactional
    public Pedido crearPedidoDesdeCarrito(Long idUsuario) {
        // 1. Buscar el usuario
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        // 2. Buscar el carrito del usuario
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario"));
        // 3. Obtener los productos del carrito
        List<CarritoProducto> productosEnCarrito = carritoProductoRepository.findByCarrito(carrito);
        if (productosEnCarrito.isEmpty()) {
            throw new RuntimeException("El carrito está vacío, no se puede crear un pedido.");
        }

        // 4. Crear el pedido inicial
        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .total_pedido(0)
                .estado("Solicitado")
                .fechaPedido(LocalDate.now())
                .build();

        pedido = pedidoRepository.save(pedido); // Guardar para generar el ID

        // 5. Crear relaciones PedidoProducto y calcular total
        int totalPedido = 0;
        List<PedidoProducto> pedidoProductos = new java.util.ArrayList<>();

        for (CarritoProducto cp : productosEnCarrito) {
            PedidoProducto pedidoProducto = PedidoProducto.builder()
                    .pedido(pedido)
                    .producto(cp.getProducto())
                    .cantidad(cp.getCantidad())
                    .build();

            totalPedido += cp.getProducto().getPrecio() * cp.getCantidad();
            pedidoProductos.add(pedidoProducto);
        }

        // Guardar relaciones
        pedidoProductoRepository.saveAll(pedidoProductos);

        // 6. Actualizar el pedido con productos y total
        pedido.setProductos(pedidoProductos);
        pedido.setTotal_pedido(totalPedido);

        return pedidoRepository.save(pedido);
    }



    // Listar todos los pedidos
    public List<PedidoDTO> listarTodosLosPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Listar pedidos por usuario
    public List<PedidoDTO> listarPedidosPorUsuario(Long idUsuario) {
        List<Pedido> pedidos = pedidoRepository.findAllByUsuario_IdUsuario(idUsuario);
        return pedidos.stream()
                .map(this::convertirADTO)
                .toList();
    }

    // Convertir pedido a DTO
    private PedidoDTO convertirADTO(Pedido pedido) {
        List<PedidoProductoDTO> productosDTO = pedido.getProductos().stream().map(pp -> {
            return PedidoProductoDTO.builder()
                    .id_pedido_producto(pp.getId_pedido_producto())
                    .producto_id(pp.getProducto().getId_producto())
                    .cantidad(pp.getCantidad())
                    .build();
        }).toList();

        return PedidoDTO.builder()
                .id_pedido(pedido.getId_pedido())
                .total_pedido(pedido.getTotal_pedido())
                .estado(pedido.getEstado())
                .fechaPedido(pedido.getFechaPedido())
                .usuario_id(pedido.getUsuario().getIdUsuario())
                .productos(productosDTO)
                .build();
    }


    @Transactional
    public void eliminarPedido(Long idPedido) {
        // 1. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        // 2. Eliminar las relaciones PedidoProducto
        pedidoProductoRepository.deleteAll(pedido.getProductos());
        // 3. Eliminar el pedido
        pedidoRepository.delete(pedido);
    }


    @Transactional
    public PedidoDTO actualizarEstadoPedido(Long idPedido, String nuevoEstado) {
        // 1. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        // 2. Actualizar el estado
        pedido.setEstado(nuevoEstado);
        // 3. Guardar los cambios
        Pedido pedidoActualizado = pedidoRepository.save(pedido);
        // 4. Retornar como DTO
        return convertirADTO(pedidoActualizado);
    }



}
