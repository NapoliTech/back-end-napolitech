package com.pizzaria.backendpizzaria.domain.DTO;

import com.pizzaria.backendpizzaria.domain.Usuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UsuarioCreatedDTO {
    Long id;
    String nome;
    String email;
    String dataNasc;
    String cpf;
    String tipoUsuario;
    String telefone;

    public UsuarioCreatedDTO(Usuario usuario) {
        this.id = usuario.getIdUsuario();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.dataNasc = usuario.getDataNasc();
        this.cpf = usuario.getCpf();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.telefone = usuario.getTelefone();
    }
}