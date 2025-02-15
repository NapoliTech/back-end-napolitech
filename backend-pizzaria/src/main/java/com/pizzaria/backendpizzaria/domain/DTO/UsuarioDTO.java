package com.pizzaria.backendpizzaria.domain.DTO;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {

    private Long idUsuario;

    private String nome;

    private String email;

    private String senha;

    private String confirmarSenha;

    private String cpf;

    private String telefone;

    private String dataNasc;

}
