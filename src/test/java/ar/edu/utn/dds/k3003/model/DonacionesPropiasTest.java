package ar.edu.utn.dds.k3003.model;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.EstadoDonacionEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.logistica.DepositoDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.logistica.TipoAlgoritmoEnum;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaLogistica;
import java.time.LocalDate;
import java.util.List;
import ar.edu.utn.dds.k3003.exceptions.DonacionNoEncontradaException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ar.edu.utn.dds.k3003.app.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class DonacionesPropiasTest {

  @Autowired
  Fachada instancia;

  @MockitoBean FachadaDonadoresYEntidades fachadaDonadoresYEntidades;
  @MockitoBean FachadaLogistica fachadaLogistica;

  DonadorDTO donadorEjemplo;
  DonacionDTO donacionEjemplo;

  @BeforeEach
  void setUp() {
    instancia.setFachadaDonadoresYEntidades(fachadaDonadoresYEntidades);
    instancia.setFachadaLogistica(fachadaLogistica);

    donadorEjemplo =
        new DonadorDTO(
            "donador1", "Juan", "Perez", 5, "juan@mail.com", "123", "Buenos Aires", null, "dni1");
    donacionEjemplo =
        new DonacionDTO(
            null, "donador1", "deposito1", "ropa de invierno", "producto1", 3, EstadoDonacionEnum.INGRESADA);
  }

  @Test
  void registrarDonacionAsignaIDYEstadoIngresada() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO resultado = instancia.registrarDonacion(donacionEjemplo);

    Assertions.assertNotNull(resultado.id());
    Assertions.assertEquals(EstadoDonacionEnum.INGRESADA, resultado.estado());
  }

  @Test
  void buscarDonacionPorIDDevuelveLaDonacionCorrecta() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    DonacionDTO encontrada = instancia.buscarDonacionPorID(registrada.id());

    Assertions.assertEquals(registrada.id(), encontrada.id());
    Assertions.assertEquals("donador1", encontrada.donadorID());
  }

  @Test
  void buscarDonacionPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(
        RuntimeException.class,
        () -> instancia.buscarDonacionPorID("donacion-que-no-existe"));
  }

  @Test
  void cambiarEstadoDeDonacionActualizaElEstado() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    DonacionDTO actualizada = instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.ACEPTADA);

    Assertions.assertEquals(EstadoDonacionEnum.ACEPTADA, actualizada.estado());
  }

  @Test
  void buscarPorDonadorYFechaInicioDevuelveDonacionesFiltradas() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);

    List<DonacionDTO> resultado =
        instancia.buscarPorDonadorYFechaInicio("donador1", LocalDate.of(2025, 1, 1));

    Assertions.assertFalse(resultado.isEmpty());
    Assertions.assertTrue(resultado.stream().anyMatch(d -> d.id().equals(registrada.id())));
  }

  @Test
  void buscarPorDonadorYFechaInicioSinDonacionesLanzaExcepcion() {
    Assertions.assertThrows(
        DonacionNoEncontradaException.class,
        () -> instancia.buscarPorDonadorYFechaInicio("donador-sin-donaciones", LocalDate.now()));
  }

  @Test
  void registrarQuejaEnDonacionCambiaEstadoAConQueja() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));
    when(fachadaDonadoresYEntidades.agregarQueja(any()))
        .thenReturn(new QuejaDTO("queja1", "donacion1", "donador1", null, "producto roto"));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.ACEPTADA);

    DonacionDTO conQueja = instancia.registrarQuejaEnDonacion(registrada.id(), "producto roto");

    Assertions.assertEquals(EstadoDonacionEnum.CONQUEJA, conQueja.estado());
  }

  @Test
  void registrarDonacionFallaSiDonadorNoPuedeDonar() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.FALSE);

    Assertions.assertThrows(
        RuntimeException.class,
        () -> instancia.registrarDonacion(donacionEjemplo));
  }

  @Test
  void cambiarEstadoDonacionInexistenteLanzaExcepcion() {
    Assertions.assertThrows(
        RuntimeException.class,
        () -> instancia.cambiarEstadoDeDonacion("id-que-no-existe", EstadoDonacionEnum.ACEPTADA));
  }

  @Test
  void cambiarEstadoTransicionInvalidaIngresadaAConQuejaLanzaExcepcion() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);

    Assertions.assertThrows(
        RuntimeException.class,
        () -> instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.CONQUEJA));
  }

  @Test
  void cambiarEstadoTransicionAceptadaAConQuejaExitosa() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.ACEPTADA);

    DonacionDTO resultado = instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.CONQUEJA);

    Assertions.assertEquals(EstadoDonacionEnum.CONQUEJA, resultado.estado());
  }

  @Test
  void cambiarEstadoTransicionInvalidaConQuejaAAceptadaLanzaExcepcion() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.ACEPTADA);
    instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.CONQUEJA);

    Assertions.assertThrows(
        RuntimeException.class,
        () -> instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.ACEPTADA));
  }

  @Test
  void registrarQuejaFallidaNoModificaElEstado() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));
    when(fachadaDonadoresYEntidades.agregarQueja(any()))
        .thenThrow(new RuntimeException("Servicio de quejas no disponible"));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    instancia.cambiarEstadoDeDonacion(registrada.id(), EstadoDonacionEnum.ACEPTADA);

    Assertions.assertThrows(
        RuntimeException.class,
        () -> instancia.registrarQuejaEnDonacion(registrada.id(), "producto roto"));

    DonacionDTO despues = instancia.buscarDonacionPorID(registrada.id());
    Assertions.assertEquals(EstadoDonacionEnum.ACEPTADA, despues.estado());
  }

  @Test
  void listarDonacionesDevuelveDonacionesRegistradas() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    List<DonacionDTO> lista = instancia.listarDonaciones();

    Assertions.assertFalse(lista.isEmpty());
    Assertions.assertTrue(lista.stream().anyMatch(d -> d.id().equals(registrada.id())));
  }

  @Test
  void eliminarDonacionPorIDEliminaDonacion() {
    when(fachadaDonadoresYEntidades.buscarDonadorPorID("donador1")).thenReturn(donadorEjemplo);
    when(fachadaDonadoresYEntidades.puedeDonar("donador1")).thenReturn(Boolean.TRUE);
    when(fachadaLogistica.gestionarDonacion(any(), any(), any(), anyInt()))
        .thenReturn(new DepositoDTO("deposito1", TipoAlgoritmoEnum.SUB_ATENDIDOS, "deposito1", "direccion1", 1000, null));

    DonacionDTO registrada = instancia.registrarDonacion(donacionEjemplo);
    String idRegistrada = registrada.id();
    instancia.eliminarDonacionPorID(idRegistrada);

    Assertions.assertThrows(RuntimeException.class, () -> instancia.buscarDonacionPorID(idRegistrada));
  }

  @Test
  void eliminarDonacionPorIDInexistenteLanzaExcepcion() {
    Assertions.assertThrows(RuntimeException.class, () -> instancia.eliminarDonacionPorID("id-inexistente"));
  }
}
