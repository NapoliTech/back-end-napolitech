package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import com.pizzaria.backendpizzaria.domain.Enum.CategoriaProduto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa os dados de um produto no sistema.")
public class ProdutoDTO {

    @Schema(description = "Nome do produto.", example = "Pizza de Calabresa", required = true)
    private String nome;

    @Schema(description = "Preço do produto.", example = "45.90", required = true)
    private Double preco;

    @Schema(description = "Quantidade disponível no estoque.", example = "10", required = true)
    private Integer quantidade;

    @Schema(description = "Ingredientes do produto.", example = "Calabresa, queijo, molho de tomate", required = true)
    private String ingredientes;

    @Schema(description = "Categoria do produto.", example = "PIZZA", required = true)
    private CategoriaProduto categoriaProduto;

    public CategoriaProduto getCategoriaProduto() {
        return categoriaProduto;
    }

    public void setCategoriaProduto(CategoriaProduto categoriaProduto) {
        this.categoriaProduto = categoriaProduto;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}