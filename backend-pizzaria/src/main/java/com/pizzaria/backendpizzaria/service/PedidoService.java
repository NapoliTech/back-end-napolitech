package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ItemPedidoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.PedidoDTO;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.domain.ItemPedido;
import com.pizzaria.backendpizzaria.domain.Pedido;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.ProdutoRepository;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private UsuarioRepository clienteRepository;


    @Transactional
    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getClienteId() == null) {
            throw new RuntimeException("Cliente ID não pode ser nulo.");
        }

        Usuario cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + pedidoDTO.getClienteId() + " não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido = pedidoRepository.save(pedido);

        List<ItemPedido> itens = new ArrayList<>();
        double valorTotal = 0.0;

        for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
            if (itemDTO.getProduto() == null) {
                throw new ValidationException("Produto ID não pode ser nulo.");
            }

            Produto produto = produtoRepository.findById(Math.toIntExact(Long.parseLong(itemDTO.getProduto())))
                    .orElseThrow(() -> new ValidationException("Produto com ID " + itemDTO.getProduto() + " não encontrado"));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoTotal(produto.getPreco() * itemDTO.getQuantidade());
            item.setPedido(pedido);
            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade() );

            produtoRepository.save(produto);
            itens.add(item);
            valorTotal += item.getPrecoTotal();
        }

        pedido.setStatusPedido(StatusPedido.CRIADO);
        pedido.setNomeCliente(cliente.getNome());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setItens(itens);
        pedido.setPrecoTotal(valorTotal);
        pedidoRepository.save(pedido);

        return pedido;
    }

    public Pedido atualizarStatusPedido(Long id, String statusPedido) {
        Pedido pedido = pedidoRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new RuntimeException("Pedido com ID " + id + " não encontrado"));

        try {
            StatusPedido status = StatusPedido.valueOf(statusPedido.toUpperCase());
            pedido.setStatusPedido(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Status inválido: " + statusPedido);
        }

        return pedidoRepository.save(pedido);
    }

    public Optional<Pedido> listarPedidoPorId(Long id) {
        return pedidoRepository.findById(Math.toIntExact(id));
    }

    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

}
