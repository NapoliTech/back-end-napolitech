package com.pizzaria.backendpizzaria.infra;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentação da API da NapoliTech")
                        .description("Esta é a documentação detalhada da API para o backend do projeto de extensão da faculdade. Inclui informações dos endpoints e exemplos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("pedrohpiress")
                                .url("https://www.pizzariabonari.com")
                                .email("pedro.souza@v8.tech"))
                        .license(new License()
                                .name("Licença MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}