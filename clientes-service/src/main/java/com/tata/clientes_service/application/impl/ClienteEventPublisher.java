package com.tata.clientes_service.application.impl;

import com.tata.clientes_service.config.RabbitConfig;
import com.tata.clientes_service.domain.Cliente;
import com.tata.clientes_service.web.dto.ClienteEventDto;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClienteEventPublisher {
  private final RabbitTemplate rabbit;

  public void emitCreated(Cliente c){ publish("cliente.created", c); }
  public void emitUpdated(Cliente c){ publish("cliente.updated", c); }
  public void emitDeleted(Cliente c){ publish("cliente.deleted", c); }

  private void publish(String rk, Cliente c){
    var e = new ClienteEventDto(UUID.randomUUID().toString(), rk, Instant.now(),
      c.getId(), c.getClienteId(), c.getNombre(), c.getGenero(), c.getEdad(),
      c.getIdentificacion(), c.getDireccion(), c.getTelefono(), c.getEstado());
    rabbit.convertAndSend(RabbitConfig.EXCHANGE, rk, e);
  }
}

