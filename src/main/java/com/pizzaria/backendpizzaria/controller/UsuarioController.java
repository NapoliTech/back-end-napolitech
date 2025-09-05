package com.pizzaria.backendpizzaria.controller;

import com.pizzaria.backendpizzaria.domain.DTO.Login.LoginDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioAtualizacaoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioCreatedDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.domain.Pedido;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.repository.PedidoRepository;
import com.pizzaria.backendpizzaria.service.Usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Operation(summary = "Registrar um novo usuário", description = "Registra um novo usuário no sistema.")
    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, Object>> registrarUsuario(
            @Validated @RequestBody UsuarioRegistroDTO usuarioDTO) {
        Usuario usuario = usuarioService.registro(usuarioDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", new UsuarioCreatedDTO(usuario));
        response.put("mensagem", "Usuário cadastrado com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Registrar um novo atendente", description = "Registra um novo atendente no sistema.")
    @PostMapping("/cadastro/atendente")
    public ResponseEntity<Map<String, Object>> registrarAtendente(
            @Validated @RequestBody UsuarioRegistroDTO usuarioDTO) {
        Usuario usuario = usuarioService.registroAtendente(usuarioDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", new UsuarioCreatedDTO(usuario));
        response.put("mensagem", "Atendente cadastrado com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Registrar um novo administrador", description = "Registra um novo administrador no sistema.")
    @PostMapping("/cadastro/admin")
    public ResponseEntity<Map<String, Object>> registrarAdmin(
            @Validated @RequestBody UsuarioRegistroDTO usuarioDTO) {
        Usuario usuario = usuarioService.registroAdmin(usuarioDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", new UsuarioCreatedDTO(usuario));
        response.put("mensagem", "Admin cadastrado com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Realizar login", description = "Autentica um usuário e retorna um token.")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            String token = usuarioService.loginUsuario(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Listar usuários", description = "Retorna uma lista paginada de usuários.")
    @GetMapping
    public ResponseEntity<Page<Usuario>> listarUsuarios(
            @Parameter(description = "Configuração de paginação e ordenação.")
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listarUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarUsuarioPorId(
            @Parameter(description = "ID do usuário a ser buscado.", example = "1")
            @PathVariable("id") Long id) {
        Optional<Usuario> usuario = usuarioService.listarUsuariosPorId(id);

        if (usuario.isPresent()) {
            List<Pedido> pedidosDoUsuario = pedidoRepository.findByClienteIdUsuario(id);


            List<Map<String, Object>> pedidosResumidos = pedidosDoUsuario.stream()
                    .map(pedido -> {
                        Map<String, Object> pedidoMap = new HashMap<>();
                        pedidoMap.put("id", pedido.getId());
                        pedidoMap.put("itens", pedido.getItens());
                        pedidoMap.put("dataPedido", pedido.getDataPedido());
                        pedidoMap.put("statusPedido", pedido.getStatusPedido());
                        pedidoMap.put("precoTotal", pedido.getPrecoTotal());
                        return pedidoMap;
                    })
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("usuario", usuario.get());
            response.put("pedidosQuantidade", pedidosDoUsuario.size());
            response.put("pedidosRealizados", pedidosResumidos);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar usuário por email", description = "Retorna os detalhes de um usuário pelo email.")
    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Object>> listarUsuarioPorEmail(
            @Parameter(description = "ID do usuário a ser buscado.", example = "napolitech@email.com")
            @PathVariable("email") String email) {
        Optional<Usuario> usuario = usuarioService.listarUsuariosPorEmail(email);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(
            @Parameter(description = "ID do usuário a ser atualizado.", example = "1")
            @Validated @PathVariable("id") Long id,
            @RequestBody UsuarioAtualizacaoDTO usuarioDTO) {
        Usuario usuarioParaAtuallizar = usuarioService.atualizarUsuario(id, usuarioDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", new UsuarioCreatedDTO(usuarioParaAtuallizar));
        response.put("mensagem", "Usuário Atualizado com sucesso");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Deletar usuário", description = "Remove um usuário pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarUsuario(
            @Parameter(description = "ID do usuário a ser deletado.", example = "1")
            @PathVariable Long id) {
        usuarioService.deletarUsuario(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Usuário deletado com sucesso");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}