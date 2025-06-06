package com.pizzaria.backendpizzaria.service.Dashboard;

import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.domain.ItemPedido;
import com.pizzaria.backendpizzaria.domain.Pedido;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KpiService {
    private final PedidoRepository pedidoRepository;

    public KpiService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Map<String, Object> calcularKpis() {
        Map<String, Object> kpis = new HashMap<>();

        kpis.putAll(calcularKpisBasicos());

        kpis.put("faturamento", calcularFaturamentoAnual(LocalDateTime.now().getYear()));

        return kpis;
    }

    public Map<String, Object> calcularFaturamentoAnual(int ano) {
        String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        List<Map<String, Object>> faturamentoAnual = new ArrayList<>();

        int mesAtual = LocalDateTime.now().getMonthValue();

        for (int mes = 1; mes <= mesAtual; mes++) {
            Double valorMes = calcularFaturamentoMensal(mes, ano);
            Map<String, Object> dadosMes = new HashMap<>();
            dadosMes.put("mes", meses[mes - 1]);
            dadosMes.put("valor", valorMes);
            faturamentoAnual.add(dadosMes);
        }

        return Map.of("faturamentoAnual", faturamentoAnual);
    }

    public Double calcularFaturamentoMensal(int mes, int ano) {
        LocalDateTime inicio = LocalDateTime.of(ano, mes, 1, 0, 0);
        LocalDateTime fim = inicio.plusMonths(1).minusSeconds(1);

        List<Pedido> pedidosDoMes = pedidoRepository.findByDataPedidoBetween(inicio, fim);

        return pedidosDoMes.stream()
                .mapToDouble(Pedido::getPrecoTotal)
                .sum();
    }

    private Map<String, Object> calcularKpisBasicos() {
        LocalDateTime hoje = LocalDateTime.now();
        LocalDateTime inicioDia = hoje.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimDia = hoje.withHour(23).withMinute(59).withSecond(59);

        List<Pedido> pedidosHoje = pedidoRepository.findByDataPedidoBetween(inicioDia, fimDia);
        List<Pedido> todosPedidos = pedidoRepository.findAll();

        Double faturamentoHoje = pedidosHoje.stream()
                .mapToDouble(Pedido::getPrecoTotal)
                .sum();

        Double faturamentoTotal = todosPedidos.stream()
                .mapToDouble(Pedido::getPrecoTotal)
                .sum();

        Map<String, Object> kpis = new HashMap<>();
        kpis.put("pedidosHoje", pedidosHoje.size());
        kpis.put("faturamentoHoje", faturamentoHoje);
        kpis.put("totalPedidos", todosPedidos.size());
        kpis.put("faturamentoTotal", faturamentoTotal);

        return kpis;
    }

    public Map<String, Object> calcularVendasUltimosSete() {
        LocalDateTime hoje = LocalDateTime.now();
        List<Map<String, Object>> vendasPorDia = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDateTime data = hoje.minusDays(i);
            LocalDateTime inicioDia = data.withHour(0).withMinute(0).withSecond(0);
            LocalDateTime fimDia = data.withHour(23).withMinute(59).withSecond(59);

            List<Pedido> pedidosDoDia = pedidoRepository.findByDataPedidoBetween(inicioDia, fimDia);

            Map<String, Object> vendaDia = new HashMap<>();
            vendaDia.put("dia", data.getDayOfMonth());
            vendaDia.put("quantidade", pedidosDoDia.size());
            vendasPorDia.add(vendaDia);
        }

        return Map.of("vendasPorDia", vendasPorDia);
    }

    public Map<String, Object> calcularVendasPorCategoria(int mes, int ano) {
        LocalDateTime inicio = LocalDateTime.of(ano, mes, 1, 0, 0);
        LocalDateTime fim = inicio.plusMonths(1).minusSeconds(1);

        List<Pedido> pedidosDoMes = pedidoRepository.findByDataPedidoBetween(inicio, fim);

        Map<String, Integer> vendasPorCategoria = pedidosDoMes.stream()
                .flatMap(pedido -> pedido.getItens().stream())
                .collect(Collectors.groupingBy(
                        item -> String.valueOf(item.getProduto().getCategoriaProduto()),
                        Collectors.summingInt(ItemPedido::getQuantidade)
                ));

        return Map.of("vendasPorCategoria", vendasPorCategoria);
    }

    public Map<String, Object> calcularKpisCards() {
        LocalDateTime hoje = LocalDateTime.now();
        LocalDateTime inicioDia = hoje.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fimDia = hoje.withHour(23).withMinute(59).withSecond(59);

        List<Pedido> todosPedidos = pedidoRepository.findAll();
        List<Pedido> pedidosHoje = pedidoRepository.findByDataPedidoBetween(inicioDia, fimDia);

        long pedidosFinalizados = todosPedidos.stream()
                .filter(p -> p.getStatusPedido() == StatusPedido.ENCERRADO)
                .count();

        long pedidosEmAberto = todosPedidos.stream()
                .filter(p -> p.getStatusPedido() != StatusPedido.ENCERRADO)
                .count();

        double faturamentoDiario = pedidosHoje.stream()
                .mapToDouble(Pedido::getPrecoTotal)
                .sum();

        Map<String, Object> kpis = new HashMap<>();
        kpis.put("pedidosTotais", todosPedidos.size());
        kpis.put("pedidosFinalizados", pedidosFinalizados);
        kpis.put("pedidosEmAberto", pedidosEmAberto);
        kpis.put("faturamentoDiario", faturamentoDiario);

        return Map.of("kpis", kpis);
    }
}