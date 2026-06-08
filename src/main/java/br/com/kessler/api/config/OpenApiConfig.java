package br.com.kessler.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI kesslerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kessler OS — API")
                        .description("API RESTful para monitoramento, priorização e gerenciamento de detritos orbitais. " +
                                "Parte do ecossistema Kessler OS — FIAP Global Solution 2026.1 (SOA).")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Kessler")
                                .email("kessler@fiap.com.br")))
                .servers(List.of(new Server().url("http://localhost:8080").description("Local")));
    }
}
