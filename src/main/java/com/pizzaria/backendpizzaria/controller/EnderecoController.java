package com.pizzaria.backendpizzaria.controller;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.EnderecoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ProdutoDTO;
import com.pizzaria.backendpizzaria.domain.Endereco;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.service.EnderecoService;
import com.pizzaria.backendpizzaria.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrarEndereco(@RequestBody EnderecoDTO enderecoDTO) {
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


    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarEnderecoPorId(
            @PathVariable Integer id
    ){
        Optional<Endereco> endereco = enderecoService.listarEnderecoPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("endereco", endereco);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> listarEnderecos() {
        List<Endereco> enderecos = enderecoService.listarEnderecos();
        return ResponseEntity.ok(enderecos);
    }

    @PutMapping("/{usuarioId}/{enderecoId}")
    public ResponseEntity<Map<String, Object>> atualizarEnderecoDeUsuario(
            @PathVariable Long usuarioId,
            @PathVariable Long enderecoId,
            @RequestBody EnderecoDTO enderecoDTO) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarEndereco(@PathVariable Integer id) {
        boolean deletado = enderecoService.deletarEndereco(id);

        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Endereço não encontrado!"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("mensagem", "Endereço deletado com sucesso!"));
    }
}
