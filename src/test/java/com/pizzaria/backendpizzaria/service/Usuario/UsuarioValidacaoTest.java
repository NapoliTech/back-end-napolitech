package com.pizzaria.backendpizzaria.service.Usuario;

import com.pizzaria.backendpizzaria.config.JwtUtil;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioValidacaoTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private UsuarioValidacao usuarioValidacao;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = mock(JwtUtil.class);
        usuarioValidacao = new UsuarioValidacao(usuarioRepository, passwordEncoder, jwtUtil);
    }

    private UsuarioRegistroDTO getUsuarioValido() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setEmail("teste@email.com");
        dto.setSenha("123456");
        dto.setConfirmarSenha("123456");
        dto.setNome("João Silva");
        dto.setCpf("123.456.789-00");
        dto.setDataNasc("01/01/2000");
        dto.setTelefone("(11) 98765-4321");
        return dto;
    }

    @Test
    void validarCampos_sucesso() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        assertDoesNotThrow(() -> usuarioValidacao.validarCampos(dto));
    }

    @Test
    void validarCampos_emailJaEmUso() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(true);
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCampos(dto));
        assertTrue(ex.getMessage().contains("Email já está em uso"));
    }

    @Test
    void validarCampos_camposObrigatoriosFaltando() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        dto.setEmail(null);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCampos(dto));
        assertTrue(ex.getMessage().contains("Todos os campos são obrigatórios"));
    }

    @Test
    void validarCampos_dataNascimentoFormatoInvalido() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        dto.setDataNasc("2000-01-01");
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCampos(dto));
        assertTrue(ex.getMessage().contains("A data de nascimento deve estar no formato dd/MM/yyyy."));
    }

    @Test
    void validarCampos_dataNascimentoNoFuturo() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        dto.setDataNasc("01/01/3000");
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCampos(dto));
        assertTrue(ex.getMessage().contains("A data de nascimento não pode estar no futuro."));
    }

    @Test
    void validarCampos_senhasNaoCoincidem() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        dto.setConfirmarSenha("diferente");
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCampos(dto));
        assertTrue(ex.getMessage().contains("As senhas não coincidem"));
    }

    @Test
    void validarCampos_nomeSemSobrenome() {
        UsuarioRegistroDTO dto = getUsuarioValido();
        dto.setNome("Joao");
        when(usuarioRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCampos(dto));
        assertTrue(ex.getMessage().contains("O nome deve conter pelo menos um sobrenome"));
    }

    @Test
    void validarCPF_sucesso() {
        assertDoesNotThrow(() -> usuarioValidacao.validarCPF("123.456.789-00"));
    }

    @Test
    void validarCPF_formatoInvalido() {
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarCPF("12345678900"));
        assertTrue(ex.getMessage().contains("CPF inválido. O formato correto é XXX.XXX.XXX-XX"));
    }

    @Test
    void validarTelefone_sucesso_10digitos() {
        assertDoesNotThrow(() -> usuarioValidacao.validarTelefone("1198765432"));
    }

    @Test
    void validarTelefone_sucesso_11digitos() {
        assertDoesNotThrow(() -> usuarioValidacao.validarTelefone("(11) 98765-4321"));
    }

    @Test
    void validarTelefone_numeroCurto() {
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarTelefone("12345"));
        assertTrue(ex.getMessage().contains("Número de telefone inválido"));
    }

    @Test
    void validarTelefone_numeroLongo() {
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioValidacao.validarTelefone("119876543210"));
        assertTrue(ex.getMessage().contains("Número de telefone inválido"));
    }
}