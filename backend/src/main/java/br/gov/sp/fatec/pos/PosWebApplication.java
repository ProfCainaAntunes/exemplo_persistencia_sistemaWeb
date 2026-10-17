package br.gov.sp.fatec.pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada principal da aplicação Spring Boot (PDV Core).
 * <p>
 * <b>Conceito Pedagógico (Inversão de Controle e Autoconfiguração):</b>
 * A anotação {@link SpringBootApplication} engloba:
 * <ul>
 *   <li>{@code @Configuration}: Define a classe como fonte de definições de beans no contexto do Spring.</li>
 *   <li>{@code @EnableAutoConfiguration}: Habilita o mecanismo inteligente do Spring Boot para configurar automaticamente
 *       a infraestrutura com base nas dependências do classpath (JPA, DataSource H2, Tomcat, Jackson).</li>
 *   <li>{@code @ComponentScan}: Realiza a varredura automática de componentes no pacote raiz e subpacotes.</li>
 * </ul>
 *
 * @author Cainã Antunes Silva
 */
@SpringBootApplication
public class PosWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(PosWebApplication.class, args);
    }
}
