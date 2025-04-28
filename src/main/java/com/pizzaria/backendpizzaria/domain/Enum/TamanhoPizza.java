package com.pizzaria.backendpizzaria.domain.Enum;

public enum TamanhoPizza {
    BROTO(2),
    GRANDE(1),
    TREM(4),
    MEIO_A_MEIO(2);

    private BordaRecheada bordaRecheada;

    private Integer qntFatias;
    private Double valorAdicional;

    TamanhoPizza(Integer qntFatias) {
        this.qntFatias = qntFatias;
    }

    public BordaRecheada getBordaRecheada() {
        return bordaRecheada;
    }
}
