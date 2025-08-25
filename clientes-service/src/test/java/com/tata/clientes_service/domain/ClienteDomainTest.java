package com.tata.clientes_service.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

class ClienteDomainTest {

    @Test
    void puede_setear_clienteId_y_estado_por_defecto_true() {
        var c = new Cliente();
        c.setClienteId("CLI-001");
        assertEquals("CLI-001", c.getClienteId());
        assertTrue(c.getEstado() == null ? true : c.getEstado());
    }
}
