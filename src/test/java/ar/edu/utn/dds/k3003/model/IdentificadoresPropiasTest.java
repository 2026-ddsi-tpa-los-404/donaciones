package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.IdentificadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.TipoIdentificadorEnum;
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
public class IdentificadoresPropiasTest {

  @Autowired
  Fachada instancia;

  @Test
  void agregarIdentificadorGuardaYRetornaConID() {
    IdentificadorDTO dto = new IdentificadorDTO(null, TipoIdentificadorEnum.QR, "identificador QR");
    IdentificadorDTO resultado = instancia.agregarIdentificador(dto);

    Assertions.assertNotNull(resultado.id());
    Assertions.assertEquals(TipoIdentificadorEnum.QR, resultado.tipo());
  }

  @Test
  void buscarIdentificadorPorIDDevuelveIdentificadorCorrecto() {
    IdentificadorDTO guardado = instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.CODIGODEBARRAS, "barras"));

    IdentificadorDTO encontrado = instancia.buscarIdentificadorPorID(guardado.id());

    Assertions.assertEquals(guardado.id(), encontrado.id());
    Assertions.assertEquals(TipoIdentificadorEnum.CODIGODEBARRAS, encontrado.tipo());
  }

  @Test
  void buscarIdentificadorPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.buscarIdentificadorPorID("id-inexistente"));
  }

  @Test
  void listarIdentificadoresDevuelveIdentificadoresRegistrados() {
    instancia.agregarIdentificador(new IdentificadorDTO(null, TipoIdentificadorEnum.QR, "qr1"));
    instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.CODIGODEBARRAS, "cb1"));

    List<IdentificadorDTO> lista = instancia.listarIdentificadores();

    Assertions.assertEquals(2, lista.size());
  }

  @Test
  void eliminarIdentificadorPorIDEliminaIdentificador() {
    IdentificadorDTO guardado = instancia.agregarIdentificador(
        new IdentificadorDTO(null, TipoIdentificadorEnum.QR, "qr para eliminar"));

    instancia.eliminarIdentificadorPorID(guardado.id());

    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.buscarIdentificadorPorID(guardado.id()));
  }

  @Test
  void eliminarIdentificadorPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(NoSuchElementException.class,
        () -> instancia.eliminarIdentificadorPorID("id-inexistente"));
  }
}
