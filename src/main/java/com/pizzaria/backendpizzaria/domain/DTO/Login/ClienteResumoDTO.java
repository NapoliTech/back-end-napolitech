package com.pizzaria.backendpizzaria.domain.DTO.Login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo das informações de um cliente.")
public class ClienteResumoDTO {

    @Schema(description = "Nome do cliente.", example = "João Silva", required = true)
    private String nome;

    @Schema(description = "E-mail do cliente.", example = "joao.silva@exemplo.com", required = true)
    private String email;

    @Schema(description = "Telefone do cliente.", example = "(11) 98765-4321", required = true)
    private String telefone;

    @Schema(description = "Quantidade de pedidos realizados pelo cliente.", example = "5", required = true)
    private int quantidadePedidos;

    public ClienteResumoDTO(String nome, String email, String telefone, int quantidadePedidos) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.quantidadePedidos = quantidadePedidos;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getQuantidadePedidos() {
        return quantidadePedidos;
    }

    public void setQuantidadePedidos(int quantidadePedidos) {
        this.quantidadePedidos = quantidadePedidos;
    }
}
