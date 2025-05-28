package com.pizzaria.backendpizzaria.domain.DTO.Dashboard;

public class kpisResponseDTO {
    private Integer pedidosTotais;
    private Integer pedidosFinalizados; // Pedidos de hoje
    private Integer pedidosEmAberto; // Pedidos de hoje
    private Float faturamentoDiario;

    public Integer getPedidosTotais() {
        return pedidosTotais;
    }

    public void setPedidosTotais(Integer pedidosTotais) {
        this.pedidosTotais = pedidosTotais;
    }

    public Integer getPedidosFinalizados() {
        return pedidosFinalizados;
    }

    public void setPedidosFinalizados(Integer pedidosFinalizados) {
        this.pedidosFinalizados = pedidosFinalizados;
    }

    public Integer getPedidosEmAberto() {
        return pedidosEmAberto;
    }

    public void setPedidosEmAberto(Integer pedidosEmAberto) {
        this.pedidosEmAberto = pedidosEmAberto;
    }

    public Float getFaturamentoDiario() {
        return faturamentoDiario;
    }

    public void setFaturamentoDiario(Float faturamentoDiario) {
        this.faturamentoDiario = faturamentoDiario;
    }
}
