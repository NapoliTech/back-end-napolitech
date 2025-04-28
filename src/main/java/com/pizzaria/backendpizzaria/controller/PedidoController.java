package com.pizzaria.backendpizzaria.controller;

import com.pizzaria.backendpizzaria.domain.DTO.Pedido.AtualizarStatusPedidoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Pedido.PedidoDTO;
import com.pizzaria.backendpizzaria.domain.Pedido;
import com.pizzaria.backendpizzaria.service.PedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> criarPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            Pedido pedidoCriado = pedidoService.criarPedido(pedidoDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("pedidoId", pedidoCriado.getId());
            response.put("valorTotal", pedidoCriado.getPrecoTotal());
            response.put("itens", pedidoCriado.getItens());
            response.put("nomeCliente", pedidoCriado.getCliente().getNome());
            response.put("telefone", pedidoCriado.getCliente().getTelefone());
            response.put("tipoEntrega", pedidoCriado.getTipoEntrega());
            response.put("endereco", pedidoCriado.getCliente().getEndereco());
            response.put("observacao", pedidoCriado.getObservacao());
            response.put("status", pedidoCriado.getStatusPedido());
            response.put("endereco", pedidoCriado.getEndereco());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Erro ao criar pedido: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Pedido> atualizarStatusPedido(@PathVariable Long id, @RequestBody AtualizarStatusPedidoDTO statusPedido) {
        Pedido pedidoAtualizado = pedidoService.atualizarStatusPedido(id, statusPedido.getStatus());
        return new ResponseEntity<>(pedidoAtualizado, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarPedidoPorId(
            @PathVariable Long id
    ){
        Optional<Pedido> pedido = pedidoService.listarPedidoPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("pedido", pedido);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }
}
