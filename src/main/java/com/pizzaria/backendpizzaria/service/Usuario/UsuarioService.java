package com.pizzaria.backendpizzaria.service.Usuario;

import com.pizzaria.backendpizzaria.config.JwtUtil;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioAtualizacaoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final UsuarioValidacao usuarioValidacao;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UsuarioValidacao usuarioValidacao) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.usuarioValidacao = usuarioValidacao;
    }

    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    public Optional<Usuario> listarUsuariosPorEmail(String email) { return usuarioRepository.findByEmail(email);}

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
        usuarioValidacao.validarCampos(usuarioDTO);
        usuarioValidacao.validarCPF(usuarioDTO.getCpf());
        usuarioValidacao.validarTelefone(usuarioDTO.getTelefone());
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
        usuarioValidacao.validarCampos(usuarioDTO);
        usuarioValidacao.validarCPF(usuarioDTO.getCpf());
        usuarioValidacao.validarTelefone(usuarioDTO.getTelefone());
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setDataNasc(usuarioDTO.getDataNasc());
        usuario.setTipoUsuario("ATENDENTE");
        usuario.setTelefone(usuarioDTO.getTelefone());
        usuario.setPedidos(0L);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario registroAdmin(UsuarioRegistroDTO usuarioDTO) {
        usuarioValidacao.validarCampos(usuarioDTO);
        usuarioValidacao.validarCPF(usuarioDTO.getCpf());
        usuarioValidacao.validarTelefone(usuarioDTO.getTelefone());
        Usuario usuario = new Usuario();

        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setDataNasc(usuarioDTO.getDataNasc());
        usuario.setTipoUsuario("ADMINISTRADOR");
        usuario.setTelefone(usuarioDTO.getTelefone());
        usuario.setPedidos(0L);

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



    @Transactional
    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

    public UserDetails loadUserByUsername(String username) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(username);
        if (usuarioOptional.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado com o email: " + username);
        }
        Usuario usuario = usuarioOptional.get();
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(usuario.getTipoUsuario())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
