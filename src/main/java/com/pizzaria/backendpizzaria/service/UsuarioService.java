package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.config.JwtUtil;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioAtualizacaoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.domain.UsuarioDetailsAdapter;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Optional<Usuario> listarUsuariosPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioAtualizacaoDTO usuarioParaAtualizar) {
        Usuario usuarioExistente = usuarioRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Usuário não encontrado com ID: " + id)
        );

        usuarioExistente.setSenha(passwordEncoder.encode(usuarioParaAtualizar.getSenha()));
        usuarioExistente.setNome(usuarioParaAtualizar.getNome());
        usuarioExistente.setEmail(usuarioParaAtualizar.getEmail());
        usuarioExistente.setTelefone(usuarioParaAtualizar.getTelefone());

        return usuarioRepository.save(usuarioExistente);
    }

    @Transactional
    public Usuario registro(UsuarioRegistroDTO usuarioDTO) {
        validarCampos(usuarioDTO);
        validarCPF(usuarioDTO.getCpf());
        validarTelefone(usuarioDTO.getTelefone());
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setDataNasc(usuarioDTO.getDataNasc());
        usuario.setTipoUsuario("USUARIO_COMUM");
        usuario.setTelefone(usuarioDTO.getTelefone());
        usuario.setPedidos(0L);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario registroAtendente(UsuarioRegistroDTO usuarioDTO) {
        validarCampos(usuarioDTO);
        validarCPF(usuarioDTO.getCpf());
        validarTelefone(usuarioDTO.getTelefone());
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setDataNasc(usuarioDTO.getDataNasc());
        usuario.setTipoUsuario("ATENDENTE");
        usuario.setTelefone(usuarioDTO.getTelefone());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario registroAdmin(UsuarioRegistroDTO usuarioDTO) {
        validarCampos(usuarioDTO);
        validarCPF(usuarioDTO.getCpf());
        validarTelefone(usuarioDTO.getTelefone());
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setDataNasc(usuarioDTO.getDataNasc());
        usuario.setTipoUsuario("ADMINISTRADOR");
        usuario.setTelefone(usuarioDTO.getTelefone());

        return usuarioRepository.save(usuario);
    }

    public String loginUsuario(String email, String senha) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(email);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            if (passwordEncoder.matches(senha, usuario.getSenha())){
                return jwtUtil.generateToken(usuario.getEmail());
            }
        }
        throw new ValidationException("Credenciais inválidas");
    }

    private void validarCampos(UsuarioRegistroDTO usuarioDTO) {
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

    private void validarCPF(String cpf) {
        String regex = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}";
        if (!Pattern.matches(regex, cpf)) {
            throw new ValidationException("CPF inválido. O formato correto é XXX.XXX.XXX-XX");
        }
    }

    private void validarTelefone(String telefone) {
        String telefoneNumerico = telefone.replaceAll("[^0-9]", "");

        if (telefoneNumerico.length() < 10 || telefoneNumerico.length() > 11) {
            throw new ValidationException("Número de telefone inválido. Formatos aceitos: (11) 98765-4321 ou 11987654321.");
        }
    }

    @Transactional
    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
        return new UsuarioDetailsAdapter(usuario);
    }
}
