package com.pizzaria.backendpizzaria.domain.Enum;

public enum BordaRecheada {
    NORMAL(0.0),
    CHEDDAR(8.0),
    CATUPIRY(8.0),
    CHOCOLATE(10.0);

    private final Double valorAdicional;

    BordaRecheada(Double valorAdicional) {
        this.valorAdicional = valorAdicional;
    }

    public Double getValorAdicional() {
        return valorAdicional;
    }


}