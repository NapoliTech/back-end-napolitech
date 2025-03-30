package com.pizzaria.backendpizzaria.controller;

import com.pizzaria.backendpizzaria.domain.DTO.Login.LoginDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioAtualizacaoDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioCreatedDTO;
import com.pizzaria.backendpizzaria.domain.DTO.Login.UsuarioRegistroDTO;
import com.pizzaria.backendpizzaria.domain.Usuario;
import com.pizzaria.backendpizzaria.service.UsuarioService;
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
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<Map<String, Object>> registrarUsuario(@Validated @RequestBody UsuarioRegistroDTO usuarioDTO){
        Usuario usuario = usuarioService.registro(usuarioDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", new UsuarioCreatedDTO(usuario));
        response.put("mensagem", "Usuário cadastrado com sucesso");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            String token = usuarioService.loginUsuario(loginDTO.getEmail(), loginDTO.getSenha());
            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<Page<Usuario>> listarUsuarios(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.listarUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listarUsuarioPorId(
            @PathVariable Long id
    ){
        Optional<Usuario> usuario = usuarioService.listarUsuariosPorId(id);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", usuario);
        return  ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizarUsuario(@Validated @PathVariable Long id, @RequestBody UsuarioAtualizacaoDTO usuarioDTO) {
        Usuario usuarioParaAtuallizar = usuarioService.atualizarUsuario(id, usuarioDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("usuario", new UsuarioCreatedDTO(usuarioParaAtuallizar));
        response.put("mensagem", "Usuário Atualizado com sucesso");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Usuário deletado com sucesso");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
