package com.pizzaria.backendpizzaria.domain;

import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pizzaria.backendpizzaria.domain.Enum.StatusPedido.CRIADO;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    private String nomeCliente;

    private StatusPedido statusPedido;

    private Double precoTotal;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();

    private LocalDateTime dataPedido;

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
}
