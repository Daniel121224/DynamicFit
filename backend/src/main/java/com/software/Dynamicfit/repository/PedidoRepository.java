package com.software.Dynamicfit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.software.Dynamicfit.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByUsuario_IdUsuario(Long idUsuario);
}
