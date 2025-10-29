package com.pizzaria.backendpizzaria;

import com.pizzaria.backendpizzaria.mock.DadosMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackendPizzariaApplication implements CommandLineRunner {

    @Autowired
    private DadosMock dadosMock;

    public static void main(String[] args) {
        SpringApplication.run(BackendPizzariaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        dadosMock.carregarDados();
    }
}