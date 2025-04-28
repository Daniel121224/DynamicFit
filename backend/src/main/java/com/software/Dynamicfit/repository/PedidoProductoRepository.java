package com.software.Dynamicfit.repository;

import com.software.Dynamicfit.model.Pedido;
import com.software.Dynamicfit.model.PedidoProducto;
import com.software.Dynamicfit.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PedidoProductoRepository extends JpaRepository<PedidoProducto, Long> {

    List<PedidoProducto> findByPedido(Pedido pedido);

    Optional<PedidoProducto> findByPedidoAndProducto(Pedido pedido, Producto producto);

    void deleteByPedidoAndProducto(Pedido pedido, Producto producto);

    void deleteByPedido(Pedido pedido);
}
