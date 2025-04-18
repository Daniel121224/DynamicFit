package com.software.Dynamicfit.service;

import com.software.Dynamicfit.dto.CarritoDTO;
//import com.software.Dynamicfit.dto.UsuarioDTO;
import com.software.Dynamicfit.model.*;
import com.software.Dynamicfit.repository.*;
import org.springframework.stereotype.Service;

//import java.util.List;
import java.util.Optional;
//import java.util.stream.Collectors;

@Service
public class CarritoService {


    private final CarritoRepository carritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final CarritoProductoRepository carritoProductoRepository;

    public CarritoService(CarritoRepository carritoRepository,
                          ProductoRepository productoRepository,
                          UsuarioRepository usuarioRepository,
                          CarritoProductoRepository carritoProductoRepository) {
        this.carritoRepository = carritoRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.carritoProductoRepository = carritoProductoRepository;
    }

    // Para crear un carrito solo necesita pasarle el id del usuario al que va a pertenecer
    public Carrito crearCarrito(CarritoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuario_id()) // Cambié de id_usuario a usuario_id
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setTotal_carrito(0);

        // Establecer la relación bidireccional
        usuario.setCarrito(carrito);

        return carritoRepository.save(carrito);
    }

    /*

    // Duda en el metodo findByUsuarioId, no se si es correcto
    public Carrito obtenerCarritoPorUsuario(Long idUsuario) {
        return carritoRepository.findByUsuario_Id_usuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }

    public void eliminarCarrito(Long idCarrito) {
        vaciarCarrito(idCarrito);
        carritoRepository.deleteById(idCarrito);
    }

    */

    public Carrito agregarProducto(Long idCarrito, Long idProducto, int cantidad) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<CarritoProducto> existente = carritoProductoRepository.findByCarritoAndProducto(carrito, producto);

        if (existente.isPresent()) {
            CarritoProducto cp = existente.get();
            cp.setCantidad(cp.getCantidad() + cantidad);
            carritoProductoRepository.save(cp);
        } else {
            CarritoProducto nuevo = new CarritoProducto();
            nuevo.setCarrito(carrito);
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            carritoProductoRepository.save(nuevo);
        }

        //actualizarTotal(carrito);
        return carritoRepository.save(carrito);
    }

    /*

    public void quitarProducto(Long idCarrito, Long idProducto) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        carritoProductoRepository.deleteByCarritoAndProducto(carrito, producto);

        actualizarTotal(carrito);
        carritoRepository.save(carrito);
    }

    public void vaciarCarrito(Long idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carritoProductoRepository.deleteByCarrito(carrito);
        carrito.setTotal_carrito(0);
        carritoRepository.save(carrito);
    }

    public Carrito actualizarCantidad(Long idCarrito, Long idProducto, int cantidad) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        CarritoProducto cp = carritoProductoRepository.findByCarritoAndProducto(carrito, producto)
                .orElseThrow(() -> new RuntimeException("Producto no está en el carrito"));

        cp.setCantidad(cantidad);
        carritoProductoRepository.save(cp);

        actualizarTotal(carrito);
        return carritoRepository.save(carrito);
    }

    public List<Producto> obtenerProductos(Long idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        return carritoProductoRepository.findByCarrito(carrito).stream()
                .map(CarritoProducto::getProducto)
                .collect(Collectors.toList());
    }

    private void actualizarTotal(Carrito carrito) {
        List<CarritoProducto> productos = carritoProductoRepository.findByCarrito(carrito);
        int total = productos.stream()
                .mapToInt(cp -> cp.getProducto().getPrecio() * cp.getCantidad())
                .sum();
        carrito.setTotal_carrito(total);
    }

    */
}
