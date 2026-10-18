package br.gov.sp.fatec.pos.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuração de DataSource para ambiente de Produção (PostgreSQL).
 * <p>
 * <b>Compatibilidade com Provedores Cloud (Render, Neon, Supabase):</b>
 * Suporta tanto o formato padrão JDBC ({@code jdbc:postgresql://host:port/database})
 * quanto o formato de URL de conexão direta fornecido nativamente pelo Render
 * ({@code postgres://user:password@host:port/database}).
 *
 * @author Cainã Antunes Silva
 */
@Configuration
@Profile("prod")
public class ProductionDatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductionDatabaseConfig.class);

    @Value("${spring.datasource.url:${DATABASE_URL:}}")
    private String rawDatasourceUrl;

    @Value("${spring.datasource.username:}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:}")
    private String datasourcePassword;

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");

        if (rawDatasourceUrl != null && (rawDatasourceUrl.startsWith("postgres://") || rawDatasourceUrl.startsWith("postgresql://"))) {
            log.info("Detectada URL de conexão URI do Render (postgres://). Normalizando para formato JDBC...");
            URI uri = new URI(rawDatasourceUrl);
            String host = uri.getHost();
            int port = (uri.getPort() > 0) ? uri.getPort() : 5432;
            String path = uri.getPath();

            String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
            config.setJdbcUrl(jdbcUrl);

            if (uri.getUserInfo() != null) {
                String[] credentials = uri.getUserInfo().split(":", 2);
                config.setUsername(credentials[0]);
                if (credentials.length > 1) {
                    config.setPassword(credentials[1]);
                }
            }
        } else {
            config.setJdbcUrl(rawDatasourceUrl);
            if (datasourceUsername != null && !datasourceUsername.isBlank()) {
                config.setUsername(datasourceUsername);
            }
            if (datasourcePassword != null && !datasourcePassword.isBlank()) {
                config.setPassword(datasourcePassword);
            }
        }

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(20000);

        return new HikariDataSource(config);
    }
}
