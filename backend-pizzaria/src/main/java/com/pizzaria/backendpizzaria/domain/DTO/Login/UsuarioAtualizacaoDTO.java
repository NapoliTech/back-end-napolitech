package com.pizzaria.backendpizzaria.domain.DTO.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAtualizacaoDTO {

    private String nome;

    private String email;

    private String senha;

    private String cpf;

    private String telefone;

    private String dataNasc;
}
