package com.pizzaria.backendpizzaria.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String rua;
    @NotEmpty
    private String bairro;
    @NotNull
    private int numero;

    private String complemento;
    @NotEmpty
    private String cidade;
    @NotEmpty
    private String estado;
    @NotEmpty
    private String cep;

    @OneToOne(mappedBy = "endereco")
    private Usuario usuario;

}
