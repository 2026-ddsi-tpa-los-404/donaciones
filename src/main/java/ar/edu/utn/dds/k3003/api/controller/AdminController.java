package ar.edu.utn.dds.k3003.api.controller;

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

  public AdminController(
      DonacionesJpaRepository donacionesRepository,
      ProductoJpaRepository productoRepository,
      IdentificadorJpaRepository identificadorRepository,
      CategoriaJpaRepository categoriaRepository) {
    this.donacionesRepository = donacionesRepository;
    this.productoRepository = productoRepository;
    this.identificadorRepository = identificadorRepository;
    this.categoriaRepository = categoriaRepository;
  }

  @DeleteMapping("/reset")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void resetDatabase() {
    donacionesRepository.deleteAll();
    productoRepository.deleteAll();
    identificadorRepository.deleteAll();
    categoriaRepository.deleteAll();
  }
}
