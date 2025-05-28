package com.pizzaria.backendpizzaria.domain.DTO.Dashboard;

import java.util.HashMap;

public class VendaPorPeriodoResponseDTO {
    //Deve listar a quantidade de pedidos que foram vendidos nos ultimos 7 dias
    // Seg = 53, Ter = 12, Qua = 45, Qui = 22
    private HashMap<String, Integer> pedidosPorDia;
}
