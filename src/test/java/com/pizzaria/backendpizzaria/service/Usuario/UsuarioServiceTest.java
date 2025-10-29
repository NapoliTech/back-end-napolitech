package com.pizzaria.backendpizzaria.service.Usuario;

import com.pizzaria.backendpizzaria.config.JwtUtil;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioAtualizacaoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UsuarioValidacao usuarioValidacao;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioRegistroDTO registroDTO;
    private UsuarioAtualizacaoDTO atualizacaoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senhaCriptografada");
        usuario.setNome("João Silva");
        usuario.setCpf("123.456.789-00");
        usuario.setDataNasc("01/01/2000");
        usuario.setTipoUsuario("USUARIO_COMUM");
        usuario.setTelefone("11987654321");
        usuario.setPedidos(0L);

        registroDTO = new UsuarioRegistroDTO();
        registroDTO.setEmail("novo@email.com");
        registroDTO.setSenha("123456");
        registroDTO.setConfirmarSenha("123456");
        registroDTO.setNome("Novo Usuário");
        registroDTO.setCpf("123.456.789-00");
        registroDTO.setDataNasc("01/01/2000");
        registroDTO.setTelefone("11987654321");

        atualizacaoDTO = new UsuarioAtualizacaoDTO();
        atualizacaoDTO.setSenha("novaSenha");
        atualizacaoDTO.setNome("Nome Atualizado");
        atualizacaoDTO.setEmail("atualizado@email.com");
        atualizacaoDTO.setTelefone("11999999999");
    }

    @Test
    void listarUsuarios_sucesso() {
        Page<Usuario> page = new PageImpl<>(List.of(usuario));
        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Usuario> result = usuarioService.listarUsuarios(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void listarUsuariosPorEmail_sucesso() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioService.listarUsuariosPorEmail("teste@email.com");
        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
    }

    @Test
    void listarUsuariosPorId_sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioService.listarUsuariosPorId(1L);
        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
    }

    @Test
    void atualizarUsuario_sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(atualizacaoDTO.getSenha())).thenReturn("senhaCriptografadaNova");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario atualizado = usuarioService.atualizarUsuario(1L, atualizacaoDTO);

        assertEquals("Nome Atualizado", atualizado.getNome());
        assertEquals("atualizado@email.com", atualizado.getEmail());
        assertEquals("senhaCriptografadaNova", atualizado.getSenha());
        assertEquals("11999999999", atualizado.getTelefone());
    }

    @Test
    void atualizarUsuario_usuarioNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> usuarioService.atualizarUsuario(1L, atualizacaoDTO));
        assertTrue(ex.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void registro_sucesso() {
        doNothing().when(usuarioValidacao).validarCampos(registroDTO);
        doNothing().when(usuarioValidacao).validarCPF(registroDTO.getCpf());
        doNothing().when(usuarioValidacao).validarTelefone(registroDTO.getTelefone());
        when(passwordEncoder.encode(registroDTO.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario novo = usuarioService.registro(registroDTO);

        assertEquals("novo@email.com", novo.getEmail());
        assertEquals("USUARIO_COMUM", novo.getTipoUsuario());
    }

    @Test
    void registroAtendente_sucesso() {
        doNothing().when(usuarioValidacao).validarCampos(registroDTO);
        doNothing().when(usuarioValidacao).validarCPF(registroDTO.getCpf());
        doNothing().when(usuarioValidacao).validarTelefone(registroDTO.getTelefone());
        when(passwordEncoder.encode(registroDTO.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario novo = usuarioService.registroAtendente(registroDTO);

        assertEquals("ATENDENTE", novo.getTipoUsuario());
    }

    @Test
    void registroAdmin_sucesso() {
        doNothing().when(usuarioValidacao).validarCampos(registroDTO);
        doNothing().when(usuarioValidacao).validarCPF(registroDTO.getCpf());
        doNothing().when(usuarioValidacao).validarTelefone(registroDTO.getTelefone());
        when(passwordEncoder.encode(registroDTO.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario novo = usuarioService.registroAdmin(registroDTO);

        assertEquals("ADMINISTRADOR", novo.getTipoUsuario());
    }

    @Test
    void loginUsuario_sucesso() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", usuario.getSenha())).thenReturn(true);
        when(jwtUtil.generateToken(usuario.getEmail())).thenReturn("token123");

        String token = usuarioService.loginUsuario("teste@email.com", "123456");
        assertEquals("token123", token);
    }

    @Test
    void loginUsuario_credenciaisInvalidas() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", usuario.getSenha())).thenReturn(false);

        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioService.loginUsuario("teste@email.com", "senhaErrada"));
        assertTrue(ex.getMessage().contains("Credenciais inválidas"));
    }

    @Test
    void loginUsuario_usuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioService.loginUsuario("inexistente@email.com", "123456"));
        assertTrue(ex.getMessage().contains("Credenciais inválidas"));
    }

    @Test
    void deletarUsuario_sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        assertDoesNotThrow(() -> usuarioService.deletarUsuario(1L));
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void deletarUsuario_usuarioNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        ValidationException ex = assertThrows(ValidationException.class, () -> usuarioService.deletarUsuario(1L));
        assertTrue(ex.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void loadUserByUsername_sucesso() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));
        UserDetails details = usuarioService.loadUserByUsername("teste@email.com");
        assertEquals(usuario.getEmail(), details.getUsername());
        assertEquals(usuario.getSenha(), details.getPassword());
        assertTrue(details.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(usuario.getTipoUsuario())));
    }

    @Test
    void loadUserByUsername_usuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("naoexiste@email.com")).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> usuarioService.loadUserByUsername("naoexiste@email.com"));
        assertTrue(ex.getMessage().contains("Usuário não encontrado"));
    }
}