package com.pizzaria.backendpizzaria.controller;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ProdutoDTO;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> cadastrarProduto(@RequestBody ProdutoDTO produtoDTO) {
        try {
            Produto produtoCriado = produtoService.cadastrarProduto(produtoDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("produtoId", produtoCriado.getId());
            response.put("nome", produtoCriado.getNome());
            response.put("preco", produtoCriado.getPreco());
            response.put("quantidade", produtoCriado.getQuantidadeEstoque());
            response.put("mensagem", "Produto cadastrado com sucesso!");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Erro ao cadastrar produto: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }
}
