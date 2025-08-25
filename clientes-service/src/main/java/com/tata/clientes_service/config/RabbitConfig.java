package com.tata.clientes_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
  public static final String EXCHANGE = "clientes.events";
  public static final String RK_CREATED = "cliente.created";
  public static final String RK_UPDATED = "cliente.updated";
  public static final String RK_DELETED = "cliente.deleted";

  @Bean TopicExchange clientesExchange(){ return new TopicExchange(EXCHANGE, true, false); }

  @Bean public MessageConverter jacksonConverter(){ return new Jackson2JsonMessageConverter(); }

  @Bean public RabbitTemplate rabbitTemplate(ConnectionFactory cf, MessageConverter mc){
    var rt = new RabbitTemplate(cf); rt.setMessageConverter(mc); return rt;
  }
}

