package com.pizzaria.backendpizzaria.service.Usuario;

import com.pizzaria.backendpizzaria.config.JwtUtil;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Service
public class UsuarioValidacao {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UsuarioValidacao(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public void validarCampos(UsuarioRegistroDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new ValidationException("Email já está em uso");
        }

        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isBlank() ||
                usuarioDTO.getSenha() == null || usuarioDTO.getSenha().isBlank() ||
                usuarioDTO.getConfirmarSenha() == null || usuarioDTO.getConfirmarSenha().isBlank() ||
                usuarioDTO.getNome() == null || usuarioDTO.getNome().isBlank() ||
                usuarioDTO.getCpf() == null || usuarioDTO.getCpf().isBlank() ||
                usuarioDTO.getDataNasc() == null || usuarioDTO.getTelefone() == null || usuarioDTO.getTelefone().isBlank()) {
            throw new ValidationException( "Todos os campos são obrigatórios");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(usuarioDTO.getDataNasc(), formatter);
        } catch (DateTimeParseException e) {
            throw new ValidationException("A data de nascimento deve estar no formato dd/MM/yyyy.");
        }

        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new ValidationException("A data de nascimento não pode estar no futuro.");
        }

        if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmarSenha())) {
            throw new ValidationException("As senhas não coincidem");
        }

        if (!usuarioDTO.getNome().contains(" ")) {
            throw new ValidationException("O nome deve conter pelo menos um sobrenome");
        }
    }

    public void validarCPF(String cpf) {
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        if (!Pattern.matches(regex, cpf)) {
            throw new ValidationException("CPF inválido. O formato correto é XXX.XXX.XXX-XX");
        }
    }

    public void validarTelefone(String telefone) {
        String telefoneNumerico = telefone.replaceAll("[^0-9]", "");

        if (telefoneNumerico.length() < 10 || telefoneNumerico.length() > 11) {
            throw new ValidationException("Número de telefone inválido. Formatos aceitos: (11) 98765-4321 ou 11987654321.");
        }
    }

}
