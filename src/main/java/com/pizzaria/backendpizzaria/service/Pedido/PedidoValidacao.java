package com.pizzaria.backendpizzaria.service.Pedido;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ItemPedidoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.PedidoDTO;
import com.pizzaria.backendpizzaria.domain.Enum.TamanhoPizza;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import com.pizzaria.backendpizzaria.repository.ProdutoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class PedidoValidacao {

    private final PedidoRepository pedidoRepository;

    private final ProdutoRepository produtoRepository;

    private final UsuarioRepository clienteRepository;

    public PedidoValidacao(PedidoRepository pedidoRepository, ProdutoRepository produtoRepository, UsuarioRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    public void validarEnderecoCliente(Usuario cliente) {
        if (cliente.getEndereco() == null) {
            throw new ValidationException("O cliente não possui um endereço cadastrado. Por favor, cadastre um endereço.");
        }
    }

    public void validarTipoEntrega(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getTipoEntrega() == null) {
            throw new ValidationException("Tipo de entrega não pode ser nulo.");
        }
    }

    public void validarPedidoDTO(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getClienteId() == null) {
            throw new RuntimeException("Cliente ID não pode ser nulo.");
        }
    }

    public void verificarTamanhoPizza(TamanhoPizza tamanhoPizza) {
        try {
            TamanhoPizza.valueOf(tamanhoPizza.name());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Tamanho de pizza inválido: " + tamanhoPizza);
        }
    }

    public void validarItemPedidoDTO(ItemPedidoDTO itemDTO) {
        if (itemDTO.getProduto() == null || itemDTO.getProduto().isEmpty()) {
            throw new ValidationException("Produto ID não pode ser nulo ou vazio.");
        }
    }


}
