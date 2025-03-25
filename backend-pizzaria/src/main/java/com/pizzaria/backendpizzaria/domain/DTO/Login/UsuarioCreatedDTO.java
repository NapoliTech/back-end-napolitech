package com.pizzaria.backendpizzaria.domain.DTO.Login;

import com.pizzaria.backendpizzaria.domain.Usuario;

public class UsuarioCreatedDTO {
    Long id;
    String nome;
    String email;
    String dataNasc;
    String cpf;
    String tipoUsuario;
    String telefone;

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