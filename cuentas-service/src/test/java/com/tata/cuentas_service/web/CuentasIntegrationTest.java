package com.tata.cuentas_service.web;

import com.tata.cuentas_service.web.dto.CuentaDto;
import com.tata.cuentas_service.web.dto.MovimientoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CuentasIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", () -> postgres.getJdbcUrl() + "&currentSchema=cuentas");
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.flyway.enabled", () -> "true");
        r.add("spring.flyway.schemas", () -> "cuentas");
        r.add("spring.flyway.default-schema", () -> "cuentas");
    }

    @Autowired
    TestRestTemplate rest;

    @Test
    void crear_cuenta_y_realizar_abono_y_debito() {

        var nueva = new CuentaDto(null, "CTA-IT-001", "Ahorro", "CLI-IT-100", true, BigDecimal.ZERO);
        var r1 = rest.postForEntity("/api/cuentas", nueva, CuentaDto.class);
        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID cuentaId = r1.getBody().id();

        var abono = new MovimientoDto(null, cuentaId, null, new BigDecimal("200.00"), null, null);
        var r2 = rest.postForEntity("/api/movimientos", abono, MovimientoDto.class);
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r2.getBody().saldoResultante()).isEqualByComparingTo("200.00");

        var retiro = new MovimientoDto(null, cuentaId, null, new BigDecimal("-50.00"), null, null);
        var r3 = rest.postForEntity("/api/movimientos", retiro, MovimientoDto.class);
        assertThat(r3.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r3.getBody().saldoResultante()).isEqualByComparingTo("150.00");
    }
}
