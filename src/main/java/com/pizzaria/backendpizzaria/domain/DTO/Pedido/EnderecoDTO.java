package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Representa os dados de um endereço no sistema.")
public class EnderecoDTO {

    @Schema(description = "Identificador único do endereço.", example = "1", required = true)
    private Long enderecoId;

    @Schema(description = "Nome da rua.", example = "Rua das Flores", required = true)
    private String rua;

    @Schema(description = "Nome do bairro.", example = "Centro", required = true)
    private String bairro;

    @Schema(description = "Número do endereço.", example = "123", required = true)
    private int numero;

    @Schema(description = "Complemento do endereço.", example = "Apartamento 101", required = false)
    private String complemento;

    @Schema(description = "Nome da cidade.", example = "São Paulo", required = true)
    private String cidade;

    @Schema(description = "Nome do estado.", example = "SP", required = true)
    private String estado;

    @Schema(description = "CEP do endereço.", example = "01001-000", required = true)
    private String cep;

    @Schema(description = "Identificador do usuário associado ao endereço.", example = "1", required = true)
    private Long usuarioId;


    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}
