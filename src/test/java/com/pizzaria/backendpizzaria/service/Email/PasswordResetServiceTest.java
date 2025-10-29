package com.pizzaria.backendpizzaria.service.Email;

import com.pizzaria.backendpizzaria.domain.DTO.Login.Email;
import com.pizzaria.backendpizzaria.domain.PasswordResetToken;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.PasswordResetTokenRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository tokenRepository;
    @Mock
    private UsuarioRepository userRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private Usuario usuario;
    private PasswordResetToken token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("teste@email.com");
        usuario.setNome("João");

        token = new PasswordResetToken();
        token.setToken("token123");
        token.setUsuario(usuario);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
    }

    @Test
    void sendResetLink_sucesso() {
        when(userRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(inv -> inv.getArgument(0));

        passwordResetService.sendResetLink("teste@email.com");

        verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(emailService, times(1)).sendEmail(any(Email.class));
    }

    @Test
    void sendResetLink_usuarioNaoEncontrado() {
        when(userRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> passwordResetService.sendResetLink("naoexiste@email.com"));
        assertTrue(ex.getMessage().contains("Nenhum usuário encontrado"));
    }

    @Test
    void sendResetLink_tokenExpirado() {
        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setToken("tokenExpirado");
        expiredToken.setUsuario(usuario);
        expiredToken.setExpiryDate(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(inv -> inv.getArgument(0));

        // Simula que o token gerado está expirado
        try (MockedConstruction<PasswordResetToken> mock = mockConstruction(PasswordResetToken.class,
                (mocked, context) -> {
                    when(mocked.isExpired()).thenReturn(true);
                    when(mocked.getUsuario()).thenReturn(usuario);
                    when(mocked.getToken()).thenReturn("tokenExpirado");
                })) {
            passwordResetService.sendResetLink("teste@email.com");
            verify(tokenRepository, times(1)).delete(any(PasswordResetToken.class));
            verify(tokenRepository, times(1)).save(any(PasswordResetToken.class));
            verify(emailService, times(1)).sendEmail(any(Email.class));
        }
    }

    @Test
    void resetPassword_sucesso() {
        when(tokenRepository.findByToken("token123")).thenReturn(Optional.of(token));
        when(userRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        passwordResetService.resetPassword("token123", "novaSenha");

        verify(userRepository, times(1)).save(any(Usuario.class));
        verify(tokenRepository, times(1)).delete(token);
    }

    @Test
    void resetPassword_tokenNaoEncontrado() {
        when(tokenRepository.findByToken("tokenInvalido")).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class, () -> passwordResetService.resetPassword("tokenInvalido", "senha"));
        assertTrue(ex.getMessage().contains("Token inválido"));
    }

    @Test
    void resetPassword_tokenExpirado() {
        token.setExpiryDate(LocalDateTime.now().minusMinutes(1));
        when(tokenRepository.findByToken("token123")).thenReturn(Optional.of(token));

        PasswordResetToken spyToken = spy(token);
        doReturn(true).when(spyToken).isExpired();

        when(tokenRepository.findByToken("token123")).thenReturn(Optional.of(spyToken));

        ValidationException ex = assertThrows(ValidationException.class, () -> passwordResetService.resetPassword("token123", "senha"));
        assertTrue(ex.getMessage().contains("Token expirado"));
    }
}