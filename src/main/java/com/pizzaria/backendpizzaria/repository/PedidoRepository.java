package com.pizzaria.backendpizzaria.repository;

import com.pizzaria.backendpizzaria.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido,Integer> {
}
