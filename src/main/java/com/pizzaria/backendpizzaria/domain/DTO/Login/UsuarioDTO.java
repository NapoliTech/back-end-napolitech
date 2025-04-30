package com.pizzaria.backendpizzaria.domain.DTO.Login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados básicos de um usuário para operações gerais.")
public class UsuarioDTO {

    @Schema(description = "Identificador único do usuário.", example = "1", required = true)
    private Long idUsuario;

    @Schema(description = "Nome do usuário.", example = "João Silva", required = true)
    private String nome;

    @Schema(description = "E-mail do usuário.", example = "joao.silva@exemplo.com", required = true)
    private String email;

    @Schema(description = "Senha do usuário.", example = "senha123", required = true)
    private String senha;

    @Schema(description = "Confirmação da senha do usuário.", example = "senha123", required = true)
    private String confirmarSenha;

    @Schema(description = "CPF do usuário.", example = "123.456.789-00", required = true)
    private String cpf;

    @Schema(description = "Telefone do usuário.", example = "(11) 98765-4321", required = true)
    private String telefone;

    @Schema(description = "Data de nascimento do usuário.", example = "01/01/1990", required = true)
    private String dataNasc;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
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
