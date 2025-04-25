package com.pizzaria.backendpizzaria.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/logged")
    public String hello() {
        return "Hello World";
    }
}