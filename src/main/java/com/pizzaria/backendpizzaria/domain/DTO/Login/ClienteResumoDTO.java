package com.pizzaria.backendpizzaria.domain.DTO.Login;

public class ClienteResumoDTO {
    private String nome;
    private String email;
    private String telefone;
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
