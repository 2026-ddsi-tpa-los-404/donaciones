package ar.edu.utn.dds.k3003.api.controller;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.repositories.jpa.CategoriaJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.DonacionesJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.IdentificadorJpaRepository;
import ar.edu.utn.dds.k3003.repositories.jpa.ProductoJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final DonacionesJpaRepository donacionesRepository;
  private final ProductoJpaRepository productoRepository;
  private final IdentificadorJpaRepository identificadorRepository;
  private final CategoriaJpaRepository categoriaRepository;
  private final Fachada fachada;

  public AdminController(
      DonacionesJpaRepository donacionesRepository,
      ProductoJpaRepository productoRepository,
      IdentificadorJpaRepository identificadorRepository,
      CategoriaJpaRepository categoriaRepository,
      Fachada fachada) {
    this.donacionesRepository = donacionesRepository;
    this.productoRepository = productoRepository;
    this.identificadorRepository = identificadorRepository;
    this.categoriaRepository = categoriaRepository;
    this.fachada = fachada;
  }

  @DeleteMapping("/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetDatabase() {
    fachada.publicarBajaDeTodo(); // publicacion de eventos
    donacionesRepository.deleteAll();
    productoRepository.deleteAll();
    identificadorRepository.deleteAll();
    categoriaRepository.deleteAll();
  }
}
