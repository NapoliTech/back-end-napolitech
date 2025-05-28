package com.pizzaria.backendpizzaria.service.Dashboard;

import com.pizzaria.backendpizzaria.domain.Enum.StatusPedido;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class KpiService {

    private final PedidoRepository pedidoRepository;

    public KpiService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public Map<String, Object> calcularKpis() {
        Map<String, Object> kpis = new HashMap<>();

        int pedidosTotais = pedidoRepository.countAllBy();
        kpis.put("pedidosTotais", pedidosTotais);

        int pedidosFinalizados = pedidoRepository.countByStatusPedidoAndDataPedidoBetween(
                StatusPedido.ENCERRADO, LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
        kpis.put("pedidosFinalizados", pedidosFinalizados);

        int pedidosEmAberto = pedidoRepository.countByStatusPedidoAndDataPedidoBetween(
                StatusPedido.RECEBIDO, LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
        kpis.put("pedidosEmAberto", pedidosEmAberto);

        Double faturamentoDiario = pedidoRepository.sumPrecoTotalByDataPedidoBetween(
                LocalDate.now().atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay());
        kpis.put("faturamentoDiario", faturamentoDiario != null ? faturamentoDiario : 0.0);

        return kpis;
    }
}