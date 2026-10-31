package ar.edu.utn.dds.k3003.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String EXCHANGE_EVENTOS = "donatrack.eventos";

  @Bean
  public TopicExchange exchangeEventos() {
    return new TopicExchange(EXCHANGE_EVENTOS, true, false);
  }
}
