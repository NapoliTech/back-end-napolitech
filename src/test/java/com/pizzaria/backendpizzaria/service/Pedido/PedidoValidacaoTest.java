package com.pizzaria.backendpizzaria.service.Pedido;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ItemPedidoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.PedidoDTO;
import com.pizzaria.backendpizzaria.domain.Enum.TamanhoPizza;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.domain.Endereco;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import com.pizzaria.backendpizzaria.repository.ProdutoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoValidacaoTest {

    private PedidoValidacao pedidoValidacao;

    @BeforeEach
    void setUp() {
        pedidoValidacao = new PedidoValidacao(
                mock(PedidoRepository.class),
                mock(ProdutoRepository.class),
                mock(UsuarioRepository.class)
        );
    }

    @Test
    void validarEnderecoCliente_sucesso() {
        Usuario usuario = new Usuario();
        usuario.setEndereco(new Endereco());
        assertDoesNotThrow(() -> pedidoValidacao.validarEnderecoCliente(usuario));
    }

    @Test
    void validarEnderecoCliente_semEndereco() {
        Usuario usuario = new Usuario();
        usuario.setEndereco(null);
        ValidationException ex = assertThrows(ValidationException.class, () -> pedidoValidacao.validarEnderecoCliente(usuario));
        assertTrue(ex.getMessage().contains("O cliente não possui um endereço cadastrado"));
    }

    @Test
    void validarTipoEntrega_sucesso() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setTipoEntrega(com.pizzaria.backendpizzaria.domain.Enum.TipoEntrega.DELIVERY);
        assertDoesNotThrow(() -> pedidoValidacao.validarTipoEntrega(pedidoDTO));
    }

    @Test
    void validarTipoEntrega_nulo() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setTipoEntrega(null);
        ValidationException ex = assertThrows(ValidationException.class, () -> pedidoValidacao.validarTipoEntrega(pedidoDTO));
        assertTrue(ex.getMessage().contains("Tipo de entrega não pode ser nulo"));
    }

    @Test
    void validarPedidoDTO_sucesso() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(1L);
        assertDoesNotThrow(() -> pedidoValidacao.validarPedidoDTO(pedidoDTO));
    }

    @Test
    void validarPedidoDTO_clienteIdNulo() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(null);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> pedidoValidacao.validarPedidoDTO(pedidoDTO));
        assertTrue(ex.getMessage().contains("Cliente ID não pode ser nulo"));
    }

    @Test
    void verificarTamanhoPizza_sucesso() {
        assertDoesNotThrow(() -> pedidoValidacao.verificarTamanhoPizza(TamanhoPizza.GRANDE));
    }

    @Test
    void verificarTamanhoPizza_invalido() {
        TamanhoPizza tamanhoInvalido = mock(TamanhoPizza.class);
        when(tamanhoInvalido.name()).thenReturn("INEXISTENTE");
        ValidationException ex = assertThrows(ValidationException.class, () -> pedidoValidacao.verificarTamanhoPizza(tamanhoInvalido));
        assertTrue(ex.getMessage().contains("Tamanho de pizza inválido"));
    }

    @Test
    void validarItemPedidoDTO_sucesso() {
        ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO(Collections.singletonList(1), 1, TamanhoPizza.GRANDE, null);
        assertDoesNotThrow(() -> pedidoValidacao.validarItemPedidoDTO(itemPedidoDTO));
    }

    @Test
    void validarItemPedidoDTO_produtoNulo() {
        ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO(null, 1, TamanhoPizza.GRANDE, null);
        ValidationException ex = assertThrows(ValidationException.class, () -> pedidoValidacao.validarItemPedidoDTO(itemPedidoDTO));
        assertTrue(ex.getMessage().contains("Produto ID não pode ser nulo ou vazio"));
    }

    @Test
    void validarItemPedidoDTO_produtoVazio() {
        ItemPedidoDTO itemPedidoDTO = new ItemPedidoDTO(Collections.emptyList(), 1, TamanhoPizza.GRANDE, null);
        ValidationException ex = assertThrows(ValidationException.class, () -> pedidoValidacao.validarItemPedidoDTO(itemPedidoDTO));
        assertTrue(ex.getMessage().contains("Produto ID não pode ser nulo ou vazio"));
    }
}