package com.pizzaria.backendpizzaria.domain.DTO.Login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Representa os dados necessários para realizar login.")
public class LoginDTO {

    @Schema(description = "E-mail do usuário.", example = "usuario@exemplo.com", required = true)
    private String email;

    @Schema(description = "Senha do usuário.", example = "senha123", required = true)
    private String senha;

    public LoginDTO() {
    }

    public LoginDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}