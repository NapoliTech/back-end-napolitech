package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pizzaria.backendpizzaria.domain.ItemPedido;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemPedidoDTO {
    private String produto;
    private Integer quantidade;

    public ItemPedidoDTO(ItemPedido item) {
        this.produto = (item.getProduto() != null) ? item.getProduto().getNome() : "Produto Desconhecido";
        this.quantidade = item.getQuantidade();
    }

    @JsonCreator
    public ItemPedidoDTO(
            @JsonProperty("produto") String produto,
            @JsonProperty("quantidade") Integer quantidade,
            @JsonProperty("precoUnitario") Double precoUnitario
    ) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public ItemPedidoDTO() {
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
