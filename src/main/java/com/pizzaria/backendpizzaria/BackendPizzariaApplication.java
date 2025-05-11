package com.pizzaria.backendpizzaria;

import com.pizzaria.backendpizzaria.service.DadosMockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendPizzariaApplication implements CommandLineRunner {

    @Autowired
    private DadosMockService dadosMockService;

    public static void main(String[] args) {
        SpringApplication.run(BackendPizzariaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dadosMockService.carregarDados();
    }
}