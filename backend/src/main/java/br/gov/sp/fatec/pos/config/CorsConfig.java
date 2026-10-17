package br.gov.sp.fatec.pos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração de CORS (Cross-Origin Resource Sharing).
 * <p>
 * <b>Conceito Pedagógico (Segurança em Navegadores e Desacoplamento Front/Back):</b>
 * Aplicações SPA (como o Angular) rodam em domínios/portas distintos do back-end em desenvolvimento
 * (ex.: porta 4200 vs 8080) e produção. O CORS instrui os navegadores a permitirem requisições
 * assíncronas HTTP (XMLHttpRequest/Fetch) com os métodos RESTful especificados.
 *
 * @author Cainã Antunes Silva
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
