package com.tata.cuentas_service.application.impl;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.tata.cuentas_service.repository.CuentaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(value = "app.events.enabled", havingValue = "true")
@RequiredArgsConstructor
public class ClienteEventsListener {
    private final CuentaRepository cuentaRepo;

    @RabbitListener(queues = "cuentas.clientes")
    public void onMessage(Map<String, Object> evt) {
        var routing = (String) evt.getOrDefault("routingKey", "");
        var clienteId = (String) evt.get("clienteId");
        if (clienteId == null)
            return;

        if (routing.contains("deleted")) {
            var cuentas = cuentaRepo.findByClienteId(clienteId);
            cuentas.forEach(c -> c.setEstado(Boolean.FALSE));
            cuentaRepo.saveAll(cuentas);
            log.info("Cuentas inhabilitadas para cliente {}", clienteId);
        }
    }
}
