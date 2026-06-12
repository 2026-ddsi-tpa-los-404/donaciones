package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaLogistica;
import ar.edu.utn.dds.k3003.integration.DonadoresRestClient;
import ar.edu.utn.dds.k3003.integration.LogisticaRestClient;
import ar.edu.utn.dds.k3003.repositories.jpa.CategoriaJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.DonacionesJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.IdentificadorJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.ProductoJpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

  @Value("${donadores.url}")
  private String donadoresUrl;

  @Value("${logistica.url}")
  private String logisticaUrl;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder.requestFactory(() -> new HttpComponentsClientHttpRequestFactory()).build();
  }

  @Bean
  public DonadoresRestClient donadoresRestClient(RestTemplate restTemplate) {
    return new DonadoresRestClient(restTemplate, donadoresUrl);
  }

  @Bean
  public LogisticaRestClient logisticaRestClient(RestTemplate restTemplate) {
    return new LogisticaRestClient(restTemplate, logisticaUrl);
  }

  @Bean
  public Fachada fachada(
      DonacionesJpaRepository donacionesRepository,
      ProductoJpaRepository productoRepository,
      IdentificadorJpaRepository identificadorRepository,
      CategoriaJpaRepository categoriaRepository,
      FachadaDonadoresYEntidades fachadaDonadoresYEntidades,
      FachadaLogistica fachadaLogistica) {
    Fachada fachada =
        new Fachada(
            donacionesRepository,
            productoRepository,
            identificadorRepository,
            categoriaRepository);
    fachada.setFachadaDonadoresYEntidades(fachadaDonadoresYEntidades);
    fachada.setFachadaLogistica(fachadaLogistica);
    return fachada;
  }
}
