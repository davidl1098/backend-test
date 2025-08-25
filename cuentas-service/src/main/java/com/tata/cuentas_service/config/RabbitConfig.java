package com.tata.cuentas_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
  @Bean public TopicExchange clientesExchange() {
    return new TopicExchange("clientes.events", true, false);
  }
  @Bean public Queue clientesQueue() {
    return new Queue("cuentas.clientes", true);
  }
  @Bean public Binding binding(Queue clientesQueue, TopicExchange clientesExchange) {
    return BindingBuilder.bind(clientesQueue).to(clientesExchange).with("cliente.*");
  }
}