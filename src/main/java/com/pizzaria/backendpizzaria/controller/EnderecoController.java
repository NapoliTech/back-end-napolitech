package com.pizzaria.backendpizzaria.controller;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.EnderecoDTO;
import com.pizzaria.backendpizzaria.domain.Endereco;
import com.pizzaria.backendpizzaria.service.EnderecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enderecos")
@Tag(name = "Endereços", description = "Endpoints para gerenciamento de endereços.")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @Operation(summary = "Cadastrar um novo endereço", description = "Registra um novo endereço no sistema.")
    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrarEndereco(
            @Parameter(description = "Dados do endereço a ser cadastrado.") @RequestBody EnderecoDTO enderecoDTO) {
        try {
            Endereco enderecoCriado = enderecoService.cadastrarEndereco(enderecoDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("enderecoId", enderecoCriado.getId());
            response.put("rua", enderecoCriado.getRua());
            response.put("numero", enderecoCriado.getNumero());
            response.put("bairro", enderecoCriado.getBairro());
            response.put("complemento", enderecoCriado.getComplemento());
            response.put("cidade", enderecoCriado.getCidade());
            response.put("estado", enderecoCriado.getEstado());
            response.put("cep", enderecoCriado.getCep());
            response.put("mensagem", "Endereço cadastrado com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Erro ao cadastrar endereço: " + e.getMessage()));
        }
    }

    @Operation(summary = "Buscar endereço por ID", description = "Retorna os detalhes de um endereço pelo ID.")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarEnderecoPorId(
            @Parameter(description = "ID do endereço a ser buscado.", example = "1") @PathVariable Integer id) {
        Optional<Endereco> endereco = enderecoService.listarEnderecoPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("endereco", endereco);
        response.put("usuario", endereco.map(Endereco::getUsuario).orElse(null));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Buscar endereço por email", description = "Retorna os detalhes de um endereço pelo email.")
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> listarEnderecoPorEmail(
            @Parameter(description = "email do endereço a ser buscado.", example = "napolitech@email.com") @PathVariable String email) {
        Optional<Endereco> endereco = enderecoService.listarEnderecoPorEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("endereco", endereco);
        response.put("usuario", endereco.map(Endereco::getUsuario).orElse(null));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Listar todos os endereços", description = "Retorna uma lista de todos os endereços cadastrados.")
    @GetMapping
    public ResponseEntity<List<Endereco>> listarEnderecos() {
        List<Endereco> enderecos = enderecoService.listarEnderecos();
        return ResponseEntity.ok(enderecos);
    }

    @Operation(summary = "Atualizar endereço de um usuário", description = "Atualiza os dados de um endereço associado a um usuário.")
    @PutMapping("/{usuarioId}/{enderecoId}")
    public ResponseEntity<Map<String, Object>> atualizarEnderecoDeUsuario(
            @Parameter(description = "ID do usuário associado ao endereço.", example = "1") @PathVariable Long usuarioId,
            @Parameter(description = "ID do endereço a ser atualizado.", example = "1") @PathVariable Long enderecoId,
            @Parameter(description = "Dados atualizados do endereço.") @RequestBody EnderecoDTO enderecoDTO) {
        try {
            Endereco enderecoAtualizado = enderecoService.atualizarEnderecoDeUsuario(usuarioId, enderecoId, enderecoDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("enderecoId", enderecoAtualizado.getId());
            response.put("rua", enderecoAtualizado.getRua());
            response.put("numero", enderecoAtualizado.getNumero());
            response.put("bairro", enderecoAtualizado.getBairro());
            response.put("complemento", enderecoAtualizado.getComplemento());
            response.put("cidade", enderecoAtualizado.getCidade());
            response.put("estado", enderecoAtualizado.getEstado());
            response.put("cep", enderecoAtualizado.getCep());
            response.put("mensagem", "Endereço atualizado com sucesso!");

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Erro ao atualizar endereço: " + e.getMessage()));
        }
    }

    @Operation(summary = "Deletar endereço", description = "Remove um endereço pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarEndereco(
            @Parameter(description = "ID do endereço a ser deletado.", example = "1") @PathVariable Integer id) {
        boolean deletado = enderecoService.deletarEndereco(id);

        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Endereço não encontrado!"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("mensagem", "Endereço deletado com sucesso!"));
    }
}