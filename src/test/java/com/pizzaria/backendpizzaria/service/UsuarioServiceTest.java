package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.config.JwtUtil;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioAtualizacaoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private Usuario usuario;
    private UsuarioRegistroDTO usuarioRegistroDTO;
    private UsuarioAtualizacaoDTO usuarioAtualizacaoDTO;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao.silva@example.com");
        usuario.setSenha("senhaCodificada");
        usuario.setSenha("senhaCodificada");
        usuario.setCpf("123.456.789-00");
        usuario.setDataNasc("01/01/1990");
        usuario.setTelefone("11987654321");
        usuario.setTipoUsuario("USUARIO_COMUM");
        usuario.setPedidos(0L);

        usuarioRegistroDTO = new UsuarioRegistroDTO();
        usuarioRegistroDTO.setNome("Maria Souza");
        usuarioRegistroDTO.setEmail("maria.souza@example.com");
        usuarioRegistroDTO.setSenha("senha123");
        usuarioRegistroDTO.setConfirmarSenha("senha123");
        usuarioRegistroDTO.setCpf("987.654.321-00");
        usuarioRegistroDTO.setDataNasc("10/05/1995");
        usuarioRegistroDTO.setTelefone("(21) 99876-5432");
    }

    @Test
    @DisplayName("Deve retornar uma página de usuários quando existirem usuários")
    void listarUsuarios_comUsuariosExistentes_deveRetornarPagina() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> paginaEsperada = new PageImpl<>(Collections.singletonList(usuario), pageable, 1);
        when(usuarioRepository.findAll(pageable)).thenReturn(paginaEsperada);

        Page<Usuario> resultado = usuarioService.listarUsuarios(pageable);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.getTotalElements());
        assertEquals(usuario.getEmail(), resultado.getContent().get(0).getEmail());
        verify(usuarioRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar uma página vazia quando não existirem usuários")
    void listarUsuarios_semUsuarios_deveRetornarPaginaVazia() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> paginaVazia = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(usuarioRepository.findAll(pageable)).thenReturn(paginaVazia);

        Page<Usuario> resultado = usuarioService.listarUsuarios(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.getTotalElements());
        verify(usuarioRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Deve retornar um Optional contendo o usuário quando o email existir")
    void listarUsuariosPorEmail_comEmailExistente_deveRetornarUsuario() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.listarUsuariosPorEmail(usuario.getEmail());

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getEmail(), resultado.get().getEmail());
        verify(usuarioRepository, times(1)).findByEmail(usuario.getEmail());
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio quando o email não existir")
    void listarUsuariosPorEmail_comEmailNaoExistente_deveRetornarOptionalVazio() {
        String emailNaoExistente = "naoexiste@example.com";
        when(usuarioRepository.findByEmail(emailNaoExistente)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.listarUsuariosPorEmail(emailNaoExistente);

        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findByEmail(emailNaoExistente);
    }

    @Test
    @DisplayName("Deve retornar um Optional contendo o usuário quando o ID existir")
    void listarUsuariosPorId_comIdExistente_deveRetornarUsuario() {


        when(usuarioRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.listarUsuariosPorId(usuario.getIdUsuario());

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getIdUsuario(), resultado.get().getIdUsuario());
        verify(usuarioRepository, times(1)).findById(usuario.getIdUsuario());
    }

    @Test
    @DisplayName("Deve retornar um Optional vazio quando o ID não existir")
    void listarUsuariosPorId_comIdNaoExistente_deveRetornarOptionalVazio() {
        Long idNaoExistente = 99L;
        when(usuarioRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.listarUsuariosPorId(idNaoExistente);

        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findById(idNaoExistente);
    }



    @Test
    @DisplayName("Deve atualizar o usuário com sucesso quando o ID existir")
    void atualizarUsuario_comIdExistente_deveAtualizarUsuario() {
        usuarioAtualizacaoDTO = new UsuarioAtualizacaoDTO();
        usuarioAtualizacaoDTO.setNome("João Atualizado");
        usuarioAtualizacaoDTO.setEmail("joao.atualizado@example.com");
        usuarioAtualizacaoDTO.setSenha("novaSenha");
        usuarioAtualizacaoDTO.setTelefone("11999999999");



        when(usuarioRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(usuarioAtualizacaoDTO.getSenha())).thenReturn("novaSenhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            return usuarioSalvo;
        });

        Usuario resultado = usuarioService.atualizarUsuario(usuario.getIdUsuario(), usuarioAtualizacaoDTO);

        assertNotNull(resultado);
        assertEquals(usuarioAtualizacaoDTO.getNome(), resultado.getNome());
        assertEquals(usuarioAtualizacaoDTO.getEmail(), resultado.getEmail());
        assertEquals(usuarioAtualizacaoDTO.getTelefone(), resultado.getTelefone());
        assertEquals("novaSenhaCodificada", resultado.getSenha());
        verify(usuarioRepository, times(1)).findById(usuario.getIdUsuario());
        verify(passwordEncoder, times(1)).encode(usuarioAtualizacaoDTO.getSenha());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar usuário com ID inexistente")
    void atualizarUsuario_comIdNaoExistente_deveLancarEntityNotFoundException() {
        Long idNaoExistente = 99L;
        when(usuarioRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                usuarioService.atualizarUsuario(idNaoExistente, usuarioAtualizacaoDTO)
        );
        assertEquals("Usuário não encontrado com ID: " + idNaoExistente, thrown.getMessage());
        verify(usuarioRepository, times(1)).findById(idNaoExistente);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }



    @Test
    @DisplayName("Deve registrar um usuário comum com sucesso")
    void registro_usuarioComum_deveRegistrarComSucesso() {
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(usuarioRegistroDTO.getSenha())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            usuarioSalvo.setIdUsuario(1L);
            return usuarioSalvo;
        });

        Usuario resultado = usuarioService.registro(usuarioRegistroDTO);

        assertNotNull(resultado);
        assertEquals(usuarioRegistroDTO.getEmail(), resultado.getEmail());
        assertEquals("senhaCodificada", resultado.getSenha());
        assertEquals(usuarioRegistroDTO.getNome(), resultado.getNome());
        assertEquals(usuarioRegistroDTO.getCpf(), resultado.getCpf());
        assertEquals(usuarioRegistroDTO.getDataNasc(), resultado.getDataNasc());
        assertEquals(usuarioRegistroDTO.getTelefone(), resultado.getTelefone());
        assertEquals("USUARIO_COMUM", resultado.getTipoUsuario());
        assertEquals(0L, resultado.getPedidos());
        assertNotNull(resultado.getIdUsuario());

        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(usuarioRegistroDTO.getSenha());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o email já estiver em uso no registro de usuário comum")
    void registro_usuarioComum_emailEmUso_deveLancarValidationException() {
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(true);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Email já está em uso", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se os campos obrigatórios estiverem em branco ou nulos no registro de usuário comum")
    void registro_usuarioComum_camposEmBrancoOuNulos_deveLancarValidationException() {


        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);



        usuarioRegistroDTO.setEmail("");
        ValidationException thrownEmailVazio = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Todos os campos são obrigatórios", thrownEmailVazio.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(anyString());
        verify(usuarioRepository, times(1)).existsByEmail(anyString());



        setUp();
        setUp();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);



        usuarioRegistroDTO.setSenha(null);
        ValidationException thrownSenhaNula = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Todos os campos são obrigatórios", thrownSenhaNula.getMessage());

        setUp();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        usuarioRegistroDTO.setNome(" ");
        ValidationException thrownNomeEmBranco = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Todos os campos são obrigatórios", thrownNomeEmBranco.getMessage());

        setUp();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        usuarioRegistroDTO.setCpf(null);
        ValidationException thrownCpfNulo = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Todos os campos são obrigatórios", thrownCpfNulo.getMessage());

        setUp();
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        usuarioRegistroDTO.setTelefone("");
        ValidationException thrownTelefoneEmBranco = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Todos os campos são obrigatórios", thrownTelefoneEmBranco.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }


    @Test
    @DisplayName("Deve lançar ValidationException se a data de nascimento estiver em formato inválido no registro de usuário comum")
    void registro_usuarioComum_dataNascimentoInvalida_deveLancarValidationException() {
        usuarioRegistroDTO.setDataNasc("01-01-1990");
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("A data de nascimento deve estar no formato dd/MM/yyyy.", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se a data de nascimento estiver no futuro no registro de usuário comum")
    void registro_usuarioComum_dataNascimentoNoFuturo_deveLancarValidationException() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        usuarioRegistroDTO.setDataNasc(tomorrow.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("A data de nascimento não pode estar no futuro.", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se as senhas não coincidirem no registro de usuário comum")
    void registro_usuarioComum_senhasNaoCoincidem_deveLancarValidationException() {
        usuarioRegistroDTO.setConfirmarSenha("senhaDiferente");
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("As senhas não coincidem", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o nome não contiver sobrenome no registro de usuário comum")
    void registro_usuarioComum_nomeSemSobrenome_deveLancarValidationException() {
        usuarioRegistroDTO.setNome("Maria");
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("O nome deve conter pelo menos um sobrenome", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o CPF for inválido no registro de usuário comum")
    void registro_usuarioComum_cpfInvalido_deveLancarValidationException() {
        usuarioRegistroDTO.setCpf("12345678900");
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("CPF inválido. O formato correto é XXX.XXX.XXX-XX", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o telefone for inválido (muito curto) no registro de usuário comum")
    void registro_usuarioComum_telefoneInvalidoCurto_deveLancarValidationException() {
        usuarioRegistroDTO.setTelefone("123");
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Número de telefone inválido. Formatos aceitos: (11) 98765-4321 ou 11987654321.", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o telefone for inválido (muito longo) no registro de usuário comum")
    void registro_usuarioComum_telefoneInvalidoLongo_deveLancarValidationException() {
        usuarioRegistroDTO.setTelefone("123456789012");
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registro(usuarioRegistroDTO)
        );
        assertEquals("Número de telefone inválido. Formatos aceitos: (11) 98765-4321 ou 11987654321.", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }



    @Test
    @DisplayName("Deve registrar um atendente com sucesso")
    void registroAtendente_deveRegistrarComSucesso() {
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(usuarioRegistroDTO.getSenha())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            usuarioSalvo.setIdUsuario(2L);
            return usuarioSalvo;
        });

        Usuario resultado = usuarioService.registroAtendente(usuarioRegistroDTO);

        assertNotNull(resultado);
        assertEquals(usuarioRegistroDTO.getEmail(), resultado.getEmail());
        assertEquals("senhaCodificada", resultado.getSenha());
        assertEquals("ATENDENTE", resultado.getTipoUsuario());
        assertNotNull(resultado.getIdUsuario());

        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(usuarioRegistroDTO.getSenha());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o email já estiver em uso no registro de atendente")
    void registroAtendente_emailEmUso_deveLancarValidationException() {
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(true);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registroAtendente(usuarioRegistroDTO)
        );
        assertEquals("Email já está em uso", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }



    @Test
    @DisplayName("Deve registrar um administrador com sucesso")
    void registroAdmin_deveRegistrarComSucesso() {
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(usuarioRegistroDTO.getSenha())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            usuarioSalvo.setIdUsuario(3L);
            return usuarioSalvo;
        });

        Usuario resultado = usuarioService.registroAdmin(usuarioRegistroDTO);

        assertNotNull(resultado);
        assertEquals(usuarioRegistroDTO.getEmail(), resultado.getEmail());
        assertEquals("senhaCodificada", resultado.getSenha());
        assertEquals("ADMINISTRADOR", resultado.getTipoUsuario());
        assertNotNull(resultado.getIdUsuario());

        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, times(1)).encode(usuarioRegistroDTO.getSenha());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar ValidationException se o email já estiver em uso no registro de administrador")
    void registroAdmin_emailEmUso_deveLancarValidationException() {
        when(usuarioRepository.existsByEmail(usuarioRegistroDTO.getEmail())).thenReturn(true);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.registroAdmin(usuarioRegistroDTO)
        );
        assertEquals("Email já está em uso", thrown.getMessage());
        verify(usuarioRepository, times(1)).existsByEmail(usuarioRegistroDTO.getEmail());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }



    @Test
    @DisplayName("Deve retornar um token JWT válido para login bem-sucedido")
    void loginUsuario_comCredenciaisValidas_deveRetornarToken() {
        String email = "joao.silva@example.com";
        String senha = "senhaTeste";
        Usuario usuarioParaLogin = new Usuario();
        usuarioParaLogin.setEmail(email);
        usuarioParaLogin.setSenha("senhaCodificada");

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioParaLogin));
        when(passwordEncoder.matches(senha, usuarioParaLogin.getSenha())).thenReturn(true);
        when(jwtUtil.generateToken(email)).thenReturn("fakeTokenJwt");

        String token = usuarioService.loginUsuario(email, senha);

        assertNotNull(token);
        assertEquals("fakeTokenJwt", token);
        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(senha, usuarioParaLogin.getSenha());
        verify(jwtUtil, times(1)).generateToken(email);
    }

    @Test
    @DisplayName("Deve lançar ValidationException para login com email não encontrado")
    void loginUsuario_emailNaoEncontrado_deveLancarValidationException() {
        String email = "naoexiste@example.com";
        String senha = "senhaTeste";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.loginUsuario(email, senha)
        );
        assertEquals("Credenciais inválidas", thrown.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    @DisplayName("Deve lançar ValidationException para login com senha inválida")
    void loginUsuario_senhaInvalida_deveLancarValidationException() {
        String email = "joao.silva@example.com";
        String senha = "senhaInvalida";
        Usuario usuarioParaLogin = new Usuario();
        usuarioParaLogin.setEmail(email);
        usuarioParaLogin.setSenha("senhaCodificada");

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuarioParaLogin));
        when(passwordEncoder.matches(senha, usuarioParaLogin.getSenha())).thenReturn(false);

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.loginUsuario(email, senha)
        );
        assertEquals("Credenciais inválidas", thrown.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(senha, usuarioParaLogin.getSenha());
        verify(jwtUtil, never()).generateToken(anyString());
    }



    @Test
    @DisplayName("Deve deletar um usuário com sucesso quando o ID existir")
    void deletarUsuario_comIdExistente_deveDeletarUsuario() {


        when(usuarioRepository.findById(usuario.getIdUsuario())).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        usuarioService.deletarUsuario(usuario.getIdUsuario());

        verify(usuarioRepository, times(1)).findById(usuario.getIdUsuario());
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    @DisplayName("Deve lançar ValidationException ao tentar deletar usuário com ID inexistente")
    void deletarUsuario_comIdNaoExistente_deveLancarValidationException() {
        Long idNaoExistente = 99L;
        when(usuarioRepository.findById(idNaoExistente)).thenReturn(Optional.empty());

        ValidationException thrown = assertThrows(ValidationException.class, () ->
                usuarioService.deletarUsuario(idNaoExistente)
        );
        assertEquals("Usuário não encontrado", thrown.getMessage());
        verify(usuarioRepository, times(1)).findById(idNaoExistente);
        verify(usuarioRepository, never()).delete(any(Usuario.class));
    }



    @Test
    @DisplayName("Deve retornar UserDetails quando o usuário for encontrado pelo email para autenticação")
    void loadUserByUsername_usuarioEncontrado_deveRetornarUserDetails() {
        String email = "joao.silva@example.com";


        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(usuario.getTipoUsuario())));

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());

        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando o usuário não for encontrado pelo email para autenticação")
    void loadUserByUsername_usuarioNaoEncontrado_deveLancarEntityNotFoundException() {
        String email = "naoexiste@example.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () ->
                usuarioService.loadUserByUsername(email)
        );
        assertEquals("Usuário não encontrado com o email: " + email, thrown.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(email);
    }
}