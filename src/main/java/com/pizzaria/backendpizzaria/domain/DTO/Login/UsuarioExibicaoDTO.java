package com.pizzaria.backendpizzaria.domain.DTO.Login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados detalhados de um usuário para exibição.")
public class UsuarioExibicaoDTO {

    @Schema(description = "Nome do usuário.", example = "João Silva", required = true)
    private String nome;

    @Schema(description = "Telefone do usuário.", example = "(11) 98765-4321", required = true)
    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
