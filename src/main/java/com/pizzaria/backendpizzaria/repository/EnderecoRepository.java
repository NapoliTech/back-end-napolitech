package com.pizzaria.backendpizzaria.repository;

import com.pizzaria.backendpizzaria.domain.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco,Integer> {
}
