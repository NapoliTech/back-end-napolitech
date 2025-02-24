package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProdutoDTO {
    private String nome;
    private Double preco;
    private Integer quantidade;
}