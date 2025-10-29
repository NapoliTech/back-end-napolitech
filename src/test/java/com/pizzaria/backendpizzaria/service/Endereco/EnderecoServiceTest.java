package com.pizzaria.backendpizzaria.service.Endereco;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.EnderecoDTO;
import com.pizzaria.backendpizzaria.domain.Endereco;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.repository.EnderecoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    private EnderecoDTO enderecoDTO;
    private Endereco endereco;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = new Usuario();
        usuario.setIdUsuario(1L);

        enderecoDTO = new EnderecoDTO();
        enderecoDTO.setRua("Rua A");
        enderecoDTO.setNumero(123);
        enderecoDTO.setBairro("Centro");
        enderecoDTO.setComplemento("Apto 1");
        enderecoDTO.setCidade("Cidade");
        enderecoDTO.setEstado("SP");
        enderecoDTO.setCep("12345-678");
        enderecoDTO.setUsuarioId(1L);
        endereco = new Endereco();
        endereco.setId(1L);
        endereco.setRua("Rua A");
        endereco.setNumero(123);
        endereco.setBairro("Centro");
        endereco.setComplemento("Apto 1");
        endereco.setCidade("Cidade");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");
        endereco.setUsuarioId(1L);
    }

    @Test
    void cadastrarEndereco_sucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(inv -> inv.getArgument(0));

        Endereco salvo = enderecoService.cadastrarEndereco(enderecoDTO);

        assertNotNull(salvo);
        assertEquals(enderecoDTO.getRua(), salvo.getRua());
        assertEquals(enderecoDTO.getUsuarioId(), salvo.getUsuarioId());
    }

    @Test
    void cadastrarEndereco_usuarioNaoInformado() {
        enderecoDTO.setUsuarioId(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> enderecoService.cadastrarEndereco(enderecoDTO));
        assertTrue(ex.getMessage().contains("Usuário não informado"));
    }

    @Test
    void cadastrarEndereco_usuarioNaoEncontrado() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> enderecoService.cadastrarEndereco(enderecoDTO));
        assertTrue(ex.getMessage().contains("Usuário não encontrado"));
    }

    @Test
    void listarEnderecoPorId_sucesso() {
        when(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco));

        Optional<Endereco> result = enderecoService.listarEnderecoPorId(1);

        assertTrue(result.isPresent());
        assertEquals(endereco, result.get());
    }

    @Test
    void listarEnderecoPorId_naoEncontrado() {
        when(enderecoRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Endereco> result = enderecoService.listarEnderecoPorId(2);

        assertFalse(result.isPresent());
    }

    @Test
    void listarEnderecoPorEmail_sucesso() {
        when(enderecoRepository.findByEmail("email@teste.com")).thenReturn(Optional.of(endereco));

        Optional<Endereco> result = enderecoService.listarEnderecoPorEmail("email@teste.com");

        assertTrue(result.isPresent());
        assertEquals(endereco, result.get());
    }

    @Test
    void listarEnderecoPorEmail_naoEncontrado() {
        when(enderecoRepository.findByEmail("email@teste.com")).thenReturn(Optional.empty());

        Optional<Endereco> result = enderecoService.listarEnderecoPorEmail("email@teste.com");

        assertFalse(result.isPresent());
    }

    @Test
    void listarEnderecos_sucesso() {
        List<Endereco> lista = Arrays.asList(endereco);
        when(enderecoRepository.findAll()).thenReturn(lista);

        List<Endereco> result = enderecoService.listarEnderecos();

        assertEquals(1, result.size());
        assertTrue(result.contains(endereco));
    }

    @Test
    void atualizarEnderecoDeUsuario_sucesso() {
        when(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(any(Endereco.class))).thenAnswer(inv -> inv.getArgument(0));

        EnderecoDTO novoDTO = new EnderecoDTO();
        novoDTO.setRua("Nova Rua");
        novoDTO.setNumero(456);
        novoDTO.setBairro("Novo Bairro");
        novoDTO.setComplemento("Casa");
        novoDTO.setCidade("Nova Cidade");
        novoDTO.setEstado("RJ");
        novoDTO.setCep("98765-432");
        novoDTO.setUsuarioId(1L);

        Endereco atualizado = enderecoService.atualizarEnderecoDeUsuario(1L, 1L, novoDTO);

        assertEquals("Nova Rua", atualizado.getRua());
        assertEquals("RJ", atualizado.getEstado());
    }

    @Test
    void atualizarEnderecoDeUsuario_enderecoNaoEncontrado() {
        when(enderecoRepository.findById(1)).thenReturn(Optional.empty());

        EnderecoDTO novoDTO = new EnderecoDTO();
        novoDTO.setUsuarioId(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> enderecoService.atualizarEnderecoDeUsuario(1L, 1L, novoDTO));
        assertTrue(ex.getMessage().contains("Endereço com ID 1 não encontrado"));
    }

    @Test
    void atualizarEnderecoDeUsuario_enderecoNaoPertenceAoUsuario() {
        endereco.setUsuarioId(2L);
        when(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco));

        EnderecoDTO novoDTO = new EnderecoDTO();
        novoDTO.setUsuarioId(1L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> enderecoService.atualizarEnderecoDeUsuario(1L, 1L, novoDTO));
        assertTrue(ex.getMessage().contains("O endereço não pertence ao usuário"));
    }

    @Test
    void deletarEndereco_sucesso() {
        when(enderecoRepository.findById(1)).thenReturn(Optional.of(endereco));
        doNothing().when(enderecoRepository).delete(endereco);

        boolean result = enderecoService.deletarEndereco(1);

        assertTrue(result);
        verify(enderecoRepository, times(1)).delete(endereco);
    }

    @Test
    void deletarEndereco_naoEncontrado() {
        when(enderecoRepository.findById(2)).thenReturn(Optional.empty());

        boolean result = enderecoService.deletarEndereco(2);

        assertFalse(result);
        verify(enderecoRepository, never()).delete(any());
    }
}