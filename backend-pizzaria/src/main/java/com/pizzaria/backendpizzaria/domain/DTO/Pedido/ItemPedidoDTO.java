package com.pizzaria.backendpizzaria.domain.DTO.Pedido;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pizzaria.backendpizzaria.domain.ItemPedido;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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
}
