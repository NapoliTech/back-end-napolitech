package com.pizzaria.backendpizzaria.service.Email;

import com.pizzaria.backendpizzaria.domain.DTO.Login.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private Email email;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        email = new Email("destino@email.com", "Assunto", "Corpo do email","Conteudo do email de troca de senha");
    }

    @Test
    void sendEmail_sucesso() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendEmail(email));

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());
        SimpleMailMessage msg = captor.getValue();
        assertEquals("noreply@email.com", msg.getFrom());
        assertArrayEquals(new String[]{"destino@email.com"}, msg.getTo());
        assertEquals("Assunto", msg.getSubject());
        assertEquals("Corpo do email", msg.getText());
        assertEquals("Conteudo do email de troca de senha", email.contentType());
    }

    @Test
    void sendEmail_falhaEnvio() {
        doThrow(new RuntimeException("Erro ao enviar")).when(mailSender).send(any(SimpleMailMessage.class));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> emailService.sendEmail(email));
        assertEquals("Erro ao enviar", ex.getMessage());
    }
}