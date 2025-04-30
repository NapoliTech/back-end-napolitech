package com.pizzaria.backendpizzaria.domain.DTO.Login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para atualização de um usuário.")
public class UsuarioAtualizacaoDTO {

    @Schema(description = "Nome do usuário.", example = "João Silva", required = false)
    private String nome;

    @Schema(description = "E-mail do usuário.", example = "joao.silva@exemplo.com", required = false)
    private String email;

    @Schema(description = "Senha do usuário.", example = "novaSenha123", required = false)
    private String senha;

    @Schema(description = "CPF do usuário.", example = "123.456.789-00", required = false)
    private String cpf;

    @Schema(description = "Telefone do usuário.", example = "(11) 98765-4321", required = false)
    private String telefone;

    @Schema(description = "Data de nascimento do usuário.", example = "01/01/1990", required = false)
    private String dataNasc;

    public UsuarioAtualizacaoDTO(String nome, String email, String senha, String cpf, String telefone, String dataNasc) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNasc = dataNasc;
    }

    public UsuarioAtualizacaoDTO() {
    }

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }
}
