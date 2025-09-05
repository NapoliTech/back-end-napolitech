package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.domain.*;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ItemPedidoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.PedidoDTO;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.Enum.TamanhoPizza;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.EnderecoRepository;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import com.pizzaria.backendpizzaria.repository.ProdutoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private UsuarioRepository clienteRepository;
    @Mock
    private EnderecoRepository enderecoRepository;

    private Usuario cliente;
    private Produto produto;
    private PedidoDTO pedidoDTO;
    private ItemPedidoDTO itemPedidoDTO;

    @BeforeEach
    void setUp() {
        cliente = new Usuario();
        cliente.setIdUsuario(1L);
        cliente.setNome("Cliente Teste");
        Endereco endereco = new Endereco();
        cliente.setEndereco(endereco);

        produto = new Produto();
        produto.setId(1L);
        produto.setPreco(50.0);
        produto.setQuantidadeEstoque(10);

        itemPedidoDTO = new ItemPedidoDTO(
                Collections.singletonList(1),
                2,
                TamanhoPizza.GRANDE,
                null
        );

        pedidoDTO = new PedidoDTO();
        pedidoDTO.setClienteId(1L);
        pedidoDTO.setItens(Collections.singletonList(itemPedidoDTO));
        pedidoDTO.setTipoEntrega(com.pizzaria.backendpizzaria.domain.Enum.TipoEntrega.DELIVERY);
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    void criarPedido_sucesso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pedido pedidoCriado = pedidoService.criarPedido(pedidoDTO);

        assertNotNull(pedidoCriado);
        assertEquals(StatusPedido.RECEBIDO, pedidoCriado.getStatusPedido());
        assertEquals(1, pedidoCriado.getItens().size());
        assertEquals(50.0 * 2, pedidoCriado.getPrecoTotal());
        verify(produtoRepository, times(1)).save(any(Produto.class));
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se cliente não existir")
    void criarPedido_clienteNaoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(RuntimeException.class, () -> pedidoService.criarPedido(pedidoDTO));
        assertTrue(ex.getMessage().contains("Cliente com ID 1 não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se cliente não tem endereço")
    void criarPedido_clienteSemEndereco() {
        cliente.setEndereco(null);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Exception ex = assertThrows(ValidationException.class, () -> pedidoService.criarPedido(pedidoDTO));
        assertTrue(ex.getMessage().contains("O cliente não possui um endereço cadastrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se produto não existir")
    void criarPedido_produtoNaoExiste() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());
        Exception ex = assertThrows(ValidationException.class, () -> pedidoService.criarPedido(pedidoDTO));
        assertTrue(ex.getMessage().contains("Produto com ID 1 não encontrado"));
    }

    @Test
    @DisplayName("Deve lançar exceção se item não tem produto")
    void criarPedido_itemSemProduto() {
        itemPedidoDTO.setProduto((List<Integer>) null);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        Exception ex = assertThrows(ValidationException.class, () -> pedidoService.criarPedido(pedidoDTO));
        assertTrue(ex.getMessage().contains("Produto ID não pode ser nulo ou vazio"));
    }
}