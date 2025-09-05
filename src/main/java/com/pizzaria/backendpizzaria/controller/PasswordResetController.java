package com.pizzaria.backendpizzaria.controller;

import com.pizzaria.backendpizzaria.domain.DTO.Login.ResetPasswordRequest;
import com.pizzaria.backendpizzaria.infra.exception.ValidationException;
import com.pizzaria.backendpizzaria.service.Email.PasswordResetService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/request")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("🛠️ E-mail recebido no Controller: " + email);

        if (email == null || email.isEmpty()) {
            throw new ValidationException("E-mail não pode ser vazio!");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "E-mail enviado com sucesso.");
        passwordResetService.sendResetLink(email);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("mensagem", "Senha redefinida com sucesso!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
