package com.pizzaria.backendpizzaria.controller.Dashboard;

import com.pizzaria.backendpizzaria.service.Dashboard.KpiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/kpis")
@Tag(name = "KPIs", description = "Endpoints para métricas e indicadores do dashboard.")
public class KpiController {

    private final KpiService kpiService;

    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    @Operation(summary = "Listar todos os KPIs", description = "Retorna todas as métricas do dashboard.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarKpis() {
        Map<String, Object> kpis = kpiService.calcularKpis();
        return ResponseEntity.ok(kpis);
    }

    @Operation(summary = "Calcular faturamento anual", description = "Retorna o faturamento mensal do ano especificado")
    @GetMapping("/faturamento/{ano}")
    public ResponseEntity<Map<String, Object>> getFaturamentoAnual(
            @Parameter(description = "Ano para cálculo", example = "2024")
            @PathVariable int ano) {
        try {
            Map<String, Object> faturamento = kpiService.calcularFaturamentoAnual(ano);
            return ResponseEntity.ok(faturamento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao calcular faturamento: " + e.getMessage()));
        }
    }

    @Operation(summary = "Calcular faturamento mensal", description = "Retorna o faturamento do mês e ano especificados")
    @GetMapping("/faturamento/{ano}/{mes}")
    public ResponseEntity<Map<String, Object>> getFaturamentoMensal(
            @Parameter(description = "Ano para cálculo", example = "2024") @PathVariable int ano,
            @Parameter(description = "Mês para cálculo (1-12)", example = "3") @PathVariable int mes) {
        try {
            Double faturamento = kpiService.calcularFaturamentoMensal(mes, ano);
            return ResponseEntity.ok(Map.of("valor", faturamento));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao calcular faturamento: " + e.getMessage()));
        }
    }

    @Operation(summary = "Vendas dos últimos 7 dias",
            description = "Retorna a quantidade de vendas para cada um dos últimos 7 dias")
    @GetMapping("/vendas/ultimos-sete-dias")
    public ResponseEntity<Map<String, Object>> getVendasUltimosSete() {
        try {
            Map<String, Object> vendas = kpiService.calcularVendasUltimosSete();
            return ResponseEntity.ok(vendas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao calcular vendas: " + e.getMessage()));
        }
    }

    @Operation(summary = "Vendas por categoria",
            description = "Retorna as vendas por categoria para o mês e ano especificados")
    @GetMapping("/vendas/categoria/{ano}/{mes}")
    public ResponseEntity<Map<String, Object>> getVendasPorCategoria(
            @Parameter(description = "Ano para cálculo", example = "2024") @PathVariable int ano,
            @Parameter(description = "Mês para cálculo (1-12)", example = "3") @PathVariable int mes) {
        try {
            Map<String, Object> vendas = kpiService.calcularVendasPorCategoria(mes, ano);
            return ResponseEntity.ok(vendas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao calcular vendas por categoria: " + e.getMessage()));
        }
    }

    @Operation(summary = "KPIs principais",
            description = "Retorna os indicadores principais do dashboard")
    @GetMapping("/cards")
    public ResponseEntity<Map<String, Object>> getKpisCards() {
        try {
            Map<String, Object> kpis = kpiService.calcularKpisCards();
            return ResponseEntity.ok(kpis);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao calcular KPIs: " + e.getMessage()));
        }
    }
}