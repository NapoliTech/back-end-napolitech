package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pizzaria.backendpizzaria.domain.Enum.BordaRecheada;
import com.pizzaria.backendpizzaria.domain.Enum.TamanhoPizza;
import com.pizzaria.backendpizzaria.domain.ItemPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.Collections;
import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Representa os dados de um item de pedido no sistema.")
public class ItemPedidoDTO {

    @Schema(description = "Lista de IDs dos produtos no pedido.", example = "[1, 2]", required = true)
    private List<Integer> produto;

    @Schema(description = "Quantidade do produto no pedido.", example = "2", required = true)
    private Integer quantidade;
    @Enumerated(EnumType.STRING)
    @Schema(description = "Tamanho da pizza.", example = "GRANDE", required = true)
    private TamanhoPizza tamanhoPizza;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de borda recheada, se aplicável.", example = "CATUPIRY", required = false)
    private BordaRecheada bordaRecheada;


    public ItemPedidoDTO(ItemPedido item) {
        this.produto = Collections.singletonList(Integer.valueOf((item.getProduto() != null) ? item.getProduto().getNome() : "Produto Desconhecido"));
        this.quantidade = item.getQuantidade();
    }

    @JsonCreator
    public ItemPedidoDTO(
            @JsonProperty("produto") List<Integer> produto,
            @JsonProperty("quantidade") Integer quantidade,
            @JsonProperty("tamanhoPizza") TamanhoPizza tamanhoPizza,
            @JsonProperty("bordaRecheada") BordaRecheada bordaRecheada
    ) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.tamanhoPizza = tamanhoPizza;
        this.bordaRecheada = bordaRecheada;
    }


    public BordaRecheada getBordaRecheada() {
        return bordaRecheada;
    }

    public void setBordaRecheada(BordaRecheada bordaRecheada) {
        this.bordaRecheada = bordaRecheada;
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

    public void setProduto(List<Integer> produto) {
        this.produto = produto;
    }
}
