package com.pizzaria.backendpizzaria.domain.DTO.Login;

public record Email (
        String to,
        String subject,
        String body,
        String contentType){
}
