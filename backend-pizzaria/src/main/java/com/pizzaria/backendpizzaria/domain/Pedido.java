package com.pizzaria.backendpizzaria.domain;

import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pizzaria.backendpizzaria.domain.Enum.StatusPedido.CRIADO;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @OneToOne
    private Endereco endereco;

    private String nomeCliente;

    private StatusPedido statusPedido;

    private Double precoTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();

    private LocalDateTime dataPedido;

    public Pedido() {
    }

    public void calcularValorTotal() {
        this.precoTotal = itens.stream()
                .mapToDouble(ItemPedido::getPrecoTotal)
                .sum();
    }

    public Pedido(Long id, Usuario cliente, String nomeCliente, StatusPedido statusPedido, Double precoTotal, List<ItemPedido> itens, LocalDateTime dataPedido) {
        this.id = id;
        this.cliente = cliente;
        this.nomeCliente = nomeCliente;
        this.statusPedido = CRIADO;
        this.precoTotal = precoTotal;
        this.itens = itens;
        this.dataPedido = dataPedido;
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
