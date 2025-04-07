package com.pizzaria.backendpizzaria.service;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.EnderecoDTO;
import com.pizzaria.backendpizzaria.domain.Endereco;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.repository.EnderecoRepository;
import com.pizzaria.backendpizzaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; // Adicionado para buscar usuário

    @Transactional
    public Endereco cadastrarEndereco(EnderecoDTO enderecoDTO) {
        Endereco endereco = new Endereco();
        endereco.setRua(enderecoDTO.getRua());
        endereco.setNumero(enderecoDTO.getNumero());
        endereco.setBairro(enderecoDTO.getBairro());
        endereco.setComplemento(enderecoDTO.getComplemento());
        endereco.setCidade(enderecoDTO.getCidade());
        endereco.setEstado(enderecoDTO.getEstado());
        endereco.setCep(enderecoDTO.getCep());

        if (enderecoDTO.getUsuarioId() != null) {
            Optional<Usuario> usuarioOptional = usuarioRepository.findById(enderecoDTO.getUsuarioId());
            if (usuarioOptional.isPresent()) {
                endereco.setUsuarioId(enderecoDTO.getUsuarioId());
            } else {
                throw new RuntimeException("Usuário não encontrado com ID: " + enderecoDTO.getUsuarioId());
            }
        } else {
            throw new RuntimeException("Usuário não informado no cadastro do endereço!");
        }

        return enderecoRepository.save(endereco);
    }

    public Optional<Endereco> listarEnderecoPorId(Integer id) {
        return enderecoRepository.findById(id);
    }

    public List<Endereco> listarEnderecos() {
        return enderecoRepository.findAll();
    }

    @Transactional
    public boolean deletarEndereco(Integer id) {
        Optional<Endereco> enderecoOptional = enderecoRepository.findById(id);

        if (enderecoOptional.isPresent()) {
            enderecoRepository.delete(enderecoOptional.get());
            return true;
        }

        return false;
    }
}
