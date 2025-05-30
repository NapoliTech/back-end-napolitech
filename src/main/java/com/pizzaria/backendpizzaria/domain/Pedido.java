package com.pizzaria.backendpizzaria.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.Enum.TipoEntrega;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pizzaria.backendpizzaria.domain.Enum.StatusPedido.RECEBIDO;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    private String nomeCliente;

    private StatusPedido statusPedido;

    private Double precoTotal;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private TipoEntrega tipoEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ItemPedido> itens = new ArrayList<>();

    private LocalDateTime dataPedido;

    public Pedido() {
    }

    public void calcularValorTotal() {
        this.precoTotal = itens.stream()
                .mapToDouble(ItemPedido::getPrecoTotal)
                .sum();
    }

    public Pedido(Long id, Usuario cliente, String nomeCliente, StatusPedido statusPedido, Double precoTotal, List<ItemPedido> itens, LocalDateTime dataPedido, String observacao) {
        this.id = id;
        this.cliente = cliente;
        this.nomeCliente = nomeCliente;
        this.statusPedido = RECEBIDO;
        this.precoTotal = precoTotal;
        this.itens = itens;
        this.dataPedido = dataPedido;
        this.observacao = observacao;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public StatusPedido getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(StatusPedido statusPedido) {
        this.statusPedido = statusPedido;
    }

    public Double getPrecoTotal() {
        return precoTotal;
    }

    public void setPrecoTotal(Double precoTotal) {
        this.precoTotal = precoTotal;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }
}
