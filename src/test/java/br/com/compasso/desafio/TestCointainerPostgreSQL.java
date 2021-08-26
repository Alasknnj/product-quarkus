package br.com.compasso.desafio;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Collections;
import java.util.Map;

public class TestCointainerPostgreSQL implements QuarkusTestResourceLifecycleManager {

    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:13")
        .withDatabaseName("product-quarkus")
        .withUsername("postgres")
        .withPassword("postgres");

    @Override
    public Map<String, String> start() {
        db.start();
        return Collections.singletonMap(
            "quarkus.datasource.url", db.getJdbcUrl()
        );
    }

    @Override
    public void stop() {
        db.stop();
    }
}
