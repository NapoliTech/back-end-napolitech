package com.pizzaria.backendpizzaria.domain.DTO.Pedido;

import com.pizzaria.backendpizzaria.domain.Enum.BordaRecheada;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.Enum.TipoEntrega;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Representa os dados de um pedido no sistema.")
public class PedidoDTO {

    @Schema(description = "Identificador único do pedido.", example = "1", required = true)
    private Long pedidoId;

    @Schema(description = "Identificador do cliente associado ao pedido.", example = "1", required = true)
    private Long clienteId;

    @Schema(description = "Nome do cliente.", example = "João Silva", required = true)
    private String nomeCliente;

    @Schema(description = "Valor total do pedido.", example = "89.90", required = true)
    private Double valorTotal;

    @Schema(description = "Lista de itens do pedido.", required = true)
    private List<ItemPedidoDTO> itens;

    @Schema(description = "Identificador do endereço associado ao pedido.", example = "1", required = true)
    private Long enderecoId;

    @Schema(description = "Telefone do cliente.", example = "(11) 98765-4321", required = true)
    private String telefone;

    @Schema(description = "Observações adicionais sobre o pedido.", example = "Sem cebola", required = false)
    private String observacao;

    @Schema(description = "Status atual do pedido.", example = "RECEBIDO", required = true)
    private StatusPedido statusPedido;

    @Schema(description = "Tipo de entrega do pedido.", example = "DELIVERY", required = true)
    private TipoEntrega tipoEntrega;

    @Schema(description = "Tipo de borda recheada, se aplicável.", example = "CATUPIRY", required = false)
    private BordaRecheada bordaRecheada;

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public BordaRecheada getBordaRecheada() {
        return bordaRecheada;
    }

    public void setBordaRecheada(BordaRecheada bordaRecheada) {
        this.bordaRecheada = bordaRecheada;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }
}