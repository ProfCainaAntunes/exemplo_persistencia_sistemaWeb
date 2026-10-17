package br.gov.sp.fatec.pos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da Documentação Interativa da API (OpenAPI 3 / Swagger UI).
 * <p>
 * <b>Conceito Pedagógico (Contrato de Interface e Auto-documentação):</b>
 * Permite a geração dinâmica da documentação formal da API REST, facilitando testes,
 * integração com clientes HTTP (front-end Angular, Postman) e padronização corporativa.
 *
 * @author Cainã Antunes Silva
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PDV Core - API RESTful de Gestão de Produtos")
                        .version("1.0.0")
                        .description("API RESTful desenvolvida com Spring Boot 3 para persistência relacional transacional de produtos com chamadas assíncronas.")
                        .contact(new Contact()
                                .name("Prof. Cainã Antunes Silva")
                                .url("https://github.com/ProfCainaAntunes"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
