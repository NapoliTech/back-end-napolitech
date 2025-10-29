package com.pizzaria.backendpizzaria.service.Dashboard;

import com.pizzaria.backendpizzaria.domain.Enum.CategoriaProduto;
import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.ItemPedido;
import com.pizzaria.backendpizzaria.domain.Pedido;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class KpiServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private KpiService kpiService;

    private Pedido pedido1;
    private Pedido pedido2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pedido1 = new Pedido();
        pedido1.setPrecoTotal(100.0);
        pedido1.setStatusPedido(StatusPedido.ENCERRADO);

        pedido2 = new Pedido();
        pedido2.setPrecoTotal(200.0);
        pedido2.setStatusPedido(StatusPedido.EM_PREPARO);
    }

    @Test
    @DisplayName("Deve calcular faturamento mensal corretamente")
    void deveCalcularFaturamentoMensal() {
        int mes = 10;
        int ano = 2025;

        when(pedidoRepository.findByDataPedidoBetween(any(), any()))
                .thenReturn(List.of(pedido1, pedido2));

        Double total = kpiService.calcularFaturamentoMensal(mes, ano);

        assertEquals(300.0, total);
        verify(pedidoRepository, times(1))
                .findByDataPedidoBetween(any(), any());
    }

    @Test
    @DisplayName("Deve calcular faturamento anual com meses até o atual")
    void deveCalcularFaturamentoAnual() {
        when(pedidoRepository.findByDataPedidoBetween(any(), any()))
                .thenReturn(List.of(pedido1));

        Map<String, Object> result = kpiService.calcularFaturamentoAnual(LocalDateTime.now().getYear());

        assertTrue(result.containsKey("faturamentoAnual"));
        List<Map<String, Object>> lista = (List<Map<String, Object>>) result.get("faturamentoAnual");
        assertFalse(lista.isEmpty());
        assertTrue(lista.get(0).containsKey("mes"));
        assertTrue(lista.get(0).containsKey("valor"));
    }

    @Test
    @DisplayName("Deve calcular KPIs básicos dentro do calcularKpis")
    void deveCalcularKpisComFaturamento() {
        when(pedidoRepository.findByDataPedidoBetween(any(), any()))
                .thenReturn(List.of(pedido1, pedido2));
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

        Map<String, Object> result = kpiService.calcularKpis();

        assertTrue(result.containsKey("faturamento"));
        assertTrue(result.containsKey("pedidosHoje"));
        assertTrue(result.containsKey("faturamentoHoje"));
        assertTrue(result.containsKey("totalPedidos"));
        assertTrue(result.containsKey("faturamentoTotal"));
    }

    @Test
    @DisplayName("Deve calcular vendas dos últimos 7 dias")
    void deveCalcularVendasUltimosSeteDias() {
        when(pedidoRepository.findByDataPedidoBetween(any(), any()))
                .thenReturn(List.of(pedido1));

        Map<String, Object> result = kpiService.calcularVendasUltimosSete();

        assertTrue(result.containsKey("vendasPorDia"));

        List<Map<String, Object>> vendas = (List<Map<String, Object>>) result.get("vendasPorDia");
        assertEquals(7, vendas.size());
        assertTrue(vendas.get(0).containsKey("dia"));
        assertTrue(vendas.get(0).containsKey("quantidade"));
    }

    @Test
    @DisplayName("Deve calcular vendas por categoria corretamente")
    void deveCalcularVendasPorCategoria() {
        Produto produto1 = new Produto();
        produto1.setCategoriaProduto(CategoriaProduto.PIZZA);

        Produto produto2 = new Produto();
        produto2.setCategoriaProduto(CategoriaProduto.BEBIDAS);

        ItemPedido item1 = new ItemPedido();
        item1.setProduto(produto1);
        item1.setQuantidade(2);

        ItemPedido item2 = new ItemPedido();
        item2.setProduto(produto2);
        item2.setQuantidade(3);

        Pedido pedido = new Pedido();
        pedido.setItens(List.of(item1, item2));

        when(pedidoRepository.findByDataPedidoBetween(any(), any()))
                .thenReturn(List.of(pedido));

        Map<String, Object> result = kpiService.calcularVendasPorCategoria(10, 2025);

        assertTrue(result.containsKey("vendasPorCategoria"));
        @SuppressWarnings("unchecked")
        Map<String, Integer> categorias = (Map<String, Integer>) result.get("vendasPorCategoria");


        String keyPizza = CategoriaProduto.PIZZA.toString();
        String keyBebidas = CategoriaProduto.BEBIDAS.toString();

        assertEquals(2, categorias.size());
        assertEquals(2, categorias.get(keyPizza).intValue());
        assertEquals(3, categorias.get(keyBebidas).intValue());
    }


    @Test
    @DisplayName("Deve calcular KPIs dos cards corretamente")
    void deveCalcularKpisCards() {
        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));
        when(pedidoRepository.findByDataPedidoBetween(any(), any()))
                .thenReturn(List.of(pedido1));

        Map<String, Object> result = kpiService.calcularKpisCards();

        assertTrue(result.containsKey("kpis"));
        Map<String, Object> kpis = (Map<String, Object>) result.get("kpis");

        assertEquals(2, kpis.get("pedidosTotais"));
        assertEquals(1L, kpis.get("pedidosFinalizados"));
        assertEquals(1L, kpis.get("pedidosEmAberto"));
        assertEquals(100.0, kpis.get("faturamentoDiario"));
    }
}
