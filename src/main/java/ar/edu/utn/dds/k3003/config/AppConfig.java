package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.repositories.jpa.CategoriaJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.DonacionesJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.IdentificadorJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.ProductoJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

  @Bean
  public Fachada fachada(
      DonacionesJpaRepository donacionesRepository,
      ProductoJpaRepository productoRepository,
      IdentificadorJpaRepository identificadorRepository,
      CategoriaJpaRepository categoriaRepository) {
    return new Fachada(donacionesRepository, productoRepository, identificadorRepository, categoriaRepository);
  }
}
