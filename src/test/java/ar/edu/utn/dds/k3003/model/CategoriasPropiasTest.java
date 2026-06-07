package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.CategoriaDTO;
import java.util.List;
import java.util.NoSuchElementException;
import ar.edu.utn.dds.k3003.app.Application;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class CategoriasPropiasTest {

  @Autowired
  Fachada instancia;

  @Test
  void agregarCategoriaGuardaYRetornaConID() {
    CategoriaDTO dto = new CategoriaDTO(null, "Alimentos", "Comida y bebida", null);
    CategoriaDTO resultado = instancia.agregarCategoria(dto);

    Assertions.assertNotNull(resultado.id());
    Assertions.assertEquals("Alimentos", resultado.nombre());
  }

  @Test
  void buscarCategoriaPorIDDevuelveCategoriaCorrrecta() {
    CategoriaDTO guardada = instancia.agregarCategoria(
        new CategoriaDTO(null, "Higiene", "Productos de higiene personal", null));

    CategoriaDTO encontrada = instancia.buscarCategoriaPorID(guardada.id());

    Assertions.assertEquals(guardada.id(), encontrada.id());
    Assertions.assertEquals("Higiene", encontrada.nombre());
  }

  @Test
  void buscarCategoriaPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.buscarCategoriaPorID("id-inexistente"));
  }

  @Test
  void listarCategoriasDevuelveCategoriasRegistradas() {
    instancia.agregarCategoria(new CategoriaDTO(null, "Cat1", "desc1", null));
    instancia.agregarCategoria(new CategoriaDTO(null, "Cat2", "desc2", null));

    List<CategoriaDTO> lista = instancia.listarCategorias();

    Assertions.assertEquals(2, lista.size());
  }

  @Test
  void eliminarCategoriaPorIDEliminaCategoria() {
    CategoriaDTO guardada = instancia.agregarCategoria(
        new CategoriaDTO(null, "Para eliminar", "desc", null));

    instancia.eliminarCategoriaPorID(guardada.id());

    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.buscarCategoriaPorID(guardada.id()));
  }

  @Test
  void eliminarCategoriaPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.eliminarCategoriaPorID("id-inexistente"));
  }
}
