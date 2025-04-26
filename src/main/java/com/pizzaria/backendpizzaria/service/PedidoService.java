package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.domain.*;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.EnderecoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ItemPedidoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.PedidoDTO;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.Enum.TamanhoPizza;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.EnderecoRepository;
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
    @Autowired
    private EnderecoRepository enderecoRepository;

    @Transactional
    public Pedido criarPedido(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getClienteId() == null) {
            throw new RuntimeException("Cliente ID não pode ser nulo.");
        }


        Usuario cliente = clienteRepository.findById(pedidoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente com ID " + pedidoDTO.getClienteId() + " não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        Endereco endereco = cliente.getEndereco();
        if (endereco == null) {
            throw new ValidationException("O cliente não possui um endereço cadastrado. Por favor, cadastre um endereço.");
        }

        List<ItemPedido> itens = new ArrayList<>();
        double valorTotal = 0.0;

        for (ItemPedidoDTO itemDTO : pedidoDTO.getItens()) {
            if (itemDTO.getProduto() == null || itemDTO.getProduto().isEmpty()) {
                throw new ValidationException("Produto ID não pode ser nulo ou vazio.");
            }

            List<Produto> produtos = new ArrayList<>();
            for (Integer produtoId : itemDTO.getProduto()) {
                Produto produto = produtoRepository.findById(produtoId)
                        .orElseThrow(() -> new ValidationException("Produto com ID " + produtoId + " não encontrado"));
                produtos.add(produto);
            }

            try {
                TamanhoPizza.valueOf(itemDTO.getTamanhoPizza().name());
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Tamanho de pizza inválido: " + itemDTO.getTamanhoPizza());
            }

            if (itemDTO.getTamanhoPizza() == TamanhoPizza.MEIO_A_MEIO) {
                if (produtos.size() != 2) {
                    throw new ValidationException("Pizzas MEIO_A_MEIO devem conter exatamente 2 sabores.");
                }

                for (Produto produto : produtos) {
                    ItemPedido item = new ItemPedido();
                    item.setQuantidade(itemDTO.getQuantidade());
                    item.setTamanhoPizza(itemDTO.getTamanhoPizza());
                    item.setProduto(produto);

                    double precoBase = produto.getPreco() / 2;
                    item.setPrecoTotal(precoBase * itemDTO.getQuantidade());
                    item.setPedido(pedido);

                    produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - item.getQuantidade());
                    produtoRepository.save(produto);

                    itens.add(item);
                    valorTotal += item.getPrecoTotal();
                }
            } else {
                ItemPedido item = new ItemPedido();
                item.setQuantidade(itemDTO.getQuantidade());
                item.setTamanhoPizza(itemDTO.getTamanhoPizza());
                item.setProduto(produtos.get(0));

                double precoBase = produtos.stream().mapToDouble(Produto::getPreco).average().orElse(0.0);
                if (itemDTO.getTamanhoPizza() == TamanhoPizza.BROTO) {
                    precoBase /= 2;
                } else if (itemDTO.getTamanhoPizza() == TamanhoPizza.TREM) {
                    precoBase *= 3;
                }

                item.setPrecoTotal(precoBase * itemDTO.getQuantidade());
                item.setPedido(pedido);

                produtos.get(0).setQuantidadeEstoque(produtos.get(0).getQuantidadeEstoque() - item.getQuantidade());
                produtoRepository.save(produtos.get(0));

                itens.add(item);
                valorTotal += item.getPrecoTotal();
            }
        }

        pedido.setStatusPedido(StatusPedido.RECEBIDO);
        pedido.setNomeCliente(cliente.getNome());
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setCliente(cliente);
        pedido.setItens(itens);
        pedido.setPrecoTotal(valorTotal);
        pedido.setObservacao(pedidoDTO.getObservacao());
        pedido.setEndereco(endereco);
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
