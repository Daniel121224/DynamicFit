package com.software.Dynamicfit.service;

import com.software.Dynamicfit.dto.CarritoDTO;
import com.software.Dynamicfit.dto.CarritoProductoDTO;
//import com.software.Dynamicfit.dto.UsuarioDTO;
import com.software.Dynamicfit.model.*;
import com.software.Dynamicfit.repository.*;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;
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
    public Carrito obtenerCarritoPorUsuario(Long idUsuario) {
        return carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    }
    */

    //Este metodo se usa para el metodo de obtener un carrito por el id del usuario
    //y que sea posible observar el id del producto que está en el carrito
    public CarritoDTO obtenerCarritoDTOporUsuario(Long idUsuario) {
        Carrito carrito = carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
    
        return convertirADTO(carrito);
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        List<CarritoProductoDTO> productosDTO = carrito.getProductos().stream().map(cp -> {
                return CarritoProductoDTO.builder()
                        .id_carrito_producto(cp.getId_carrito_producto())
                        .cantidad(cp.getCantidad())
                        .producto_id(cp.getProducto().getId_producto()) // Aquí se incluye el id del producto
                        .build();
        }).toList();

        return CarritoDTO.builder()
                .id_carrito(carrito.getId_carrito())
                .total_carrito(carrito.getTotal_carrito())
                .usuario_id(carrito.getUsuario().getIdUsuario())
                .productos(productosDTO)
                .build();
        }

    

    /*

    public void eliminarCarrito(Long idCarrito) {
        vaciarCarrito(idCarrito);
        carritoRepository.deleteById(idCarrito);
    }

    */

    //Método para agregar un producto al carrito
    //Recibe el id del carrito, el id del producto y la cantidad a agregar
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

        actualizarTotal(carrito);
        return carritoRepository.save(carrito);
    }

    //método para actualizar el total del carrito
    //Recorre todos los productos del carrito y suma el precio por la cantidad
    private void actualizarTotal(Carrito carrito) {
        List<CarritoProducto> productos = carritoProductoRepository.findByCarrito(carrito);
        int total = productos.stream()
                .mapToInt(cp -> cp.getProducto().getPrecio() * cp.getCantidad())
                .sum();
        carrito.setTotal_carrito(total);
    }

    //Método para quitar un producto del carrito
    //Recibe el id del carrito y el id del producto a quitar por medio de la URL
    @Transactional
    public void quitarProducto(Long idCarrito, Long idProducto) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        carritoProductoRepository.deleteByCarritoAndProducto(carrito, producto);

        actualizarTotal(carrito);
        carritoRepository.save(carrito);
    }

    //Método para actualizar la cantidad de un producto en el carrito
    //Recibe el id del carrito, el id del producto y la nueva cantidad a actualizar
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

    //Método para vaciar el carrito
    //Recibe el id del carrito por medio de la URL
    @Transactional
    public void vaciarCarrito(Long idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        carritoProductoRepository.deleteByCarrito(carrito);
        carrito.setTotal_carrito(0);
        carritoRepository.save(carrito);
    }

    /*

    

    

    public List<Producto> obtenerProductos(Long idCarrito) {
        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        return carritoProductoRepository.findByCarrito(carrito).stream()
                .map(CarritoProducto::getProducto)
                .collect(Collectors.toList());
    }

    

    */
}
