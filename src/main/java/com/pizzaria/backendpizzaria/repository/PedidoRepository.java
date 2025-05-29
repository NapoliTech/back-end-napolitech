package com.pizzaria.backendpizzaria.repository;

import com.pizzaria.backendpizzaria.domain.Pedido;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    int countAllBy();

    int countByStatusPedidoAndDataPedidoBetween(StatusPedido status, LocalDateTime start, LocalDateTime end);

    @Query("SELECT SUM(p.precoTotal) FROM Pedido p WHERE p.dataPedido BETWEEN :start AND :end")
    Double sumPrecoTotalByDataPedidoBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}