package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pizzaria.backendpizzaria.domain.Enum.TamanhoPizza;
import com.pizzaria.backendpizzaria.domain.ItemPedido;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemPedidoDTO {
    private List<Integer> produto;
    private Integer quantidade;
    @Enumerated(EnumType.STRING)
    private TamanhoPizza tamanhoPizza;

    public ItemPedidoDTO(ItemPedido item) {
        this.produto = Collections.singletonList(Integer.valueOf((item.getProduto() != null) ? item.getProduto().getNome() : "Produto Desconhecido"));
        this.quantidade = item.getQuantidade();
    }

    @JsonCreator
    public ItemPedidoDTO(
            @JsonProperty("produto") List<Integer> produto,
            @JsonProperty("quantidade") Integer quantidade,
            @JsonProperty("tamanhoPizza") TamanhoPizza tamanhoPizza
    ) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.tamanhoPizza = tamanhoPizza;
    }

    public ItemPedidoDTO() {
    }

    public TamanhoPizza getTamanhoPizza() {
        return tamanhoPizza;
    }

    public void setTamanhoPizza(TamanhoPizza tamanhoPizza) {
        this.tamanhoPizza = tamanhoPizza;
    }

    public List<Integer> getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = Collections.singletonList(Integer.valueOf(produto));
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
