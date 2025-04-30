package com.pizzaria.backendpizzaria.domain.DTO.Login;

import com.pizzaria.backendpizzaria.domain.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de um usuário criado no sistema.")
public class UsuarioCreatedDTO {

    @Schema(description = "Identificador único do usuário.", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome do usuário.", example = "João Silva", required = true)
    private String nome;

    @Schema(description = "E-mail do usuário.", example = "joao.silva@exemplo.com", required = true)
    private String email;

    @Schema(description = "Data de nascimento do usuário.", example = "01/01/1990", required = true)
    private String dataNasc;

    @Schema(description = "CPF do usuário.", example = "123.456.789-00", required = true)
    private String cpf;

    @Schema(description = "Tipo de usuário.", example = "ADMIN", required = true)
    private String tipoUsuario;

    @Schema(description = "Telefone do usuário.", example = "(11) 98765-4321", required = true)
    private String telefone;


    public UsuarioCreatedDTO(Usuario usuario) {
        this.id = usuario.getIdUsuario();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dataNasc = usuario.getDataNasc();
        this.cpf = usuario.getCpf();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.telefone = usuario.getTelefone();
    }

    public UsuarioCreatedDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}