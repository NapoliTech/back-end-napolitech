package com.pizzaria.backendpizzaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BackendPizzariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendPizzariaApplication.class, args);
    }

}
