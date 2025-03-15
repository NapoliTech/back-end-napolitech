package com.pizzaria.backendpizzaria.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios_cadastrados")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @NotBlank
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    @NotBlank
    private String cpf;

    @Transient
    private String confirmarSenha;

    @NotBlank
    private String telefone;

    @NotNull
    private Long pedidos;

    @NotBlank
    private String dataNasc;

    @NotBlank
    private String tipoUsuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario", referencedColumnName = "idUsuario")
    private Endereco endereco;
}
