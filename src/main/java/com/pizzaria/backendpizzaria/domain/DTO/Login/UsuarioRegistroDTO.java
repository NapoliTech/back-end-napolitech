package com.pizzaria.backendpizzaria.domain.DTO.Login;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para registro de um novo usuário.")
public class UsuarioRegistroDTO {

    @Schema(description = "Nome do usuário.", example = "João Silva", required = true)
    @NotBlank
    private String nome;

    @Schema(description = "E-mail do usuário.", example = "joao.silva@exemplo.com", required = true)
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Data de nascimento do usuário.", example = "01/01/1990", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private String dataNasc;

    @Schema(description = "CPF do usuário.", example = "123.456.789-00", required = true)
    @NotBlank
    private String cpf;

    @Schema(description = "Senha do usuário.", example = "senha123", required = true)
    @NotBlank
    private String senha;

    @Schema(description = "Confirmação da senha do usuário.", example = "senha123", required = true)
    @NotBlank
    private String confirmarSenha;

    @Schema(description = "Telefone do usuário.", example = "(11) 98765-4321", required = true)
    @NotBlank
    private String telefone;

    public UsuarioRegistroDTO(String nome, String email, String dataNasc, String cpf, String senha, String confirmarSenha, String telefone) {
        this.nome = nome;
        this.email = email;
        this.dataNasc = dataNasc;
        this.cpf = cpf;
        this.senha = senha;
        this.confirmarSenha = confirmarSenha;
        this.telefone = telefone;
    }

    public UsuarioRegistroDTO() {
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}