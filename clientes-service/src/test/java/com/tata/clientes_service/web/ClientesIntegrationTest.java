package com.tata.clientes_service.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.utility.TestcontainersConfiguration;

import com.tata.clientes_service.web.dto.ClienteDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class ClientesIntegrationTest {

    @Autowired
    TestRestTemplate rest;

    @Test
    void crear_y_obtener_cliente() {
        var req = new ClienteDto(
                null, "CLI-100", "Ana", "F", 30, "0102030405",
                "Av. Siempre Viva", "0999999999", true, "Secreta123");

        var create = rest.postForEntity("/api/clientes", req, ClienteDto.class);
        assertThat(create.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        var created = create.getBody();
        assertThat(created).isNotNull();
        assertThat(created.clienteId()).isEqualTo("CLI-100");
        assertThat(created.contrasenia()).isNull(); 

        var get = rest.getForEntity("/api/clientes/" + created.id(), ClienteDto.class);
        assertThat(get.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(get.getBody().clienteId()).isEqualTo("CLI-100");
    }
}
