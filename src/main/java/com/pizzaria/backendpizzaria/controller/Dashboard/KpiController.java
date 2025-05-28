package com.pizzaria.backendpizzaria.controller.Dashboard;

import com.pizzaria.backendpizzaria.service.Dashboard.KpiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/kpis")
public class KpiController {

    private final KpiService kpiService;

    public KpiController(KpiService kpiService) {
        this.kpiService = kpiService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarKpis() {
        Map<String, Object> kpis = kpiService.calcularKpis();
        return ResponseEntity.ok(kpis);
    }
}