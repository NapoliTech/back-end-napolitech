package com.pizzaria.backendpizzaria.repository;

import com.pizzaria.backendpizzaria.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
