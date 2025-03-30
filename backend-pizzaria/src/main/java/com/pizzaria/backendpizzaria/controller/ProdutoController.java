package com.pizzaria.backendpizzaria.controller;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.ProdutoDTO;
import com.pizzaria.backendpizzaria.domain.Produto;
import com.pizzaria.backendpizzaria.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarProdutosPorId(
            @PathVariable Integer id
    ){
        Optional<Produto> produto = produtoService.listarProdutoPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("produto", produto);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        List<Produto> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarProduto(@PathVariable Integer id) {
        boolean deletado = produtoService.deletarProduto(id);

        if (!deletado) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Produto não encontrado!"));
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("mensagem", "Produto deletado com sucesso!"));
    }
}
