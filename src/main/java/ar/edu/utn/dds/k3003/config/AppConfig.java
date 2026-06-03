package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.Fachada;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public Fachada fachada() {
    return new Fachada();
  }
}
